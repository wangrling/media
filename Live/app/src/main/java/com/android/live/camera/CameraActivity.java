package com.android.live.camera;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.android.live.camera.app.AppController;
import com.android.live.camera.app.CameraAppUI;
import com.android.live.camera.app.CameraProvider;
import com.android.live.camera.app.CameraServices;
import com.android.live.camera.app.CameraServicesImpl;
import com.android.live.camera.app.LocationManager;
import com.android.live.camera.app.ModuleManager;
import com.android.live.camera.app.OrientationManager;
import com.android.live.camera.app.OrientationManagerImpl;
import com.android.live.camera.debug.Log;
import com.android.live.camera.module.ModuleController;
import com.android.live.camera.one.OneCameraOpener;
import com.android.live.camera.one.config.OneCameraFeatureConfig;
import com.android.live.camera.settings.Keys;
import com.android.live.camera.settings.ResolutionSetting;
import com.android.live.camera.settings.SettingsManager;
import com.android.live.camera.stats.profiler.Profile;
import com.android.live.camera.stats.profiler.Profiler;
import com.android.live.camera.stats.profiler.Profilers;
import com.android.live.camera.ui.AbstractTutorialOverlay;
import com.android.live.camera.ui.PreviewStatusListener;
import com.android.live.camera.util.ApiHelper;
import com.android.live.camera.util.CameraPerformanceTracker;
import com.android.live.camera.util.QuickActivity;

import java.lang.ref.WeakReference;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE_SECURE;
import static android.provider.MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE;


public class CameraActivity extends QuickActivity implements AppController {
    private static final Log.Tag TAG = new Log.Tag("LocationManager");

    // The intent extra for camera from secure lock screen. True if the gallery
    // should only show newly captured pictures. sSecureAlbumId does not
    // increment. This is used when switching between camera, camcorder, and
    // panorama (全景). If the extra is not set, it is in the normal camera mode.
    public static final String SECURE_CAMERA_EXTRA = "secure_camera";

    private static final int MSG_CLEAR_SCREEN_ON_FLAG = 2;
    private final Profiler mProfiler = Profilers.instance().guard();
    // Activity创建时间
    private long mOnCreateTime;
    private Context mAppContext;
    // 主线程Handler
    private Handler mMainHandler;
    // 判断activity是否停止
    private boolean mPaused;
    private LocationManager mLocationManager;
    private OrientationManagerImpl mOrientationManager;

    // 安全拍照模式，不会暴露手机中的数据。如果Caller设置EXTRA_OUTPUT，相机会把图片保存到指定目录中，如果没有
    // 设置，相机会将缩小的Bitmap数据返回。
    private boolean mSecureCamera;

    private boolean mHasCriticalPermissions;

    // 首次安装应用启动
    // private FirstRunDialog mFirstRunDialog;

    private SettingsManager mSettingsManager;

    private FatalErrorHandler mFatalErrorHandler;

    // 模式
    private int mCurrentModeIndex;

    @Override
    protected void onCreateTasks(Bundle savedInstanceState) {
        Profile profile = mProfiler.create("CameraActivity.onCreateTasks").start();
        CameraPerformanceTracker.onEvent(CameraPerformanceTracker.ACTIVITY_START);
        mOnCreateTime = System.currentTimeMillis();
        mAppContext = getApplicationContext();
        mMainHandler = new MainHandler(this, getMainLooper());
        // 重写LocationManager，需要获取最佳的位置。
        mLocationManager = new LocationManager(mAppContext);
        mOrientationManager = new OrientationManagerImpl(this, mMainHandler);
        mSettingsManager = getServices().getSettingsManager();

        checkPermissions();
        if (!mHasCriticalPermissions) {
            Log.v(TAG, "onCreate: Missing critical permissions.");
            finish();
            return;
        }
        profile.mark();



        // Check if this is in the secure camera mode.
        Intent intent = getIntent();
        String action = intent.getAction();
        if (INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE.equals(action) ||
                ACTION_IMAGE_CAPTURE_SECURE.equals(action)) {
            mSecureCamera = true;
        } else {
            mSecureCamera = intent.getBooleanExtra(SECURE_CAMERA_EXTRA, false);
        }
    }

    @Override
    protected void onResumeTasks() {
        mPaused = false;
        checkPermissions();
        if (!mHasCriticalPermissions) {
            Log.w(TAG, "onResume: Missing critical permissions");
            finish();
            return ;
        }
        if (!isSecureCamera() && !isCaptureIntent()) {
            // Show the dialog if necessary. The resume logic will be invoked
            // at the onFirstRunStateReady() callback.
            try {
                // mFirstRunDialog.showIfNecessary();
            } catch (AssertionError e) {
                Log.e(TAG, "Creating camera controller failed.", e);
                // mFatalErrorHandler.onGenericCameraAccessFailure();
            }
        } else {
            // In secure mode from lockscreen, we go straight to camera and will
            // show first run dialog next time user enters launcher.
            Log.v(TAG, "in secure mode, skipping first run dialog check");
            resume();
        }
    }

    @Override
    protected void onPauseTasks() {
        CameraPerformanceTracker.onEvent(CameraPerformanceTracker.ACTIVITY_PAUSE);
        Profile profile = mProfiler.create("CameraActivity.onPause").start();

        /*
         * Save the last module index after all secure camera and icon launches,
         * not just on mode switches.
         *
         * Right now we exclude capture intents from this logic, because we also
         * ignore the cross-Activity recovery logic in onStart for capture intents.
         */
        if (!isCaptureIntent()) {
            // 默认存储位置，也就是app都可以访问。
            mSettingsManager.set(SettingsManager.SCOPE_GLOBAL,
                    Keys.KEY_STARTUP_MODULE_INDEX,
                    mCurrentModeIndex);
        }

        mPaused = true;


    }

    private void resume() {

    }

    /**
     * Checks if any of the needed Android runtime permissions are missing.
     * If they are, then launch the permissions activity under one of the following conditions:
     * a) The permissions dialogs have not run yet. We will ask for permission only once.
     * b) If the missing permissions are critical to the app running, we will display a fatal error dialog.
     * Critical permissions are: camera, microphone and storage. The app cannot run without them.
     * Non-critical permission is location.
     */
    private void checkPermissions() {
        if (!ApiHelper.isMOrHigher()) {
            Log.v(TAG, "not running on M, skipping permission checks");
            mHasCriticalPermissions = true;
            return;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mHasCriticalPermissions = true;
        } else {
            mHasCriticalPermissions = false;
        }

        if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                !mSettingsManager.getBoolean(SettingsManager.SCOPE_GLOBAL, Keys.KEY_HAS_SEEN_PERMISSIONS_DIALOGS)) ||
                !mHasCriticalPermissions) {
            Intent intent = new Intent(this, PermissionsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isCaptureIntent() {
        if (MediaStore.ACTION_VIDEO_CAPTURE.equals(getIntent().getAction()) ||
                MediaStore.ACTION_IMAGE_CAPTURE.equals(getIntent().getAction()) ||
                MediaStore.ACTION_IMAGE_CAPTURE_SECURE.equals(getIntent().getAction())) {
            return true;
        }
        return false;
    }

    private boolean isSecureCamera() {
        return mSecureCamera;
    }

    @Override
    public Context getAndroidContext() {
        return null;
    }

    @Override
    public OneCameraFeatureConfig getCameraFeatureConfig() {
        return null;
    }

    @Override
    public Dialog createDialog() {
        return null;
    }

    @Override
    public String getModuleScope() {
        return null;
    }

    @Override
    public String getCameraScope() {
        return null;
    }

    @Override
    public void launchActivityByIntent(Intent intent) {

    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public ModuleController getCurrentModuleController() {
        return null;
    }

    @Override
    public int getCurrentModuleIndex() {
        return 0;
    }

    @Override
    public int getModuleId(int modeIndex) {
        return 0;
    }

    @Override
    public int getQuickSwitchToModuleId(int currentModuleIndex) {
        return 0;
    }

    @Override
    public int getPreferredChildModeIndex(int modeIndex) {
        return 0;
    }

    @Override
    public void onModeSelected(int moduleIndex) {

    }

    @Override
    public void onSettingsSelected() {

    }

    @Override
    public void freezeScreenUntilPreviewReady() {

    }

    @Override
    public SurfaceTexture getPreviewBuffer() {
        return null;
    }

    @Override
    public void onPreviewReadyToStart() {

    }

    @Override
    public void onPreviewStarted() {

    }

    @Override
    public void addPreviewAreaSizeChangedListener(PreviewStatusListener.PreviewAreaChangedListener listener) {

    }

    @Override
    public void removePreviewAreaSizeChangedListener(PreviewStatusListener.PreviewAreaChangedListener listener) {

    }

    @Override
    public void setupOneShotPreviewListener() {

    }

    @Override
    public void updatePreviewAspectRatio(float aspectRatio) {

    }

    @Override
    public void updatePreviewTransformFullscreen(Matrix matrix, float aspectRatio) {

    }

    @Override
    public RectF getFullscreenRect() {
        return null;
    }

    @Override
    public void updatePreviewTransform(Matrix matrix) {

    }

    @Override
    public void setPreviewStatusListener(PreviewStatusListener previewStatusListener) {

    }

    @Override
    public FrameLayout getModuleLayoutRoot() {
        return null;
    }

    @Override
    public void lockOrientation() {

    }

    @Override
    public void unlockOrientation() {

    }

    @Override
    public void setShutterEventsListener(ShutterEventsListener listener) {

    }

    @Override
    public void setShutterEnabled(boolean enabled) {

    }

    @Override
    public boolean isShutterEnabled() {
        return false;
    }

    @Override
    public void startFlashAnimation(boolean shortFlash) {

    }

    @Override
    public void startPreCaptureAnimation() {

    }

    @Override
    public void cancelPreCaptureAnimation() {

    }

    @Override
    public void startPostCaptureAnimation() {

    }

    @Override
    public void startPostCaptureAnimation(Bitmap thumbnail) {

    }

    @Override
    public void cancelPostCaptureAnimation() {

    }

    @Override
    public void notifyNewMedia(Uri uri) {

    }

    @Override
    public void enableKeepScreenOn(boolean enabled) {

    }

    @Override
    public CameraProvider getCameraProvider() {
        return null;
    }

    @Override
    public OneCameraOpener getCameraOpener() {
        return null;
    }

    @Override
    public OrientationManager getOrientationManager() {
        return null;
    }

    @Override
    public LocationManager getLocationManager() {
        return null;
    }

    @Override
    public SettingsManager getSettingsManager() {
        return null;
    }

    @Override
    public ResolutionSetting getResolutionSetting() {
        return null;
    }

    @Override
    public CameraServices getServices() {
        return CameraServicesImpl.instance();
    }

    @Override
    public FatalErrorHandler getFatalErrorHandler() {
        return null;
    }

    @Override
    public CameraAppUI getCameraAppUI() {
        return null;
    }

    @Override
    public ModuleManager getModuleManager() {
        return null;
    }

    @Override
    public ButtonManager getButtonManager() {
        return null;
    }

    @Override
    public SoundPlayer getSoundPlayer() {
        return null;
    }

    @Override
    public boolean isAutoRotateScreen() {
        return false;
    }

    @Override
    public void showTutorial(AbstractTutorialOverlay tutorial) {

    }

    @Override
    public void finishActivityWithIntentCompleted(Intent resultIntent) {

    }

    @Override
    public void finishActivityWithIntentCanceled() {

    }


    private class MainHandler extends Handler {
        final WeakReference<CameraActivity> mActivity;

        public MainHandler(CameraActivity activity, Looper looper) {
            super(looper);
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CameraActivity activity = mActivity.get();
            if (activity == null) {
                return ;
            }
            switch (msg.what) {
                // 保持屏幕长亮标志
                case MSG_CLEAR_SCREEN_ON_FLAG: {
                    if (!activity.mPaused) {
                        activity.getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
            }
        }
    }
}

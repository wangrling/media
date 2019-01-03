package com.google.android.camera;

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
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.camera.app.AppController;
import com.google.android.camera.app.CameraAppUI;
import com.google.android.camera.app.CameraProvider;
import com.google.android.camera.app.CameraServices;
import com.google.android.camera.app.CameraServicesImpl;
import com.google.android.camera.app.FirstRunDialog;
import com.google.android.camera.app.LocationManager;
import com.google.android.camera.app.ModuleManager;
import com.google.android.camera.app.OrientationManager;
import com.google.android.camera.app.OrientationManagerImpl;
import com.google.android.camera.debug.Log;
import com.google.android.camera.module.ModuleController;
import com.google.android.camera.one.OneCameraOpener;
import com.google.android.camera.one.config.OneCameraFeatureConfig;
import com.google.android.camera.settings.ResolutionSetting;
import com.google.android.camera.settings.SettingsManager;
import com.google.android.camera.stats.profiler.Profile;
import com.google.android.camera.stats.profiler.Profiler;
import com.google.android.camera.stats.profiler.Profilers;
import com.google.android.camera.ui.AbstractTutorialOverlay;
import com.google.android.camera.ui.PreviewStatusListener;
import com.google.android.camera.util.ApiHelper;
import com.google.android.camera.util.CameraPerformanceTracker;
import com.google.android.camera.util.QuickActivity;

import java.lang.ref.WeakReference;

public class CameraActivity extends QuickActivity implements AppController {

    private static final Log.Tag TAG = new Log.Tag("CameraActivity");

    private static final String INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE =
            "android.media.action.STILL_IMAGE_CAMERA_SECURE";
    public static final String ACTION_IMAGE_CAPTURE_SECURE =
            "android.media.action.IMAGE_CAPTURE_SECURE";

    // The intent extra for camera from secure lock screen. True if the gallery
    // should only show newly captured pictures. sSecureAlbumId does not
    // increment. This is used when switching between camera, camcorder, and
    // panorama. If the extra is not set, it is in the normal camera mode.
    public static final String SECURE_CAMERA_EXTRA = "secure_camera";

    private static final int MSG_CLEAR_SCREEN_ON_FLAG = 2;
    private static final long SCREEN_DELAY_MS = 2 * 60 * 1000; // 2 mins.
    /** Load metadata for 10 items ahead of our current. */
    private static final int FILMSTRIP_PRELOAD_AHEAD_ITEMS = 10;
    private static final int PERMISSIONS_ACTIVITY_REQUEST_CODE = 1;
    private static final int PERMISSIONS_RESULT_CODE_OK = 1;
    private static final int PERMISSIONS_RESULT_CODE_FAILED = 2;

    /** Should be used wherever a context is needed. */
    private Context mAppContext;

    /**
     * Camera fatal error handling:
     * 1) Present error dialog to guide users to exit the app;
     * 2) If users hit home button, onPause should just call finish() to exit the app.
     */
    private boolean mCameraFatalError = false;

    /**
     * Whether onResume should reset the view to the preview.
     */
    private boolean mResetToPreviewOnResume = true;

    private final Profiler mProfiler = Profilers.instance().guard();

    /** First run dialog */
    private FirstRunDialog mFirstRunDialog;

    private LocationManager mLocationManager;
    private long mOnCreateTime;
    private Handler mMainHandler;
    private OrientationManager mOrientationManager;
    private SettingsManager mSettingsManager;

    private SoundPlayer mSoundPlayer;

    private boolean mPaused;

    /** Holds configuration for various OneCamera features. */
    private OneCameraFeatureConfig mFeatureConfig;

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
    public SurfaceTexture gtePreviewBuffer() {
        return null;
    }

    @Override
    public void onPreviewReadyToStart() {

    }

    @Override
    public void onPreviewStarted() {

    }

    @Override
    public void addPreviewAreaSizeChangeListener(PreviewStatusListener.PreviewAreaChangedListener listener) {

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
    public android.location.LocationManager getLocationManager() {
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
    public void showTutorial(AbstractTutorialOverlay tutorial) {

    }

    @Override
    public void finishActivityWithIntentCompleted(Intent resultIntent) {

    }

    @Override
    public void finishActivityWithIntentCanceled() {

    }

    private static class MainHandler extends Handler {
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
                case MSG_CLEAR_SCREEN_ON_FLAG: {
                    // 清除屏幕常亮标志
                    if (!activity.mPaused) {
                        activity.getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected void onCreateTasks(Bundle savedInstanceState) {
        Profile profile = mProfiler.create("CameraActivity.onCreateTasks").start();
        CameraPerformanceTracker.onEvent(CameraPerformanceTracker.ACTIVITY_START);
        mOnCreateTime = System.currentTimeMillis();
        mAppContext  = getApplicationContext();
        // 实现后续集中写
        mMainHandler = new MainHandler(this, getMainLooper());
        mLocationManager = new LocationManager(mAppContext);
        mOrientationManager = new OrientationManagerImpl(this, mMainHandler);
        mSettingsManager = getServices().getSettingsManager();
        mSoundPlayer = new SoundPlayer(mAppContext);
        mFeatureConfig = OneCameraFeatureConfigCreator.createDefault(getContentResolver(),
                getServices().getMemoryManager());
        mFatalErrorHandler = new FatalErrorHandlerImpl(this);
        checkPermissions();
        if (mHasCritialPermissions) {
            Log.v(TAG, "onCreate: Missing critical permissions.");
            finish();
            return ;
        }
        profile.mark();

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
}

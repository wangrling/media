package com.android.live.camera;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.android.live.R;
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
import com.android.live.camera.device.ActiveCameraDeviceTracker;
import com.android.live.camera.filmstrip.FilmstripController;
import com.android.live.camera.module.ModuleController;
import com.android.live.camera.one.OneCameraOpener;
import com.android.live.camera.one.config.OneCameraFeatureConfig;
import com.android.live.camera.one.config.OneCameraFeatureConfigCreator;
import com.android.live.camera.settings.Keys;
import com.android.live.camera.settings.ResolutionSetting;
import com.android.live.camera.settings.SettingsManager;
import com.android.live.camera.stats.profiler.Profile;
import com.android.live.camera.stats.profiler.Profiler;
import com.android.live.camera.stats.profiler.Profilers;
import com.android.live.camera.ui.AbstractTutorialOverlay;
import com.android.live.camera.ui.CameraActivityLayout;
import com.android.live.camera.ui.PreviewStatusListener;
import com.android.live.camera.util.ApiHelper;
import com.android.live.camera.util.CameraPerformanceTracker;
import com.android.live.camera.util.GoogleHelpHelper;
import com.android.live.camera.util.QuickActivity;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.ActionBar;

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

    // 记录现在和之前使用的相机设备。
    private ActiveCameraDeviceTracker mActiveCameraDeviceTracker;

    private ActionBar mActionBar;

    /**
     * Close activity when secure app passes lock screen or screen turns
     * off.
     */
    private BroadcastReceiver mShutdownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    // 管理应用的界面
    private CameraAppUI mCameraAppUI;

    private final Uri[] mNfcPushUris = new Uri[1];
    private Menu mActionBarMenu;

    private Intent mGalleryIntent;

    // 图片查看
    private FilmstripController mFilmstripController;

    /**
     * Whether onResume should reset the view to the preview.
     */
    private boolean mResetToPreviewOnResume;

    private SoundPlayer mSoundPlayer;

    /** Holds configuration for various OneCamera features. */
    private OneCameraFeatureConfig mFeatureConfig;

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
        mSoundPlayer = new SoundPlayer(mAppContext);
        mFeatureConfig = OneCameraFeatureConfigCreator.createDefault(getContentResolver(),
                getServices().getMemoryManager());

        checkPermissions();
        if (!mHasCriticalPermissions) {
            Log.v(TAG, "onCreate: Missing critical permissions.");
            finish();
            return;
        }
        profile.mark();

        // 切换到Glide4，接口全部改变，不用annotation生成接口。

        profile.mark();

        mActiveCameraDeviceTracker = ActiveCameraDeviceTracker.instance();

        profile.mark("OneCameraManager.get");

        // We suppress this flag via theme when drawing the system preview
        // background, but once we create activity here, reactivate to the
        // default value. The default is important for L, we don't want to
        // change app behavior, just starting background drawable layout.
        if (ApiHelper.isLOrHigher()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        profile.mark();
        setContentView(R.layout.activity_camera);
        profile.mark("setContentView");

        // A window background is set in styles.xml for the system to show a
        // drawable background with gray color and camera icon before the
        // activity is created. We set the background to null here to prevent
        // overdraw, all views must take care of drawing backgrounds if
        // necessary. This call to setBackgroundDrawable must occur after
        // setContentView, otherwise a background may be set again from the
        // style.
        getWindow().setBackgroundDrawable(null);

        // mActionBar = getActionBar();
        mActionBar = getSupportActionBar();
        // 隐藏Action title
        mActionBar.setDisplayShowTitleEnabled(false);
        // set actionbar background to 100% or 50% transparent
        if (ApiHelper.isLOrHigher()) {
            // mActionBar.setBackgroundDrawable(new ColorDrawable(0x00000000));
            // 100%透明
            mActionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.review_background)));
        } else {
            mActionBar.setBackgroundDrawable(new ColorDrawable(0x80000000));
        }



        // Check if this is in the secure camera mode.
        Intent intent = getIntent();
        String action = intent.getAction();
        if (INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE.equals(action) ||
                ACTION_IMAGE_CAPTURE_SECURE.equals(action)) {
            mSecureCamera = true;
        } else {
            mSecureCamera = intent.getBooleanExtra(SECURE_CAMERA_EXTRA, false);
        }

        if (mSecureCamera) {
            // Change the window flags so that secure camera can show when locked.
            Window win = getWindow();
            WindowManager.LayoutParams params = win.getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            win.setAttributes(params);

            // 注册监听，结束Activity

            // Filter for screen off so that we can finish secure camera
            // activity when screen is off.
            IntentFilter filter_screen_off = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mShutdownReceiver, filter_screen_off);

            // Filter for phone unlock so that we can finish secure camera
            // via this UI path:
            //    1. from secure lock screen, user starts secure camera
            //    2. user presses home button
            //    3. user unlocks phone
            IntentFilter filter_user_unlock = new IntentFilter(Intent.ACTION_USER_PRESENT);
            registerReceiver(mShutdownReceiver, filter_user_unlock);
        }

        mCameraAppUI = new CameraAppUI(this,
                (CameraActivityLayout) findViewById(R.id.activity_root_view), isCaptureIntent());

        // Nfc
        setupNfcBeamPush();
    }

    private void setupNfcBeamPush() {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(mAppContext);
        if (adapter == null) {
            return ;
        }
        if (!ApiHelper.HAS_SET_BEAM_PUSH_URIS) {
            // Disable beaming
            adapter.setNdefPushMessage(null, CameraActivity.this);
            return ;
        }

        adapter.setBeamPushUris(null, CameraActivity.this);
        adapter.setBeamPushUrisCallback(new NfcAdapter.CreateBeamUrisCallback() {
            @Override
            public Uri[] createBeamUris(NfcEvent event) {
                return mNfcPushUris;
            }
        }, CameraActivity.this);
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

    @Override
    protected void onDestroyTasks() {
        if (mSecureCamera) {
            unregisterReceiver(mShutdownReceiver);
        }

        // Ensure anything that checks for "isPaused" returns true.
        mPaused = true;

        mSettingsManager.removeAllListeners();


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filmstrip_menu, menu);
        mActionBarMenu = menu;

        // Add a button for launching the gallery.
        if (mGalleryIntent != null) {

        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_details:
                // 需要实现，不然会发生crash
                showDetailsDialog(mFilmstripController.getCurrentAdapterIndex());
                return true;
            case R.id.action_help_and_feedback:
                mResetToPreviewOnResume = false;
                new GoogleHelpHelper(this).launchGoogleHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showDetailsDialog(int currentAdapterIndex) {

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
        return mAppContext;
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
        return mCameraAppUI;
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

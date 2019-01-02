package com.google.android.camera;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager;

import com.google.android.camera.app.FirstRunDialog;
import com.google.android.camera.app.LocationManager;
import com.google.android.camera.app.OrientationManager;
import com.google.android.camera.app.OrientationManagerImpl;
import com.google.android.camera.debug.Log;
import com.google.android.camera.settings.SettingsManager;
import com.google.android.camera.stats.profiler.Profile;
import com.google.android.camera.stats.profiler.Profiler;
import com.google.android.camera.stats.profiler.Profilers;
import com.google.android.camera.util.CameraPerformanceTracker;
import com.google.android.camera.util.QuickActivity;

import java.lang.ref.WeakReference;

public class CameraActivity extends QuickActivity {

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

    private boolean mPaused;

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
        mMainHandler = new MainHandler(this, getMainLooper());
        mLocationManager = new LocationManager(mAppContext);
        mOrientationManager = new OrientationManagerImpl(this, mMainHandler);
        mSettingsManager = getServides().getSettingsManager();
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
}

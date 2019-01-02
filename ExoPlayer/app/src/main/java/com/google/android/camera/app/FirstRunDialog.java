package com.google.android.camera.app;

import android.app.Dialog;
import android.content.Context;
import android.util.Rational;

import com.google.android.camera.one.OneCameraManager;
import com.google.android.camera.settings.ResolutionSetting;
import com.google.android.camera.settings.ResolutionUtil;
import com.google.android.camera.settings.SettingsManager;

/**
 * The dialog to show when users open the app for the first time.
 * 和FirstRunDetector配套使用。
 */
public class FirstRunDialog {

    // CameraActivity构建监听实例。
    public interface FirstRunDialogListener {
        public void onFirstRunStateReady();
        public void onFirstRunDialogCancelled();
        public void onCameraAccessException();
    }

    // The default preference of aspect ratio.
    private static final Rational DEFAULT_ASPECT_RATIO = ResolutionUtil.ASPECT_RATIO_4x3;

    /** The default preference of whether enabling location recording. */
    private static final boolean DEFAULT_LOCATION_RECORDING_ENABLED = true;

    /** Listener to receive events. */
    private final FirstRunDialogListener mListener;

    /** The app controller. */
    private final AppController mAppController;

    /** The hardware manager. */
    private final OneCameraManager mOneCameraManager;

    /** The app context. */
    private final Context mContext;

    /** The resolution settings. */
    private final ResolutionSetting mResolutionSetting;

    /** The settings manager. */
    private final SettingsManager mSettingsManager;

    /** Aspect ratio preference dialog */
    private Dialog mAspectRatioPreferenceDialog;

    /** Location preference dialog */
    private Dialog mLocationPreferenceDialog;

    // Constructs a first run dialog.
    public FirstRunDialog(
            AppController appController,
            Context activityContext,
            ResolutionSetting resolutionSetting,
            SettingsManager settingsManager,
            OneCameraManager hardwareManager,
            FirstRunDialogListener listener) {
        mAppController = appController;
        mContext = activityContext;
        mResolutionSetting = resolutionSetting;
        mSettingsManager = settingsManager;
        mOneCameraManager = hardwareManager;
        mListener = listener;
    }


}

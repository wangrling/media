package com.android.live.camera.util;

import android.content.ContentResolver;

import com.android.live.camera.CameraModule;
import com.android.live.camera.app.AppController;
import com.android.live.camera.one.config.OneCameraFeatureConfig;

public class GcamHelper {

    public static CameraModule createGcamModule(AppController app,
                                                OneCameraFeatureConfig.HdrPlusSupportLevel hdrPlusSupportLevel) {
        return null;
    }

    public static boolean hasGcamAsSeparateModule(OneCameraFeatureConfig config) {
        return false;
    }

    public static boolean hasGcamCapture(OneCameraFeatureConfig config) {
        return false;
    }

    public static OneCameraFeatureConfig.HdrPlusSupportLevel determineHdrPlusSupportLevel(
            ContentResolver contentResolver, boolean useCaptureModule) {
        return OneCameraFeatureConfig.HdrPlusSupportLevel.NONE;
    }
}
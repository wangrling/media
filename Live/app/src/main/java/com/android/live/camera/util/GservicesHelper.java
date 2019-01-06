package com.android.live.camera.util;

import android.content.ContentResolver;

public class GservicesHelper {

    public static String getBlacklistedResolutionsBack(ContentResolver contentResolver) {
        return "";
    }

    public static String getBlacklistedResolutionsFront(ContentResolver contentResolver) {
        return "";
    }

    public static boolean isCaptureModuleDisabled(ContentResolver contentResolver) {
        return false;
    }

    public static boolean isJankStatisticsEnabled(ContentResolver contentResolver) {
        return false;
    }

    public static int getCaptureSupportLevelOverrideBack(ContentResolver contentResolver) {
        return -1;
    }

    public static int getCaptureSupportLevelOverrideFront(ContentResolver contentResolver) {
        return -1;
    }

    public static int getMaxAllowedNativeMemoryMb(ContentResolver contentResolver) {
        return -1;
    }

    public static int getMaxAllowedImageReaderCount(ContentResolver contentResolver) {
        return 15;
    }

    public static boolean useCamera2ApiThroughPortabilityLayer(ContentResolver contentResolver) {
        // Use the camera2 API by default. This only affects PhotoModule on L.
        return true;
    }

    public static boolean isGcamEnabled(ContentResolver contentResolver) {
        return false;
    }
}

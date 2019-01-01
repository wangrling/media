package com.google.android.camera.util;

import android.os.Build;

public class ApiHelper {

    public static final boolean HAS_CAMERA_2_API = isLOrHigher();



    public static boolean isLOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                || "L".equals(Build.VERSION.CODENAME) || "LOLLIPOP".equals(Build.VERSION.CODENAME);
    }
}

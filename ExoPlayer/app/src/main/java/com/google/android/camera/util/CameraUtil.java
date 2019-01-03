package com.google.android.camera.util;

import android.view.Surface;
import android.view.WindowManager;

public class CameraUtil {

    // 显示的角度和设备的角度是不一致的。
    public static int getDisplayRotation() {
        WindowManager windowManager = AndroidServices.instance().provideWindowManager();
        int rotation = windowManager.getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface
                    .ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }
}

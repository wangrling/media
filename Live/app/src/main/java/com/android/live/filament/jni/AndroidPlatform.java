package com.android.live.filament.jni;

import android.opengl.EGL14;

public class AndroidPlatform extends Platform{

    private static final String LOG_TAG = "Filament";

    static {
        // workaround a deadlock during loading of /vendor/lib64/egl/libGLESv*
        // on Pixel 2. This loads the GL libraries before we load filament.
        EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
    }

    AndroidPlatform() {

    }


}

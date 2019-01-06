package com.android.live.filament.android;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.util.Log;
import android.view.Surface;

public class AndroidPlatform extends Platform{

    private static final String LOG_TAG = "Filament";

    static {
        // workaround a deadlock during loading of /vendor/lib64/egl/libGLESv*
        // on Pixel 2. This loads the GL libraries before we load filament.
        EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
    }

    AndroidPlatform() {

    }

    @Override
    void log(String message) {
        Log.d(LOG_TAG, message);
    }

    @Override
    void warn(String message) {
        Log.w(LOG_TAG, message);
    }

    @Override
    boolean validateStreamSource(Object object) {
        return object instanceof SurfaceTexture;
    }

    @Override
    boolean validateSurface(Object object) {
        return object instanceof Surface;
    }

    @Override
    boolean validateSharedContext(Object object) {
        return object instanceof EGLContext;
    }

    @Override
    long getSharedContextNativeHandle(Object sharedContext) {
        return ((EGLContext) sharedContext).getNativeHandle();
    }


}

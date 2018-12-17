package com.android.mm.grafika.gles;



public class OffscreenSurface extends EglSurfaceBase {


    public OffscreenSurface(EglCore eglCore, int width, int height) {
        super(eglCore);
        createOffscreenSurface(width, height);
    }

    /**
     * Releases any resources associated with the surface.
     */
    public void release() {
        releaseEglSurface();
    }
}

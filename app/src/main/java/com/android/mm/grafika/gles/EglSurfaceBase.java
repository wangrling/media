package com.android.mm.grafika.gles;

import android.opengl.EGL14;
import android.opengl.EGLSurface;


/**
 * Common base class for EGL surfaces.
 * <p>
 *     There can be multiple surfaces associated with a single context.
 * </p>
 */

public class EglSurfaceBase {
    protected static final String TAG = GlUtil.TAG;

    // EglCore object we're associated with. It may be associated with multiple surfaces.
    protected EglCore mEglCore;

    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    private int mWidth = -1;
    private int mHeight = -1;

    protected EglSurfaceBase(EglCore eglCore) {
        mEglCore = eglCore;
    }


    /**
     * Create an off-screen surface.
     * 包裹函数
     */
    public void createOffscreenSurface(int width, int height) {
        if (mEGLSurface != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        mEGLSurface = mEglCore.createOffscreenSurface(width, height);
        mWidth = width;
        mHeight = height;
    }

    /**
     * Makes our EGL context and surface current.
     */
    public void makeCurrent() {
        mEglCore.makeCurrent(mEGLSurface);
    }

    /**
     * Release the EGL surface.
     */
    public void releaseEglSurface() {
        mEglCore.releaseSurface(mEGLSurface);
        mEGLSurface = EGL14.EGL_NO_SURFACE;
        mWidth = mHeight = -1;
    }


}

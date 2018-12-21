package com.android.mm.grafika.gles;

import android.graphics.SurfaceTexture;
import android.view.Surface;

/**
 * Recordable EGL window surface.
 * <p>
 *     It's good practice to explicitly release() the surface, preferably from a "finally" block.
 * </p>
 */

public class WindowSurface extends EglSurfaceBase {

    private Surface mSurface;

    // 控制Surface的释放
    private boolean mReleaseSurface;


    /**
     * Associates an EGL surface with the native window surface.
     * <p>
     * Set releaseSurface to true if you want the Surface to be released when release() is
     * called.  This is convenient, but can interfere with framework classes that expect to
     * manage the Surface themselves (e.g. if you release a SurfaceView's Surface, the
     * surfaceDestroyed() callback won't fire).
     */
    public WindowSurface(EglCore eglCore, Surface surface, boolean releaseSurface) {
        super(eglCore);
        createWindowSurface(surface);
        mSurface = surface;
        mReleaseSurface = releaseSurface;
    }

    /**
     * Associates an EGL surface with the SurfaceTexture.
     */
    public WindowSurface(EglCore eglCore, SurfaceTexture surfaceTexture) {
        super(eglCore);
        createWindowSurface(surfaceTexture);
    }

    /**
     * Releases any resources associated with the EGL surface (and, if configured to do so,
     * with the Surface as well).
     * <p>
     * Does not require that the surface's EGL context be current.
     */
    public void release() {
        releaseEglSurface();
        if (mSurface != null) {
            if (mReleaseSurface) {
                mSurface.release();
            }
            mSurface = null;
        }
    }
}

package com.android.mm.grafika.gles;

import android.view.Surface;

/**
 * Recordable EGL window surface.
 * <p>
 *     It's good practice to explicitly release() the surface, preferably from a "finally" block.
 * </p>
 */

public class WindowSurface extends EglSurfaceBase {

    private Surface mSurface;
    private boolean mReleaseSurface;



    protected WindowSurface(EglCore eglCore) {
        super(eglCore);
    }
}

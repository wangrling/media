package com.android.mm.libgdx.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * A simple GLSurfaceView sub-class that demonstrates how to perform OpenGL ES 2.0 rendering into a GL Surface. Note the following
 * important details:
 *
 * The class must use a custom context factory to enable 3.0 rendering.
 * See ContextFactory class definition below.
 *
 * The class must use a custom EGLConfigChooser to be able to select an
 * EGLConfig that supports 3.0. This is done by providing a config specification to
 * eglChooseConfig() that has the attribute EGL10.ELG_RENDERABLE_TYPE containing the EGL_OPENGL_ES3_BIT
 * flag set. See ConfigChooser class definition below.
 *
 * The class must select the surface's format, then choose an EGLConfig that matches it exactly (with regards to
 * red/green/blue/alpha channels bit depths). Failure to do so would result in an EGL_BAD_MATCH error.
 *
 */

public class GLSurfaceView30 extends GLSurfaceView {

    static String TAG = "GLSurfaceView";

    private static final boolean DEBUG = true;

    final ResolutionStrategy resolutionStrategy;

    static int targetGLESVersion;

    public GLSurfaceView30(Context context, ResolutionStrategy  resolutionStrategy, int targetGLESVersion) {
        super(context);

        GLSurfaceView30.targetGLESVersion = targetGLESVersion;
        this.resolutionStrategy = resolutionStrategy;
    }

    public GLSurfaceView30(Context context, ResolutionStrategy resolutionStrategy) {
        this(context, resolutionStrategy, 3);
    }

    /**
     * By default, GLSurfaceView() create a RGB_565 opaque(模糊；不透明的) surface. If we want a
     * translucent (半透明的；微微透明的) surface's format here, using PixelFormat.TRANSLUCENT for GL
     * Surfaces is interpreted as any 32 bit-surface with alpha by SurfaceFlinger.
     *
     * @param translucent
     * @param depth
     * @param stencil
     */
    private void init(boolean translucent, int depth, int stencil) {

    }
}

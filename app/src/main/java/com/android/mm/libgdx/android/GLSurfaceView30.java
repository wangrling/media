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



    public GLSurfaceView30(Context context) {
        super(context);
    }

    public GLSurfaceView30(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

package com.android.mm.libgdx.android;


import android.opengl.GLSurfaceView;

import com.android.mm.libgdx.gdx.Graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class AndroidGraphics implements Graphics, GLSurfaceView.Renderer {

    public AndroidGraphics(AndroidApplicationBase application, AndroidApplicationConfiguration config,
                           ResolutionStrategy resolutionStrategy) {
        this(application, config, resolutionStrategy, true);
    }

    public AndroidGraphics(AndroidApplicationBase application, AndroidApplicationConfiguration config,
                           ResolutionStrategy resolutionStrategy, boolean focusableView) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}

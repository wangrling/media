package com.android.mm.libgdx.android;


import android.opengl.GLSurfaceView;
import android.view.View;

import com.android.mm.libgdx.gdx.Graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class AndroidGraphics implements Graphics, GLSurfaceView.Renderer {

    final View view;
    int width;
    int height;

    protected final AndroidApplicationConfiguration config;

    public AndroidGraphics(AndroidApplicationBase application, AndroidApplicationConfiguration config,
                           ResolutionStrategy resolutionStrategy) {
        this(application, config, resolutionStrategy, true);
    }

    public AndroidGraphics(AndroidApplicationBase application, AndroidApplicationConfiguration config,
                           ResolutionStrategy resolutionStrategy, boolean focusableView) {
        this.config = config;
        view = createGLSurfaceView(application, resolutionStrategy);

    }

    protected View createGLSurfaceView(AndroidApplicationBase application, final ResolutionStrategy resolutionStrategy) {
        GLSurfaceView30 view = new GLSurfaceView30(application.getContext(), resolutionStrategy);

        return view;
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

    public View getView() {
        return view;
    }
}

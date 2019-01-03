package com.google.android.libgdx.android;

import android.opengl.GLSurfaceView;
import android.view.View;

import com.google.android.libgdx.Graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AndroidGraphics implements Graphics, GLSurfaceView.Renderer {

    final View view;

    protected final AndroidApplicationConfiguration config;

    public AndroidGraphics(AndroidApplicationBase application, AndroidApplicationConfiguration config,
                           ResolutionStrategy resolutionStrategy) {
        this(application, config, resolutionStrategy, true);
    }

    public AndroidGraphics(AndroidApplicationBase application, AndroidApplicationConfiguration config,
                           ResolutionStrategy resolutionStrategy, boolean focusableView) {
        view = createGLSurfaceView(application, resolutionStrategy);

        this.config = config;
    }

    protected View createGLSurfaceView(AndroidApplicationBase application,
                                       final ResolutionStrategy resolutionStrategy) {
        // return new GLSurfaceView(application.getContext(), resolutionStrategy, config.useGL30 ? 3 : 2);
        return new GLSurfaceView(application.getContext());
    }

    public View getView() {
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
}

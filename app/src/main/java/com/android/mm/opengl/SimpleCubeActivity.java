package com.android.mm.opengl;

import android.content.Context;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import androidx.annotation.Nullable;

public class SimpleCubeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mGlSurfaceView = new SimpleCubeView(this);
        super.onCreate(savedInstanceState);
    }

    private class SimpleCubeView extends MyGLSurfaceView {
        public SimpleCubeView(Context context) {
            super(context);

            setRenderer(new Renderer() {
                @Override
                public void onSurfaceCreated(GL10 gl, EGLConfig config) {

                }

                @Override
                public void onSurfaceChanged(GL10 gl, int width, int height) {
                    init(width, height);
                }

                @Override
                public void onDrawFrame(GL10 gl) {
                    step();
                }
            });
        }
    }
    public static native void init(int width, int height);
    public static native void step();
}

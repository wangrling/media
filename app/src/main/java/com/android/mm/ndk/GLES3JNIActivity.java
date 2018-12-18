package com.android.mm.ndk;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import androidx.annotation.Nullable;

public class GLES3JNIActivity extends Activity {
    static {
        System.loadLibrary("androidndk");
    }

    GLES3JNIView mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = new GLES3JNIView(getApplication());
        setContentView(mView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    private class GLES3JNIView extends GLSurfaceView {
        public GLES3JNIView(Context context) {
            super(context);
            // Pick an EGLConfig with RGB8 color, 16-bit depth, no stencil,
            // supporting OpenGL ES 2.0 or later backwards-compatible versions.
            setEGLConfigChooser(8, 8, 8, 0, 16, 0);
            setEGLContextClientVersion(3);
            setRenderer(new Renderer() {
                @Override
                public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                    init();
                }

                @Override
                public void onSurfaceChanged(GL10 gl, int width, int height) {
                    resize(width, height);
                }

                @Override
                public void onDrawFrame(GL10 gl) {
                    step();
                }
            });
        }
    }

    public static native void init();
    public static native void resize(int width, int height);
    public static native void step();
}

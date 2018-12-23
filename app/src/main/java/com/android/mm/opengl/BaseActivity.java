package com.android.mm.opengl;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class BaseActivity extends Activity {

    protected final static String TAG = "OpenGL";

    protected MyGLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 子类必须先初始化MyGLSurfaceView
        setContentView(mGlSurfaceView);
    }

    protected class MyGLSurfaceView extends GLSurfaceView {

        public MyGLSurfaceView(Context context) {
            super(context);
        }
    }
}

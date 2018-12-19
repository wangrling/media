package com.android.mm.ndk;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CodecGLSurfaceView extends GLSurfaceView {
    private static final String TAG = NativeCodecActivity.TAG;

    GLRenderer mRenderer;

    public CodecGLSurfaceView(Context context) {
        this(context, null);
    }

    public CodecGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(3);
        mRenderer = new GLRenderer();
        setRenderer(mRenderer);
        Log.i(TAG, "set renderer");
    }

    public SurfaceTexture getSurfaceTexture() {
        return mRenderer.getSurfaceTexture();
    }

    class GLRenderer implements Renderer, SurfaceTexture.OnFrameAvailableListener {

        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {

        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mSurface = new SurfaceTexture(mTextureID);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }
        private int mTextureID;
        private SurfaceTexture mSurface;

        public SurfaceTexture getSurfaceTexture() {
            return mSurface;
        }
    }
}

package com.android.mm.grafika;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;

import com.android.mm.R;

import androidx.annotation.Nullable;

public class TextureViewGLActivity extends Activity {
    private static final String TAG = GrafikaActivity.TAG;

    private TextureView mTextureView;
    private Renderer mRenderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start up the Renderer thread.  It'll sleep until the TextureView is ready.
        mRenderer = new Renderer();
        mRenderer.start();

        setContentView(R.layout.activity_texture_view_gl);
        mTextureView = (TextureView) findViewById(R.id.glTextureView);
        mTextureView.setSurfaceTextureListener(mRenderer);
    }

    private class Renderer extends Thread implements TextureView.SurfaceTextureListener {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    }
}

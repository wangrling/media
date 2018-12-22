package com.android.mm.grafika;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.opengl.GLES30;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.android.mm.R;
import com.android.mm.grafika.gles.EglCore;
import com.android.mm.grafika.gles.WindowSurface;

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

        mTextureView = new TextureView(this);
        setContentView(mTextureView);
        mTextureView.setSurfaceTextureListener(mRenderer);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        mRenderer.halt();
        super.onPause();
    }

    /**
     * Handles GL rendering and SurfaceTexture callbacks.
     * <p>
     *     We don't create a Looper, so the SurfaceTexture-by-way-of-TextureView callbacks
     *     happen on the UI thread.
     * </p>
     */
    private class Renderer extends Thread implements TextureView.SurfaceTextureListener {
        private Object mLock = new Object();     // guards mSurfaceTexture, mDone
        private SurfaceTexture mSurfaceTexture;
        private EglCore mEglCore;
        private boolean mDone;

        public Renderer() {
            super("TextureViewGL Renderer");
        }

        @Override
        public void run() {
            while (true) {
                SurfaceTexture surfaceTexture = null;
                // Latch the SurfaceTexture when it becomes available.  We have to wait for
                // the TextureView to create it.
                synchronized (mLock) {
                    while (!mDone && (surfaceTexture = mSurfaceTexture) == null) {
                        try {
                            mLock.wait();
                        } catch (InterruptedException ie) {
                            throw new RuntimeException(ie);     // not expected
                        }
                    }
                    if (mDone)
                        break;
                }
                Log.d(TAG, "Got surfaceTexture=" + surfaceTexture);

                // Create an EGL surface for our new SurfaceTexture. We're not on the same
                // thread as the SurfaceTexture, which is a concern for the *consumer*, which
                // wants to call updateTexImage().  Because we're the *producer*, i.e. the
                // one generating the frames, we don't need to worry about being on the same
                // thread.
                mEglCore = new EglCore(null, EglCore.FLAG_TRY_GLES3);
                WindowSurface windowSurface = new WindowSurface(mEglCore, mSurfaceTexture);
                windowSurface.makeCurrent();

                // Render frame until we're told to stop or the SurfaceTexture is destroyed.
                doAnimation(windowSurface);

                windowSurface.release();
                mEglCore.release();

            }
            Log.d(TAG, "Renderer thread exiting.");
        }

        /**
         * Draws updates as fast as the system will allow.
         * <p>
         * In 4.4, with the synchronous buffer queue queue, the frame rate will be limited.
         * In previous (and future) releases, with the async queue, many of the frames we
         * render may be dropped.
         * <p>
         * The correct thing to do here is use Choreographer to schedule frame updates off
         * of vsync, but that's not nearly as much fun.
         */
        private void doAnimation(WindowSurface eglSurface) {
            final int BLOCK_WIDTH = 80;
            final int BLOCK_SPEED = 2;
            float clearColor = 0.5f;
            int xpos = -BLOCK_WIDTH / 2;
            int xdir = BLOCK_SPEED;
            int width = eglSurface.getWidth();
            int height = eglSurface.getHeight();

            Log.d(TAG, "Animating " + width + "x" + height + " EGL surface");

            while (true) {
                // Check to see if the TextureView's SurfaceTexture is still valid.
                synchronized (mLock) {
                    SurfaceTexture surfaceTexture = mSurfaceTexture;
                    if (surfaceTexture == null) {
                        Log.d(TAG, "doAnimation exiting");
                        return;
                    }

                    // Still alive, render a frame.
                    // GL进行绘制工作。
                    GLES30.glClearColor(clearColor, clearColor/2, clearColor/4, 1.0f);
                    GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

                    GLES30.glEnable(GLES30.GL_SCISSOR_TEST);
                    // glScissor defines a rectangle, called the scissor box,
                    //            in window coordinates.
                    //            The first two arguments,
                    //            x and y,
                    //            specify the lower left corner of the box.
                    //            width and height specify the width and height of the box.
                    GLES30.glScissor(xpos, height / 4, BLOCK_WIDTH, height / 2);
                    GLES30.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
                    GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
                    GLES30.glDisable(GLES30.GL_SCISSOR_TEST);

                    // renderFrame();

                    // Publish the frame. If we overrun the consumer, frames will be dropped,
                    // so on a sufficiently fast device the animation will run at faster than
                    // the display refresh rate.
                    //
                    // If the SurfaceTexture has been destroyed, this will throw an exception.
                    // 显示到画布上
                    eglSurface.swapBuffers();

                    // Advance state

                    xpos += xdir;
                    if (xpos <= -BLOCK_WIDTH / 2 || xpos >= width - BLOCK_WIDTH / 2) {
                        Log.d(TAG, "change direction");
                        xdir = -xdir;
                    }
                }
            }
        }

        /**
         * Tells the thread to stop running.
         */
        public void halt() {
            synchronized (mLock) {
                mDone = true;
                mLock.notify();
            }
        }

        @Override   // will be called on UI thread
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureAvailable(" + width + "x" + height + ")");
            synchronized (mLock) {
                mSurfaceTexture = surface;
                mLock.notify();
            }
        }

        @Override   // will be called on UI thread
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureSizeChanged(" + width + "x" + height + ")");
            // TODO: ?
        }

        @Override   // will be called on UI thread
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.d(TAG, "onSurfaceTextureDestroyed");
            // We set the SurfaceTexture reference to null to tell the Renderer thread that
            // it needs to stop.  The renderer might be in the middle of drawing, so we want
            // to return false here so that the caller doesn't try to release the ST out
            // from under us.
            //
            // In theory.
            //
            // In 4.4, the buffer queue was changed to be synchronous, which means we block
            // in dequeueBuffer().  If the renderer has been running flat out and is currently
            // sleeping in eglSwapBuffers(), it's going to be stuck there until somebody
            // tears down the SurfaceTexture.  So we need to tear it down here to ensure
            // that the renderer thread will break.  If we don't, the thread sticks there
            // forever.
            //
            // The only down side to releasing it here is we'll get some complaints in logcat
            // when eglSwapBuffers() fails.
            synchronized (mLock) {
                mSurfaceTexture = null;
            }
            return true;
        }

        @Override   // will be called on UI thread
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            //Log.d(TAG, "onSurfaceTextureUpdated");
        }
    }
}

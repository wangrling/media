package com.android.mm.grafika;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.TextView;

import com.android.mm.R;

import androidx.annotation.Nullable;

// 对于Canvas绘制还不是很熟悉。
// 需要补充多线程知识！
// Exercises software rendering to a `TextureView` with a `Canvas`.

public class TextureViewCanvasActivity extends Activity {

    private static final String TAG = GrafikaActivity.TAG;

    private TextureView mTextureView;
    private Renderer mRenderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Start up the Renderer thread. It'll sleep until the TextureView is ready.
        // 新开一个线程进行绘制工作。
        mRenderer = new Renderer();
        mRenderer.start();

        setContentView(R.layout.activity_texture_view_canvas);
        mTextureView = (TextureView) findViewById(R.id.canvasTextureView);
        mTextureView.setSurfaceTextureListener(mRenderer);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mRenderer.halt();
    }

    /**
     * Handles Canvas rendering and SurfaceTexture callbacks.
     * <p>
     * We don't create a Looper, so the SurfaceTexture-by-way-of-TextureView callbacks
     * happen on the UI thread.
     * </p>
     */
    private static class Renderer extends Thread implements TextureView.SurfaceTextureListener {
        // guards mSurfaceTexture, mDone
        private Object mLock = new Object();

        // 从消费者端获取进行绘制图形工作。
        private SurfaceTexture mSurfaceTexture;
        private boolean mDone;

        // from SurfaceTexture
        private int mWidth;
        private int mHeight;

        public Renderer() {
            super("TextureViewCanvas Renderer");
        }

        @Override
        public void run() {
            while (true) {
                SurfaceTexture surfaceTexture = null;

                // Latch(锁) the SurfaceTexture when it becomes available. We have to wait for
                // the TextureView to create it.
                synchronized (mLock) {
                    while (!mDone && (surfaceTexture = mSurfaceTexture) == null) {

                        try {
                            // 等待唤醒。
                            mLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (mDone) {
                        break;
                    }
                }
                Log.d(TAG, "Got surfaceTexture=" + surfaceTexture);

                // Render frames until we're told to stop or the SurfaceTexture is destroyed.
                doAnimation();
            }
            Log.d(TAG, "Renderer thread exiting");
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
        private void doAnimation() {
            final int BLOCK_WIDTH = 80;
            final int BLOCK_SPEED = 2;
            int clearColor = 0;
            int xpos = -BLOCK_WIDTH / 2;
            int xdir = BLOCK_SPEED;

            // Create a Surface for the SurfaceTexture.
            // 充当生产者，从消费者那里获取SurfaceTexture，通过Surface转化为生产者的画布。
            Surface surface = null;
            synchronized (mLock) {
                SurfaceTexture surfaceTexture = mSurfaceTexture;
                if (surfaceTexture == null) {
                    Log.d(TAG, "ST null on entry");
                    return;
                }
                surface = new Surface(surfaceTexture);
            }

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);

            boolean partial = false;
            while (true) {
                Rect dirty = null;
                if (partial) {
                    // Set a dirty rect to confirm that the feature is working.  It's
                    // possible for lockCanvas() to expand the dirty rect if for some
                    // reason the system doesn't have access to the previous buffer.
                    dirty = new Rect(0, mHeight * 3 / 8, mWidth, mHeight * 5 / 8);
                }
                // A rectangle that represents the dirty region that the caller wants to redraw.
                Canvas canvas;
                try {
                    canvas = surface.lockCanvas(dirty);
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "dequeueBuffer: BufferQueue has been abandoned");
                    break;
                }
                if (canvas == null) {
                    Log.d(TAG, "lockCanvas() failed");
                    break;
                }
                try {
                    // just curious
                    if (canvas.getWidth() != mWidth || canvas.getHeight() != mHeight) {
                        Log.d(TAG, "WEIRD: width/height mismatch");
                    }

                    // Draw the entire window.  If the dirty rect is set we should actually
                    // just be drawing into the area covered by it -- the system lets us draw
                    // whatever we want, then overwrites the areas outside the dirty rect with
                    // the previous contents.  So we've got a lot of overdraw here.
                    canvas.drawRGB(clearColor, clearColor, clearColor);
                    canvas.drawRect(xpos, mHeight / 4, xpos + BLOCK_WIDTH, mHeight * 3 / 4, paint);
                } finally {
                    // Publish the frame.  If we overrun the consumer, frames will be dropped,
                    // so on a sufficiently fast device the animation will run at faster than
                    // the display refresh rate.
                    //
                    // If the SurfaceTexture has been destroyed, this will throw an exception.
                    try {
                        surface.unlockCanvasAndPost(canvas);
                    } catch (IllegalArgumentException iae) {
                        Log.d(TAG, "unlockCanvasAndPost failed: " + iae.getMessage());
                        break;
                    }
                }
                // Advance state
                clearColor += 4;
                if (clearColor > 255) {
                    clearColor = 0;
                    partial = !partial;
                }
                xpos += xdir;

                if (xpos <= -BLOCK_WIDTH / 2 || xpos >= mWidth - BLOCK_WIDTH / 2) {
                    Log.d(TAG, "change direction");
                    xdir = -xdir;
                }
            }
            surface.release();
        }

        /**
         * Tells the thread to stop running.
         */
        public void halt() {
            synchronized (mLock) {
                mDone = true;
                // 通知wait程序。
                mLock.notify();
            }
        }

        @Override   // will be called on UI thread.
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureAvailable(" + width + "x" + height + ")");
            mWidth = width;
            mHeight = height;
            synchronized (mLock) {
                mSurfaceTexture = surface;
                mLock.notify();
            }
        }

        @Override   // will be called on UI thread
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureSizeChanged(" + width + "x" + height + ")");
            mWidth = width;
            mHeight = height;
        }

        @Override   // will be called on UI thread
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.d(TAG, "onSurfaceTextureDestroyed");

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

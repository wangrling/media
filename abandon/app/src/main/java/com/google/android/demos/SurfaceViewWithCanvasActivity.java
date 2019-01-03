package com.google.android.demos;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.R;

import androidx.annotation.Nullable;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;

public class SurfaceViewWithCanvasActivity extends Activity {

    private GameView mGameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock orientation into landscape.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Create a GameView and bind it to this activity.
        // You don't need a ViewGroup to fill the screen, because the system
        // has a FrameLayout to which this will be added.
        mGameView = new GameView(this);
        // Android 4.1 and higher simple way to request fullscreen.
        mGameView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(mGameView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mGameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGameView.resume();
    }

    private class GameView extends SurfaceView implements Runnable {

        private boolean mRunning;
        private Thread mGameThread = null;
        private Path mPath;

        private Context mContext;

        private FlashlightCone mFlashlightCone;

        private Paint mPaint;
        private Bitmap mBitmap;
        private RectF mWinnerRect;
        private int mBitmapX;
        private int mBitmapY;
        private int mViewWidth;
        private int mViewHeight;
        private SurfaceHolder mSurfaceHolder;

        public GameView(Context context) {
            this(context, null);

            mContext = context;
            mSurfaceHolder = getHolder();
            mPaint = new Paint();
            mPaint.setColor(Color.DKGRAY);
            mPath = new Path();
        }

        public GameView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mViewWidth = w;
            mViewHeight = h;

            mFlashlightCone = new FlashlightCone(mViewWidth, mViewHeight);

            // Set font size proportional to view size.
            mPaint.setTextSize(mViewHeight / 5);

            mBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.ic_launcher_foreground);

            setUpBitmap();
        }

        /**
         * Calculation a randomized location for the bitmap and the winning bounding rectangle.
         */
        private void setUpBitmap() {
            mBitmapX = (int) Math.floor(
                    Math.random() * (mViewWidth - mBitmap.getWidth()));
            mBitmapY = (int) Math.floor(
                    Math.random() * (mViewHeight - mBitmap.getHeight()));
            mWinnerRect = new RectF(mBitmapX, mBitmapY,
                    mBitmapX + mBitmap.getWidth(),
                    mBitmapY + mBitmap.getHeight());
        }

        /**
         * Runs in a separate thread.
         * All drawing happens here.
         */
        @Override
        public void run() {
            Canvas canvas;
            while (mRunning) {
                // If we can obtain a valid drawing surface...
                if (mSurfaceHolder.getSurface().isValid()) {
                    // Helper variables for performance.
                    int x = mFlashlightCone.getX();
                    int y = mFlashlightCone.getY();
                    int radius = mFlashlightCone.getRadius();

                    // Lock the canvas. Note that in a more complex app, with
                    // more threads, you need to put this into a try/catch block
                    // to make sure only one thread is drawing to the surface.
                    // Starting with O, you can request a hardware surface with
                    //    lockHardwareCanvas().
                    // See https://developer.android.com/reference/android/view/
                    //    SurfaceHolder.html#lockHardwareCanvas()
                    canvas = mSurfaceHolder.lockCanvas();

                    // Fill the canvas with white and draw th bitmap.
                    canvas.save();
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY, mPaint);

                    // aDD clipping region and fill rest of the canvas with black.
                    mPath.addCircle(x, y, radius, Path.Direction.CCW);
                    canvas.clipPath(mPath, Region.Op.DIFFERENCE);
                    canvas.drawColor(Color.BLACK);

                    // If the x, y coordinates of the user touch are within a
                    //  bounding rectangle, display the winning message.
                    if (x > mWinnerRect.left && x < mWinnerRect.right
                            && y > mWinnerRect.top && y < mWinnerRect.bottom) {
                        canvas.drawColor(Color.WHITE);
                        canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY, mPaint);
                        canvas.drawText(
                                "WIN!", mViewWidth / 3, mViewHeight / 2, mPaint);
                    }

                    // Clear the path data structure.
                    mPath.rewind();
                    // Restore the previously saved (default) clip and matrix state.
                    canvas.restore();
                    // Release the lock on the canvas and show the surface's
                    // contents on the screen.
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            // Invalidate() is inside the case statements because there are
            // many other motion events, and we don't want to invalidate
            // the view for those.
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setUpBitmap();
                    // Set coordinates of flashlight cone.
                    updateFrame((int) x, (int) y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // Updated coordinates for flashlight cone.
                    updateFrame((int) x, (int) y);
                    invalidate();
                    break;
                default:
                    // Do nothing.
            }
            return true;
        }

        /**
         * Updates the game data.
         * Sets new coordinates for the flashlight cone.
         *
         * @param newX New x position of touch event.
         * @param newY New y position of touch event.
         */
        private void updateFrame(int newX, int newY) {
            mFlashlightCone.update(newX, newY);
        }

        /**
         * Called by MainActivity.onPause() to stop the thread.
         */
        public void pause() {
            mRunning = false;
            try {
                // Stop the thread == rejoin the main thread.
                mGameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Called by MainActivity.onResume() to start a thread.
         */
        public void resume() {
            mRunning = true;
            mGameThread = new Thread(this);
            mGameThread.start();
        }
    }

    private class FlashlightCone {
        private int mX;
        private int mY;

        private int mRadius;

        public FlashlightCone(int viewWidth, int viewHeight) {
            mX = viewWidth / 2;
            mY = viewHeight / 2;

            // Adjust the radius for the narrowest view dimension.
            mRadius = ((viewWidth < viewHeight) ? mX / 3 : mY / 3);
        }

        /**
         * Update the coordinates of the flashlight cone.
         *
         * @param newX Changed value for x coordinate.
         * @param newY Changed value for y coordinate.
         */
        public void update(int newX, int newY) {
           mX = newX;
           mY = newY;
        }

        public int getX() {
            return mX;
        }

        public int getY() {
            return mY;
        }

        public int getRadius() {
            return mRadius;
        }
    }
}

package com.android.mm.mediacts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.mm.R;

import androidx.annotation.Nullable;

public class SurfaceViewCtsActivity extends Activity {
    private MockSurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // New a MockSurfaceView
        mSurfaceView = new MockSurfaceView(getApplicationContext());

        setContentView(mSurfaceView);
    }

    public MockSurfaceView getSurfaceView() {
        return mSurfaceView;
    }

    public class MockSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private static final int FIX_WIDTH = 240;
        private static final int FIX_HEIGHT = 240;
        private static final int BITMAP_WIDTH = 100;
        private static final int BITMAP_HEIGHT = 100;
        private static final int RECT_LEFT = 20;
        private static final int RECT_TOP = 100;
        private static final int RECT_RIGHT = 200;
        private static final int RECT_BOTTOM = 200;

        private Canvas mCanvas;

        private SurfaceHolder mHolder;

        private boolean mIsDraw;
        private boolean mIsAttachedToWindow;
        private boolean mIsDetachedFromWindow;
        private boolean mIsOnMeasure;
        private boolean mIsOnScrollChanged;
        private boolean mIsOnSizeChanged;
        private boolean mIsOnWindowVisibilityChanged;
        private boolean mIsDispatchDraw;
        private boolean mIsSurfaceChanged;
        private boolean mSurfaceCreatedCalled;

        private int mWidthInOnMeasure;
        private int mHeightInOnMeasure;
        private int mOldLOnScrollChanged;
        private int mOldTOnScrollChanged;
        private int mOldWOnSizeChanged;
        private int mOldHOnSizeChanged;
        private int mVisibilityOnWindowVisibilityChanged;

        Surface mSurface;

        public MockSurfaceView(Context context) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setFixedSize(FIX_WIDTH, FIX_HEIGHT);
        }

        @Override
        public void onWindowVisibilityChanged(int visibility) {
            super.onWindowVisibilityChanged(visibility);
            mVisibilityOnWindowVisibilityChanged = visibility;
            mIsOnWindowVisibilityChanged = true;
        }

        public int getVInOnWindowVisibilityChanged() {
            return mVisibilityOnWindowVisibilityChanged;
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            mIsDraw = true;
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            mIsAttachedToWindow = true;
        }

        @Override
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            mIsDetachedFromWindow = true;
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mWidthInOnMeasure = getDefaultSize(FIX_WIDTH, widthMeasureSpec);
            mHeightInOnMeasure = getDefaultSize(FIX_HEIGHT, heightMeasureSpec);
            mIsOnMeasure = true;
        }

        public int getWidthInOnMeasure() {
            return mWidthInOnMeasure;
        }

        public int getHeightInOnMeasure() {
            return mHeightInOnMeasure;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            mOldLOnScrollChanged = oldl;
            mOldTOnScrollChanged = oldt;
            mIsOnScrollChanged = true;
        }

        public int getOldHorizontal() {
            return mOldLOnScrollChanged;
        }

        public int getOldVertical() {
            return mOldTOnScrollChanged;
        }

        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mOldWOnSizeChanged = oldw;
            mOldHOnSizeChanged = oldh;
            mIsOnSizeChanged = true;
        }

        public int getOldWidth() {
            return mOldWOnSizeChanged;
        }

        public int getOldHeight() {
            return mOldHOnSizeChanged;
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            mIsDispatchDraw = true;
        }

        public void setFormat(int format) {
            getHolder().setFormat(format);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceCreatedCalled = true;
            mSurface = holder.getSurface();
            // Use mock canvas listening to the drawColor() calling.
            mCanvas = new Canvas(Bitmap.createBitmap(BITMAP_WIDTH,
                    BITMAP_HEIGHT,
                    Bitmap.Config.ARGB_8888));

            draw(mCanvas);
            // Lock the surface, this returns a Canvas that can be used to render into.
            Canvas canvas = mHolder.lockCanvas();
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLUE);
            canvas.drawRect(RECT_LEFT, RECT_TOP, RECT_RIGHT, RECT_BOTTOM, paint);

            // And finally unlock and post the surface.
            mHolder.unlockCanvasAndPost(canvas);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            mIsSurfaceChanged = true;
        }

        boolean isSurfaceCreatedCalled() {
            return mSurfaceCreatedCalled;
        }

        public boolean isDraw() {
            return mIsDraw;
        }

        public boolean isOnAttachedToWindow() {
            return mIsAttachedToWindow;
        }

        public boolean isDetachedFromWindow() {
            return mIsDetachedFromWindow;
        }

        public boolean isOnMeasureCalled() {
            return mIsOnMeasure;
        }

        public boolean isOnScrollChanged() {
            return mIsOnScrollChanged;
        }

        public boolean isOnSizeChangedCalled() {
            return mIsOnSizeChanged;
        }

        public void resetOnSizeChangedFlag(boolean b) {
            mIsOnSizeChanged = b;
        }

        public boolean isOnWindowVisibilityChanged() {
            return mIsOnWindowVisibilityChanged;
        }

        public boolean isDispatchDraw() {
            return mIsDispatchDraw;
        }

        public boolean isSurfaceChanged() {
            return mIsSurfaceChanged;
        }
    }
}

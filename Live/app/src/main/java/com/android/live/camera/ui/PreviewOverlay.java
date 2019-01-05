package com.android.live.camera.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.android.live.camera.app.CameraAppUI;
import com.android.live.camera.debug.Log;

import androidx.annotation.Nullable;

/**
 * {@link #PreviewOverlay} is a view that sits on top the preview. It serves to disambiguate
 * touch events, as {@link CameraAppUI} has a touch listener set on it. As a result, touch events
 * that happen on preview will first go through the touch listener in AppUI, which filters out swipes
 * that sould be handled on the app level. The rest of the touch events will be handled here in
 * {@link #onTouchEvent(MotionEvent)}
 *
 * <p>
 * For scale gestures, if an {@link OnZoomChangedListener} is set, the listener will receive callbacks
 * as the scaling happens, and a zoom UI will be hosted in this class.
 */

public class PreviewOverlay extends View {

    private final Log.Tag TAG = new Log.Tag("PreviewOverlay");

    public static final float ZOOM_MIN_RATIO = 1.0f;
    private static final int NUM_ZOOM_LEVELS = 7;
    private static final float MIN_ZOOM = 1f;



    // 处理手势事件，不包括滑动。
    private GestureDetector mGestureDetector = null;
    // 触摸事件
    private OnTouchListener mTouchListener = null;

    private final ZoomGestureDetector mScaleDetector;
    private final ZoomProcessor mZoomProcessor = new ZoomProcessor();
    // 缩放事件
    private OnZoomChangedListener mZoomListener = null;

    private OnPreviewTouchedListener mOnPreviewTouchedListener;

    // 缩放手势的View
    private boolean mVisible = false;

    private double mFingerAngle;        // in radians.

    public interface OnZoomChangedListener {
        /**
         * This gets called when a zoom is detected and started.
         */
        void onZoomStart();

        /**
         * This gets called when zoom gesture has ended.
         */
        void onZoomEnd();

        /**
         * This gets called when scale gesture changes the zoom value.
         *
         * @param ratio zoom ratio, [1.0f,maximum]
         */
        void onZoomValueChanged(float ratio);  // only for immediate zoom
    }

    public interface OnPreviewTouchedListener {
        /**
         * This gets called on any preview touch event.
         */
        public void onPreviewTouched(MotionEvent ev);
    }

    public PreviewOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mScaleDetector = new ZoomGestureDetector();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent " + event.getActionMasked());

        // Pass the touch events to scale detector and gesture detector.
        // 首先检测手势，然后检测触摸，其次是缩放，最后教给预览处理。
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }
        if (mTouchListener != null) {
            mTouchListener.onTouch(this, event);
        }

        mScaleDetector.onTouchEvent(event);

        if (mOnPreviewTouchedListener != null) {
            mOnPreviewTouchedListener.onPreviewTouched(event);
        }

        return true;
    }

    // 注册监听手势事件
    /**
     * Each module can pass in their own gesture listener through App UI. When a gesture
     * is detected, the {@link GestureDetector.OnGestureListener} will be notified of
     * the gesture.
     *
     * @param gestureListener a listener from a module that defines how to handle gestures
     */
    public void setGestureListener(GestureDetector.OnGestureListener gestureListener) {
        if (gestureListener != null) {
            mGestureDetector = new GestureDetector(getContext(), gestureListener);
        }
    }

    public void setTouchListener(OnTouchListener touchListener) {
        mTouchListener = touchListener;
    }

    private class ZoomGestureDetector extends ScaleGestureDetector {
        private float mDeltaX;
        private float mDeltaY;

        public ZoomGestureDetector() {
            super(getContext(), mZoomProcessor);
        }

        /**
         * 在{@link PreviewOverlay#onTouchEvent(MotionEvent)}调用
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (mZoomListener == null)
                return false;
            else {
                boolean handle = super.onTouchEvent(event);
                // 两个手指
                if (event.getPointerCount() > 1) {
                    mDeltaX = event.getX(1) - event.getX(0);
                    mDeltaY = event.getY(1) - event.getY(0);
                }
                return handle;
            }
        }

        /**
         * Calculate the angle between two fingers. Range: [-pi, pi]
         */
        public float getAngle() {
            // 正切值表示两者的位移。
            return (float) Math.atan2(-mDeltaY, mDeltaX);
        }
    }

    /**
     * This class processes recognized scale gestures, notifies {@link OnZoomChangedListener} of
     * any change in scale, and draw the zoom UI on screen.
     */
    private class ZoomProcessor implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.d(TAG, "onScaleBegin");
            mZoomProcessor.showZoomUI();
            return false;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }

        public void showZoomUI() {
            if (mZoomListener == null) {
                return ;
            }

            mVisible = true;
            mFingerAngle = mScaleDetector.getAngle();
            invalidate();
        }
    }
}

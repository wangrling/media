package com.google.android.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.camera.debug.Log;
import com.google.android.camera.ui.TouchCoordinate;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 快门按钮，设置消息通知，同时进行绘制。
 */

public class ShutterButton extends AppCompatImageView {

    private static final Log.Tag TAG = new Log.Tag("ShutterButton");
    private static final float ALPHA_HEN_ENABLED = 1f;
    public static final float ALPHA_WHEN_DISABLE = 0.2f;
    private boolean mTouchEnabled = true;
    private TouchCoordinate mTouchCoordinate;
    private final GestureDetector mGestureDetector;

    private List<OnShutterButtonListener> mListeners = new ArrayList<>();

    private boolean mOldPressed;

    public interface OnShutterButtonListener {
        /**
         * Called when a ShutterButton has been pressed.
         *
         * @param pressed   The ShutterButton that was pressed.
         */
        void onShutterButtonFocus(boolean pressed);

        void onShutterCoordinate(TouchCoordinate coord);

        void onShutterButtonClick();

        /**
         * Called when shutter button is held down from a long press.
         */
        void onShutterButtonLongPressed();
    }

    /**
     * A gesture listener to detect long press.
     */
    private class LongPressGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            for (OnShutterButtonListener listener : mListeners) {

            }
        }
    }

    public ShutterButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        mGestureDetector = new GestureDetector(context, new LongPressGestureListener());
        mGestureDetector.setIsLongpressEnabled(true);
    }

    public void addOnShutterButtonListener(OnShutterButtonListener listener) {
        if(!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeOnShutterButtonListener(OnShutterButtonListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent m) {
        if (mTouchEnabled) {
            // Don't send ACTION_MOVE messages to gesture detector unless event motion is out
            // of shutter button view. A small motion resets the ong tap status. A long tap should
            // regardless of any small motions. If motion moves out of shutter button view, the
            // gesture detector needs to be notified to reset the long tap status.
            // ||的优先级比!=的高
            if (m.getActionMasked() != MotionEvent.ACTION_MOVE ||
                    m.getX() < 0 || m.getY() < 0 ||
                    m.getX() >= getWidth() || m.getY() >= getHeight()) {
                mGestureDetector.onTouchEvent(m);
            }
            if (m.getActionMasked() == MotionEvent.ACTION_UP) {
                // UP事件交个performClick函数处理。
                mTouchCoordinate = new TouchCoordinate(m.getX(), m.getY(), this.getMaxWidth(),
                        getMeasuredHeight());
            }
            return super.dispatchTouchEvent(m);
        } else {
            // 拦截所有事件
            return false;
        }
    }

    public void enableTouch(boolean enable) {
        mTouchEnabled = enable;
    }

    /**
     * Hook into the drawable state changing to get changes to isPressed -- the
     * onPressed listener doesn't always get called when the pressed state changes.
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final boolean pressed = isPressed();
        if (pressed != mOldPressed) {

            if (!pressed) {
                /**
                 * When pressing the physical camera button the sequence of events is:
                 *      focus pressed, optional camera pressed, focus released.
                 * We want to emulate this sequence of events with the shutter button.
                 * When clicking using a trackball button, the view system changes the
                 * drawable state before posting click notification, so the sequence of
                 * events is:
                 *      pressed(true), optional click, pressed(false)
                 * When clicking using touch events, the view system changes the drawable
                 * state after posting click notification, so the sequence of events is:
                 *      pressed(true), pressed(false), optional click
                 * Since we're emulating the physical camera button ,we want to have the same
                 * order of events. So we want the optional click callback to be delivered
                 * before the pressed(false) callback.
                 *
                 * To do this, we delay the posting of the pressed(false) event slightly by
                 * pushing it ont the event queue. This moves it after the optional click
                 * notification, so our client always see events in this sequence:
                 *      pressed(true), optional click, pressed(false).
                 * 放到消息队列中执行延迟释放的时间。
                 */
                post(new Runnable() {
                    @Override
                    public void run() {
                        callShutterButtonFocus(pressed);
                    }
                });

            } else {
                callShutterButtonFocus(pressed);
            }

            mOldPressed = pressed;
        }
    }

    private void  callShutterButtonFocus(boolean pressed) {
        for (OnShutterButtonListener listener : mListeners) {
            listener.onShutterButtonFocus(pressed);
        }
    }

    @Override
    public boolean performClick() {
        boolean result = super.performClick();
        if (getVisibility() == View.VISIBLE) {
            for (OnShutterButtonListener listener : mListeners) {
                listener.onShutterCoordinate(mTouchCoordinate);
                mTouchCoordinate = null;
                listener.onShutterButtonClick();
            }
        }
        return result;
    }
}

package com.android.live.camera.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.android.live.R;
import com.android.live.camera.debug.Log;
import com.android.live.camera.widget.SettingsCling;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ModeListView class displays all camera modes and settings in the form
 * of a list. A swipe to the right will bring up this list. Then tapping on
 * any of the items in the list will take the user to that corresponding mode
 * with an animation. To dismiss this list, simply swipe left or select a mode.
 */

public class ModeListView extends FrameLayout {

    private static final Log.Tag TAG = new Log.Tag("ModeListView");

    /**
     * A factor to change the UI responsiveness on a scroll.
     * e.g. A scroll factor of 0.5 means UI will move half as fast as the finger.
     */
    private static final float SCROLL_FACTOR = 0.5f;


    private final int mSettingsButtonMargin;

    private SettingsCling mSettingCling = null;

    private int mListBackgroundColor;




    private final CurrentStateManager mCurrentStateManager = new CurrentStateManager();



    private final GestureDetector.OnGestureListener mOnGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    mCurrentStateManager.getCurrentState().onScroll(e1, e2, distanceX, distanceY);
                    mLastScrollTime = System.currentTimeMillis();
                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    mCurrentStateManager.getCurrentState().onSingleTapUp(e);
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    // Cache velocity in the unit pixel/ms.
                    mVelocityX = velocityX / 1000f * SCROLL_FACTOR;
                    mCurrentStateManager.getCurrentState().onFling(e1, e2, velocityX, velocityY);
                    return true;
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    mVelocityX = 0;
                    mCurrentStateManager.getCurrentState().onDown(e);
                    return true;
                }
            };

    private View mChildViewTouched = null;

    private final GestureDetector mGestureDetector;
    private long mLastScrollTime;
    private float mVelocityX;


    public ModeListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mGestureDetector = new GestureDetector(context, mOnGestureListener);
        mListBackgroundColor = getResources().getColor(R.color.mode_list_background, null);
        mSettingsButtonMargin = getResources().getDimensionPixelSize(
                R.dimen.mode_list_settings_icon_margin);
    }


    int count = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onTouchEvent");
        // Reset touch forward recipient
        if (MotionEvent.ACTION_DOWN == ev.getActionMasked()) {
            mChildViewTouched = null;
        }
        // True if the child does not want the parent to intercept touch events
        getParent().requestDisallowInterceptTouchEvent(true);
        super.onTouchEvent(ev);
        if (count % 2 == 0) {
            count++;
            return false;
        } else {

            return true;
        }
    }

    private class CurrentStateManager {

        private ModeListState mCurrentState;

        ModeListState getCurrentState() {
            return mCurrentState;
        }

        void setCurrentState(ModeListState state) {
            mCurrentState = state;
            state.onCurrentState();
        }
    }

    /**
     * ModeListState defines a set of functions through which the view could manage
     * or change the states. Sub-classes could selectively override these functions
     * accordingly to respect the specific requirements for each state. By overriding
     * these methods, state transition can also be achieved.
     */
    private abstract class ModeListState implements GestureDetector.OnGestureListener {

        /**
         * Called by the state manager when this state instance becomes the current
         * mode list state.
         */
        public void onCurrentState() {
            // Do nothing.
            showSettingsClingIfEnabled(false);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    private void showSettingsClingIfEnabled(boolean show) {
        if (mSettingCling != null) {
            int visibility = show ? VISIBLE : INVISIBLE;
            mSettingCling.setVisibility(visibility);
        }
    }
}

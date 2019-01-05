package com.android.live.camera.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.android.live.R;
import com.android.live.camera.debug.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 事件流程
 * at android.app.Activity.dispatchTouchEvent(Activity.java:3198)
 * at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2321)
 * at com.android.live.camera.ui.CameraActivityLayout.onInterceptTouchEvent(CameraActivityLayout.java:181)
 * at com.android.live.camera.ui.CameraActivityLayout.onTouchEvent(CameraActivityLayout.java:200)
 * 根据返回值不同，有可能不会走onInterceptTouchEvent或者onTouchEvent.
 * 具体到本实例中
 * ACTION_DOWN传递到{@link #onInterceptTouchEvent(MotionEvent)}中，因为返回false，同时
 * {@link #onTouchEvent(MotionEvent)}也返回false，
 * 所以会继续向子View传递，每调用一个{@link #onInterceptTouchEvent(MotionEvent)}，就会调用一次子类的
 * {@link PreviewOverlay#onTouchEvent(MotionEvent)}，而该函数每次返回true，表示会消费此次事件。
 * ACTION_MOVE会继续向下传递，直到传递到ACTION_UP事件，检测到位移大于系统默认的阈值，判定该系列的操作为滑动，
 * 返回true，所有事件教给{@link #onTouchEvent(MotionEvent)}处理，而此时{@link #mModeList}不为空，会
 * 处理该事件。
 * 需要结合源码分析。
 */


public class CameraActivityLayout extends FrameLayout {

    private final Log.Tag TAG = new Log.Tag("CameraActivityLayout");
    // Only check for intercepting touch events within first 500ms
    private static final int SWIPE_TIME_OUT = 500;

    // 判断为滑动的阈值。
    private final int mSlop;

    private final boolean mIsCaptureIntent;

    private boolean mSwipeEnabled = true;

    private ModeListView mModeList;
    private boolean mCheckToIntercept;

    private MotionEvent mDown;

    // 将主界面的输出时间传递给子界面。
    private View mTouchReceiver;

    private boolean mRequestToInterceptTouchEvents = false;


    public CameraActivityLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Distance in pixels a touch can wander before we think the user is scrolling
        mSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        Activity activity = (Activity) context;
        Intent intent = activity.getIntent();
        String action = intent.getAction();

        mIsCaptureIntent = (MediaStore.ACTION_IMAGE_CAPTURE.equals(action)
                || MediaStore.ACTION_IMAGE_CAPTURE_SECURE.equals(action)
                || MediaStore.ACTION_VIDEO_CAPTURE.equals(action));
    }

    /**
     * Enables or disables the swipe for modules not supporting the new swipe
     * logic yet.
     */
    @Deprecated
    public void setSwipeEnabled(boolean enabled) {
        mSwipeEnabled = enabled;
    }

    /*
    private float touchDownX;
    private float touchDownY;
    private boolean mScrolling;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptionTouchEvent " + ev.getActionMasked());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = ev.getX();
                touchDownY = ev.getY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownX - ev.getX()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop() ||
                        Math.abs(touchDownY - ev.getY()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    mScrolling = true;
                } else {
                    mScrolling = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent " + event.getActionMasked());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果此处返回true，接下来子所有的操作都是交给该函数处理。
                // 01-05 04:34:44.720 14067 14067 D CAM_CameraActivityLayout: onInterceptionTouchEvent 0
                // 01-05 04:34:44.721 14067 14067 D CAM_CameraActivityLayout: onTouchEvent 0
                // 01-05 04:34:44.755 14067 14067 D CAM_CameraActivityLayout: onTouchEvent 2
                // 01-05 04:34:44.772 14067 14067 D CAM_CameraActivityLayout: onTouchEvent 2
                // 01-05 04:34:44.789 14067 14067 D CAM_CameraActivityLayout: onTouchEvent 2
                // 01-05 04:34:45.150 14067 14067 D CAM_CameraActivityLayout: onTouchEvent 1
                // return true;//消费触摸事件
                // 如果返回false，表示不处理事件，各打印一次。
                // 01-05 04:39:20.102 14771 14771 D CAM_CameraActivityLayou: onInterceptionTouchEvent 0
                // 01-05 04:39:20.103 14771 14771 D CAM_CameraActivityLayou: onTouchEvent 0
                return false;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
    */

    // 不知道事件流程就添加打印。
    // ACTION_MOVE或者ACTION_UP发生的前提是一定曾经发生了ACTION_DOWN，如果你没有消费ACTION_DOWN，
    // 那么系统会认为ACTION_DOWN没有发生过，所以ACTION_MOVE或者ACTION_UP就不能被捕获。

    /**
     * The {@link #onInterceptTouchEvent(MotionEvent)} method is called whenever a touch event is detected
     * on the surface of a ViewGroup, including on the surface of its children.
     */

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 事件一层层向下传递，如果返回true，则停止向下传递。
        Log.d(TAG, "onInterceptToucheEvent Action " + ev.getActionMasked());
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            // 初始化操作
            mCheckToIntercept = true;
            mDown = MotionEvent.obtain(ev);
            mTouchReceiver = null;
            mRequestToInterceptTouchEvents = false;
            // 表示事件继续传递。
            // If onInterceptTouchEvent() returns true, the MotionEvent is intercepted, meaning
            // it is not passed on to the child, but rather to the onTouchEvent() method of the parent.
            return false;   // Do not intercept touch event, let the child handle it.
        } else if (mRequestToInterceptTouchEvents) {
            mRequestToInterceptTouchEvents = false;
            onTouchEvent(mDown);
            // 事件终止
            return true;
        } else if (ev.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            // Do not intercept touch once child is in zoom mode.
            mCheckToIntercept = false;
            Log.d(TAG, "MotionEvent.ACTION_POINTER_DOWN");
            return false;
        } else {
            Log.d(TAG, "MotionEvent.ACTION_MOVE");
            // TODO: This can be removed once we come up with a new design for b/13751653.
            if (!mCheckToIntercept) {
                return false;
            }
            if (ev.getEventTime() - ev.getDownTime() > SWIPE_TIME_OUT) {
                return false;
            }
            if (mIsCaptureIntent || !mSwipeEnabled) {
                return false;
            }
            // 开始计算手指的滑动位移。
            int deltaX = (int) (ev.getX() - mDown.getX());
            int deltaY = (int) (ev.getY() - mDown.getY());
            if (ev.getActionMasked() == MotionEvent.ACTION_MOVE &&
                    Math.abs(deltaX) > mSlop) {
                // Intercept right swipe
                if (deltaX >= Math.abs(deltaY) * 2) {
                    // 通过mTouchReceiver为空设置对应的View的Visibility属性。
                    mTouchReceiver = mModeList;
                    // 调用onTouchEvent来显示View
                    onTouchEvent(mDown);
                    Log.d(TAG, "right swipe");
                    return true;
                }
                // Intercept left swipe
                if (deltaX < -Math.abs(deltaY) * 2) {
                    // mTouchReceiver = mFilmStripLayout;
                    onTouchEvent(mDown);
                    Log.d(TAG, "left swipe");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 事件一层层向上传递，如果返回true，则停止向上传递。
        Log.d(TAG, "onTouchEvent action " + event.getActionMasked());
        android.util.Log.d("wangrl", "onTouchEvent ", new Throwable());
        if (mTouchReceiver != null) {
            mTouchReceiver.setVisibility(VISIBLE);
            return mTouchReceiver.dispatchTouchEvent(event);
        }

        // 事件继续向上传递。
        return false;
    }

    @Override
    public void onFinishInflate() {
        mModeList = (ModeListView) findViewById(R.id.mode_list_layout);
        // mFilmstripLayout = (FilmstripLayout) findViewById(R.id.filmstrip_layout);
        // 为什么系统Camera没有调用super.onFinishInflate()函数。
        super.onFinishInflate();
    }
}
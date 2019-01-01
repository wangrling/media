package com.google.android.camera.util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.google.android.camera.debug.Log;

import androidx.annotation.Nullable;

/**
 * 延迟执行Activity onResume函数，如果是下面二三情形。
 * Workaround for lockscreen double-resume() bug.
 * <p>
 * We track 3 startup situations:
 * <ul>
 *     <li>Normal startup -- e.g. from GEL.</li>
 *     <li>Secure lock screen startup -- e.g. with a keycode.</li>
 *     <li>Non-secure lock screen startup -- e.g. with just a swipe.</li>
 * </ul>
 * The KeyguardManager service can be queried to determine which state we in.
 * If start from the lock screen, the activity may be quickly started, resumed,
 * paused, stopped, and then started and resume again. This is problematic for
 * launch time from the lock screen because we typically open the camera in
 * onResume and close it onPause(). These camera operations take a long time
 * to complete. To workaround it, this class filters out high-frequency
 * onResume()->onPause() sequences if the KeyguardManager indicates that we have
 * started from the lock screen.
 * <p>
 * Subclass should override the appropriate on[Create|Start...]Tasks() method in
 * place of the original.
 * <p>
 * Sequences of onResume() followed quickly by onPause(), when the activity is
 * started from a lockscreen will result in a quick no-op.
 */

public class QuickActivity extends Activity {

    private static final Log.Tag TAG = new Log.Tag("QuickActivity");

    /** onResume tasks delay from secure lockscreen. */
    private static final long ON_RESUME_DELAY_SECURE_MILLIS = 30;
    /** onResume tasks delay from non-secure lockscreen. */
    private static final long ON_RESUME_DELAY_NON_SECURE_MILLIS = 15;

    /** A reference to the main handler on which to run lifecycle method. */
    private Handler mMainHandler;

    /**
     * True if onResume tasks have been skipped, and made false again once they
     * are executed within the onResume() method or from a delayed Runnable.
     */
    private boolean mSkippedFirstOnResume = false;

    /** When application execution started in SystemClock.elapsedRealTimeNanos(). */
    protected long mExecutionStartNanoTime = 0;
    /** Was this session started with onCreate(). */
    protected boolean mStartupOnCreate = false;

    /** Handle to Keyguard service. */
    @Nullable
    private KeyguardManager mKeyguardManager = null;

    // A runnable for deferring tasks to be performed in Resume() if starting
    // from he lockscreen.
    private final Runnable mOnResumeTasks = new Runnable() {
        @Override
        public void run() {
            if (mSkippedFirstOnResume) {
                Log.v(TAG, "delayed Runnable --> onResumeTasks()");
                // Doing the tasks, can set to false.
                mSkippedFirstOnResume = false;
                onResumeTasks();
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        logLifecycle("onNewIntent", true);
        Log.v(TAG, "Intent Action = " + intent.getAction());
        setIntent(intent);
        super.onNewIntent(intent);
        onNewIntentTasks(intent);
        logLifecycle("onNewIntent", false);
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        mExecutionStartNanoTime = SystemClock.elapsedRealtimeNanos();
        logLifecycle("onCreate", true);
        mStartupOnCreate = true;
        super.onCreate(savedInstanceState);

        // 通过Looper来创建Handler
        mMainHandler = new Handler(getMainLooper());
        onCreateTasks(savedInstanceState);
        logLifecycle("onCreate", false);
    }

    @Override
    protected final void onStart() {
        logLifecycle("onStart", true);
        onStartTasks();
        super.onStart();
        logLifecycle("onStart", false);
    }

    @Override
    protected void onResume() {
        logLifecycle("onResume", true);
        /**
         * For lockscreen launch, there are two possible flows:
         * 1. onPause() does not occur before mOnResumeTasks is executed:
         *      Runnable mOnResumeTasks sets mSkippedFirstOnResume to false.
         * 2. onPause() occur within ON_RESUME_DELAY_*_MILLIS:
         *      a. Runnable mOnResumeTasks is removed
         *      b. onPauseTasks() is skipped, mSkippedFirstOnResume remains true.
         *      c. next onResume() will immediately execute onResumeTasks()
         *         and set mSkippedFirstOnResume to false.
         */
        Log.v(TAG, "onResume(): isKeyguardLocked() = " + isKeyguardLocked());
        mMainHandler.removeCallbacks(mOnResumeTasks);
        if (isKeyguardLocked() && mSkippedFirstOnResume == false) {
            // Skipping onResumeTasks; set to true.
            mSkippedFirstOnResume = true;
            long delay = isKeyguardSecure() ? ON_RESUME_DELAY_SECURE_MILLIS :
                    ON_RESUME_DELAY_NON_SECURE_MILLIS;
            Log.v(TAG, "onResume() --> postDelayed(mOnResumeTasks, " + delay + ")");
            mMainHandler.postDelayed(mOnResumeTasks, delay);
        } else {
            Log.v(TAG, "onResume --> onResumeTasks()");
            // Doing the tasks, can set to false.
            mSkippedFirstOnResume = false;
            onResumeTasks();
        }
        super.onResume();
        logLifecycle("onResume", false);
    }

    @Override
    protected void onPause() {
        logLifecycle("onPause", true);
        // 停止Handler的执行
        mMainHandler.removeCallbacks(mOnResumeTasks);
        /**
         * Only run onPauseTasks if we have not skipped onResumeTasks in a first
         * call to onResume. If we did skip onResumeTasks (note: we just killed
         * any delayed Runnable), we also skip onPauseTasks to adhere to lifecycle
         * state machine.
         */
        if (mSkippedFirstOnResume == false) {
            Log.v(TAG, "onPause --> onPauseTasks()");
            onPauseTasks();
        }
        super.onPause();

        mStartupOnCreate = false;
        logLifecycle("onPause", false);
    }

    @Override
    protected final void onStop() {
        if (isChangingConfigurations()) {
            Log.v(TAG, "changing configurations");
        }
        logLifecycle("onStop", true);
        onStopTasks();
        super.onStop();
        logLifecycle("onStop", false);
    }

    @Override
    protected final void onRestart() {
        logLifecycle("onRestart", true);
        super.onRestart();
        // TODO support onRestartTasks() and handle the workaround for that too.
        // 没有onRestartTasks方法
    }

    @Override
    protected void onDestroy() {
        logLifecycle("onDestroy", true);
        onDestroyTasks();
        super.onDestroy();
        logLifecycle("onDestroy", false);
    }

    private void logLifecycle(String methodName, boolean start) {
        String prefix = start ? "START" : "END";
        Log.v(TAG, prefix + " " + methodName + ": Activity = " + toString());
    }

    // 是否从锁屏进去
    protected boolean isKeyguardLocked() {
        if (mKeyguardManager == null) {
            mKeyguardManager = AndroidServices.instance().provideKeyguardManager();
        }
        if (mKeyguardManager != null) {
            return mKeyguardManager.isKeyguardLocked();
        }
        return false;
    }

    // 是否使用密码锁
    protected boolean isKeyguardSecure() {
        if (mKeyguardManager == null) {
            mKeyguardManager = AndroidServices.instance().provideKeyguardManager();
        }
        if (mKeyguardManager != null) {
            return mKeyguardManager.isKeyguardSecure();
        }
        return false;
    }

    /** Subclass should override this in place of {@link Activity#onNewIntent(Intent)}. */
    protected void onNewIntentTasks(Intent newIntent) {

    }

    /** Subclasses should override this in place of {@link Activity#onCreate}. */
    private void onCreateTasks(Bundle savedInstanceState) {

    }

    /** Subclasses should override this in plae of {@link Activity#onStart}. */
    protected void onStartTasks() {

    }

    /** Subclasses should override this in place of {@link Activity#onResume}. */
    protected void onResumeTasks() {

    }

    /** Subclasses should override this in place of {@link Activity#onPause}. */
    public void onPauseTasks() {

    }

    /** Subclass should override this in place of {@link Activity#onStop}. */
    protected void onStopTasks() {

    }

    /** Subclass should override this in place of {@link Activity#onDestroy}. */
    protected void onDestroyTasks() {

    }
}

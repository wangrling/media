package com.android.mm.grafika;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.mm.R;
import com.android.mm.grafika.gles.EglCore;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;

/**
 * Exercises SurfaceHolder#setFixedSize().
 * <p>
 * http://android-developers.blogspot.com/2013/09/using-hardware-scaler-for-performance.html
 * <p>
 * The purpose of the feature is to allow games to render at 720p or 1080p to get good
 * performance on displays with a large number of pixels.  It's easier (and more fun) to
 * see the effects when we crank the resolution way down.  Normally the resolution would
 * be fixed, perhaps with minor tweaks (e.g. letterboxing via AspectFrameLayout) to match
 * the device aspect ratio, but here we make it variable to match the display window.
 * <p>
 * TODO: examine effects on touch input
 */

/**
 * SystemClock
 * 几种时间统计的差异
 * System.currentTimeMillis() is the standard "wall" clock (time and date) expressing milliseconds
 * since the epoch. The wall clock can be set by the user or the phone network
 * (see setCurrentTimeMillis(long)), so the time may jump backwards or forwards unpredictably.
 * This clock should only be used when correspondence with real-world dates and times is important,
 * such as in a calendar or alarm clock application. Interval or elapsed time measurements should
 * use a different clock. If you are using System.currentTimeMillis(), consider listening to
 * the ACTION_TIME_TICK, ACTION_TIME_CHANGED and ACTION_TIMEZONE_CHANGED Intent broadcasts to
 * find out when the time changes.
 *
 * 很多方法都是以下面这个方法为基准。
 * uptimeMillis() is counted in milliseconds since the system was booted. This clock stops when
 * the system enters deep sleep (CPU off, display dark, device waiting for external input),
 * but is not affected by clock scaling, idle, or other power saving mechanisms. This is the
 * basis for most interval timing such as Thread.sleep(millis), Object.wait(millis), and
 * System.nanoTime(). This clock is guaranteed to be monotonic, and is suitable for interval
 * timing when the interval does not span device sleep. Most methods that accept a timestamp
 * value currently expect the uptimeMillis() clock.
 *
 * elapsedRealtime() and elapsedRealtimeNanos() return the time since the system was
 * booted, and include deep sleep. This clock is guaranteed to be monotonic, and continues to
 * tick even when the CPU is in power saving modes, so is the recommend basis for general
 * purpose interval timing.
 */

public class HardwareScalerActivity extends Activity implements SurfaceHolder.Callback,
        Choreographer.FrameCallback {
    private static final String TAG = GrafikaActivity.TAG;

    // [ This used to have "a few thoughts about app life cycle and SurfaceView".  These
    //   are now at http://source.android.com/devices/graphics/architecture.html in
    //   Appendix B. ]
    //
    // This Activity uses approach #2 (Surface-driven).

    // Indexes into the data arrays.
    private static final int SURFACE_SIZE_TINY = 0;
    private static final int SURFACE_SIZE_SMALL = 1;
    private static final int SURFACE_SIZE_MEDIUM = 2;
    private static final int SURFACE_SIZE_FULL = 3;

    private static final int[] SURFACE_DIM = new int[] { 64, 240, 480, -1 };
    private static final String[] SURFACE_LABEL = new String[] {
            "tiny", "small", "medium", "full"
    };

    private int mSelectedSize;
    private int mFullViewWidth;
    private int mFullViewHeight;
    private int[][] mWindowWidthHeight;
    private boolean mFlatShadingChecked;

    // Rendering code runs on this thread.  The thread's life span is tied to the Surface.
    private RenderThread mRenderThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hardware_scaler);

        mFullViewWidth = mFullViewHeight = 512;     // want actual view size, but it's not avail
        mWindowWidthHeight = new int[SURFACE_DIM.length][2];
        updateControls();

        SurfaceView sv = findViewById(R.id.hardwareScalerSurfaceView);
        sv.getHolder().addCallback(this);
    }

    /**
     * Updates the on-screen controls to reflect the current state of the app.
     */
    private void updateControls() {
        configureRadioButton(R.id.surfaceSizeTinyRadio, SURFACE_SIZE_TINY);
        configureRadioButton(R.id.surfaceSizeSmallRadio, SURFACE_SIZE_SMALL);
        configureRadioButton(R.id.surfaceSizeMediumRadio, SURFACE_SIZE_MEDIUM);
        configureRadioButton(R.id.surfaceSizeFullRadio, SURFACE_SIZE_FULL);

        TextView tv = (TextView) findViewById(R.id.viewSizeValueText);
        tv.setText(mFullViewWidth + "x" + mFullViewHeight);

        CheckBox cb = (CheckBox) findViewById(R.id.flatShadingCheckBox);
        cb.setChecked(mFlatShadingChecked);
    }

    /**
     * Generates the radio button text.
     */
    private void configureRadioButton(int id, int index) {
        RadioButton rb;
        rb = (RadioButton) findViewById(id);
        rb.setChecked(mSelectedSize == index);
        rb.setText(SURFACE_LABEL[index] + " (" + mWindowWidthHeight[index][0] + "x" +
                mWindowWidthHeight[index][1] + ")");
    }

    /**
     * Choreographer callback, called near vsync.
     * 开始渲染动画。
     *
     * @param frameTimeNanos The time in nanoseconds when the frame started being rendered,
     *                       in the System.nanoTime() timebase. Divide this value by 1000000 to
     *                       convert it to the SystemClock.uptimeMillis() time base.
     */
    @Override
    public void doFrame(long frameTimeNanos) {
        RenderThread.RenderHandler rh = mRenderThread.getHandler();
        if (rh != null) {
            // Posts a frame callback to run on the next frame.
            Choreographer.getInstance().postFrameCallback(this);
            rh.sendDoFrame(frameTimeNanos);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated holder = " + holder);

        // Grab the view's width. It's not available before now.
        Rect size = holder.getSurfaceFrame();
        mFullViewWidth = size.width();
        mFullViewHeight = size.height();

        // Configure our fixed-size values.  We want to configure it so that the narrowest
        // dimension (e.g. width when device is in portrait orientation) is equal to the
        // value in SURFACE_DIM, and the other dimension is sized to maintain the same
        // aspect ratio.
        float windowAspect = (float) mFullViewHeight / (float) mFullViewWidth;
        for (int i = 0; i < SURFACE_DIM.length; i++) {
            if (i == SURFACE_SIZE_FULL) {
                // special-case for full size
                mWindowWidthHeight[i][0] = mFullViewWidth;
                mWindowWidthHeight[i][1] = mFullViewHeight;
            } else if (mFullViewWidth < mFullViewHeight) {
                // portrait
                mWindowWidthHeight[i][0] = SURFACE_DIM[i];
                mWindowWidthHeight[i][1] = (int) (SURFACE_DIM[i] * windowAspect);
            } else {
                // landscape
                mWindowWidthHeight[i][0] = (int) (SURFACE_DIM[i] / windowAspect);
                mWindowWidthHeight[i][1] = SURFACE_DIM[i];
            }
        }

        // Some controls include text based on the view dimensions, so update now.
        SurfaceView sv = findViewById(R.id.hardwareScalerSurfaceView);
        mRenderThread = new RenderThread(sv.getHolder());
        mRenderThread.setName("HardwareScaler GL render");
        mRenderThread.start();
        // waitUntilReady执行得快，所以需要等待。
        mRenderThread.waitUntilReady();

        RenderThread.RenderHandler rh = mRenderThread.getHandler();
        if (rh != null) {
            rh.sendSetFlatShading(mFlatShadingChecked);
            rh.sendSurfaceCreated();
        }

        // start the draw events.
        Choreographer.getInstance().postFrameCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged fmt=" + format + " size=" + width + "x" + height +
                " holder=" + holder);

        RenderThread.RenderHandler rh = mRenderThread.getHandler();
        if (rh != null) {
            rh.sendSurfaceChanged(format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed holder=" + holder);

        // We need to wait for the render thread to shut down before continuing because we
        // don't want the Surface to disappear out from under it mid-render.  The frame
        // notifications will have been stopped back in onPause(), but there might have
        // been one in progress.

        RenderThread.RenderHandler rh = mRenderThread.getHandler();
        if (rh != null) {
            rh.sendShutdown();
            try {
                mRenderThread.join();
            } catch (InterruptedException ie) {
                // not expected
                throw new RuntimeException("join was interrupted", ie);
            }
        }
        mRenderThread = null;

        Log.d(TAG, "surfaceDestroyed complete");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If we already have a Surface, we just need to resume the frame notifications.
        if (mRenderThread != null) {
            Log.d(TAG, "onResume re-hooking choreographer");
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // If the callback was posted, remove it.  This stops the notifications.  Ideally we
        // would send a message to the thread letting it know, so when it wakes up it can
        // reset its notion of when the previous Choreographer event arrived.
        Log.d(TAG, "onPause unhooking choreographer");
        Choreographer.getInstance().removeFrameCallback(this);
    }

    /**
     * onClick handler for radio buttons.
     */
    public void onRadioButtonClicked(View view) {
        int newSize;

        RadioButton rb = (RadioButton) view;
        if (!rb.isChecked()) {
            Log.d(TAG, "Got click on non-checked radio button");
            return;
        }

        switch (rb.getId()) {
            case R.id.surfaceSizeTinyRadio:
                newSize = SURFACE_SIZE_TINY;
                break;
            case R.id.surfaceSizeSmallRadio:
                newSize = SURFACE_SIZE_SMALL;
                break;
            case R.id.surfaceSizeMediumRadio:
                newSize = SURFACE_SIZE_MEDIUM;
                break;
            case R.id.surfaceSizeFullRadio:
                newSize = SURFACE_SIZE_FULL;
                break;
            default:
                throw new RuntimeException("Click from unknown id " + rb.getId());
        }

        mSelectedSize = newSize;

        int[] wh = mWindowWidthHeight[newSize];

        // Update the Surface size.  This causes a "surface changed" event, but does not
        // destroy and re-create the Surface.
        SurfaceView sv = (SurfaceView) findViewById(R.id.hardwareScalerSurfaceView);
        SurfaceHolder sh = sv.getHolder();
        Log.d(TAG, "setting size to " + wh[0] + "x" + wh[1]);
        sh.setFixedSize(wh[0], wh[1]);
    }

    public void onFlatShadingClicked(@SuppressWarnings("unused") View unused) {
        CheckBox cb = (CheckBox) findViewById(R.id.flatShadingCheckBox);
        mFlatShadingChecked = cb.isChecked();

        RenderThread.RenderHandler rh = mRenderThread.getHandler();
        if (rh != null) {
            rh.sendSetFlatShading(mFlatShadingChecked);
        }
    }

    /**
     * This class handles all OpenGL rendering.
     * <p>
     * We use Choreographer to coordinate with the device vsync.  We deliver one frame
     * per vsync.  We can't actually know when the frame we render will be drawn, but at
     * least we get a consistent frame interval.
     * <p>
     * Start the render thread after the Surface has been created.
     */
    private static class RenderThread extends Thread {
        // Object must be created on render thread to get correct Looper, but is used from
        // UI thread, so we need to declare it volatile to ensure the UI thread sees a fully
        // constructed object.
        private volatile RenderHandler mHandler;

        private SurfaceHolder mSurfaceHolder;
        private EglCore mEglCore;

        // Used to wait for the thread to start.
        private Object mStartLock = new Object();
        private boolean mReady = false;



        /**
         * Pass in the SurfaceView's SurfaceHolder.  Note the Surface may not yet exist.
         */
        public RenderThread(SurfaceHolder holder) {
            mSurfaceHolder = holder;

        }

        @Override
        public void run() {
            Looper.prepare();

            mHandler = new RenderHandler(this);
            mEglCore = new EglCore(null, 0);
            /**
             * 12-27 18:49:48.303 28296 28296 D Grafika : surfaceCreated holder = android.view.SurfaceView$4@d2653b5
             * 12-27 18:49:48.304 28296 28296 D Grafika : waiting until ready
             * 12-27 18:49:48.305 28296 28791 D Grafika : Trying GLES 2
             * 12-27 18:49:48.311 28296 28791 D Grafika : EGLContext created, client version 2
             * 12-27 18:49:48.311 28296 28791 D Grafika : notify wait thread
             */
            synchronized (mStartLock) {
                mReady = true;
                Log.d(TAG, "notify wait thread");
                mStartLock.notify();    // signal waitUntilReady
            }
        }

        public RenderHandler getHandler() {
            return mHandler;
        }

        public void waitUntilReady() {
            synchronized (mStartLock) {
                while (!mReady) {
                    try {
                        Log.d(TAG, "waiting until ready");
                        mStartLock.wait();
                    } catch (InterruptedException ie) { /* not expected */ }
                }
            }
        }


        private static class RenderHandler extends Handler {
            private static final int MSG_SURFACE_CREATED = 0;
            private static final int MSG_SURFACE_CHANGED = 1;
            private static final int MSG_DO_FRAME = 2;
            private static final int MSG_FLAT_SHADING = 3;
            private static final int MSG_SHUTDOWN = 5;

            // This shouldn't need to be a weak ref, since we'll go away when the Looper quits,
            // but no real harm in it.
            private WeakReference<RenderThread> mWeakRenderThread;

            /**
             * Call from render thread.
             */
            public RenderHandler(RenderThread rt) {
                mWeakRenderThread = new WeakReference<RenderThread>(rt);
            }


            public void sendDoFrame(long frameTimeNanos) {

            }

            public void sendSetFlatShading(boolean mFlatShadingChecked) {

            }

            public void sendSurfaceCreated() {

            }

            public void sendSurfaceChanged(int format, int width, int height) {

            }

            public void sendShutdown() {

            }
        }
    }
}

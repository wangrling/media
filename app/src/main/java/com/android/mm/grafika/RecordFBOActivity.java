package com.android.mm.grafika;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Choreographer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.mm.R;
import com.google.common.io.FileBackedOutputStream;

import androidx.annotation.Nullable;

// 重点实战

/**
 * Demonstrate efficient display + recording of OpenGL rendering using an FBO.
 * This records only the GL surface (i.e. not the app UI, nav bar, status bar,
 * or alert dialog).
 * <p>
 * This uses a plain SurfaceView, rather than GLSurfaceView, so we have full control
 * over the EGL config and rendering.  When available, we use GLES 3, which allows us
 * to do recording with one extra copy instead of two.
 *
 */

public class RecordFBOActivity extends Activity implements
        SurfaceHolder.Callback, Choreographer.FrameCallback {

    private static final String TAG = GrafikaActivity.TAG;

    // See the (lengthy) notes at the top of HardwareScalerActivity for thoughts about
    // Activity / Surface lifecycle management.

    private static final int RECMETHOD_DRAW_TWICE = 0;
    private static final int RECMETHOD_FBO = 1;
    private static final int RECMETHOD_BLIT_FRAMEBUFFER = 2;

    private boolean mRecordingEnabled = false;          // controls button state
    private boolean mBlitFramebufferAllowed = false;    // requires GLES3
    private int mSelectedRecordMethod;                  // current radio button

    private RenderThread mRenderThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fbo_record);

        mSelectedRecordMethod = RECMETHOD_FBO;
        updateControls();

        SurfaceView sv = findViewById(R.id.fboActivitySurfaceView);
        sv.getHolder().addCallback(this);

        Log.d(TAG, "RecordFBOActivity: onCreate done");
    }

    /**
     * Updates the on-screen controls to reflect the current state of the app.
     */
    private void updateControls() {
        Button toggleRelease = (Button) findViewById(R.id.fboRecordButton);
        int id = mRecordingEnabled ?
                R.string.toggleRecordingOff : R.string.toggleRecordingOn;
        toggleRelease.setText(id);

        RadioButton rb;
        rb = (RadioButton) findViewById(R.id.recDrawTwiceRadio);
        rb.setChecked(mSelectedRecordMethod == RECMETHOD_DRAW_TWICE);
        rb = (RadioButton) findViewById(R.id.recFboRadio);
        rb.setChecked(mSelectedRecordMethod == RECMETHOD_FBO);
        rb = (RadioButton) findViewById(R.id.recFramebufferRadio);
        rb.setChecked(mSelectedRecordMethod == RECMETHOD_BLIT_FRAMEBUFFER);
        rb.setEnabled(mBlitFramebufferAllowed);
    }

    @Override
    public void doFrame(long frameTimeNanos) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void clickToggleRecording(View view) {
    }

    // onClick handler for radio buttons.
    public void onRadioButtonClicked(View view) {
        RadioButton rb = (RadioButton) view;
        if (!rb.isChecked()) {
            Log.d(TAG, "Got click on non-checked radio button");
            return;
        }

        switch (rb.getId()) {
            case R.id.recDrawTwiceRadio:
                mSelectedRecordMethod = RECMETHOD_DRAW_TWICE;
                break;
            case R.id.recFboRadio:
                mSelectedRecordMethod = RECMETHOD_FBO;
                break;
            case R.id.recFramebufferRadio:
                mSelectedRecordMethod = RECMETHOD_BLIT_FRAMEBUFFER;
                break;
            default:
                throw new RuntimeException("Click from unknown id " + rb.getId());
        }

        Log.d(TAG, "Selected rec mode " + mSelectedRecordMethod);

        RenderHandler rh = mRenderThread.getHandler();
        if (rh != null) {
            rh.setRecordMethod(mSelectedRecordMethod);
        }
    }


    private class RenderThread extends Thread {


        @Override
        public void run() {
            super.run();
        }
    }

    /**
     * Handler for RenderThread.  Used for messages sent from the UI thread to the render thread.
     * <p>
     * The object is created on the render thread, and the various "send" methods are called
     * from the UI thread.
     */
    private static class RenderHandler extends Handler {

    }
}


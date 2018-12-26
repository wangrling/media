package com.android.mm.grafika;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;

import com.android.mm.R;
import com.android.mm.grafika.gles.EglCore;
import com.android.mm.grafika.gles.WindowSurface;

import java.io.File;

import androidx.annotation.Nullable;

/**
 * Demonstrates capturing video into a ring buffer.  When the "capture" button is clicked,
 * the buffered video is saved.
 * <p>
 * Capturing and storing raw frames would be slow and require lots of memory.  Instead, we
 * feed the frames into the video encoder and buffer the output.
 * <p>
 * Whenever we receive a new frame from the camera, our SurfaceTexture callback gets
 * notified.  That can happen on an arbitrary thread, so we use it to send a message
 * through our Handler.  That causes us to render the new frame to the display and to
 * our video encoder.
 */

public class ContinuousCaptureActivity extends Activity  {

    private static final String TAG = GrafikaActivity.TAG;

    private static final int VIDEO_WIDTH = 1280;  // dimensions for 720p video
    private static final int VIDEO_HEIGHT = 720;
    private static final int DESIRED_PREVIEW_FPS = 15;

    private EglCore mEglCore;
    private WindowSurface mDisplaySurface;
    private SurfaceTexture mCameraTexture;  // receives the output from the camera preview
    // private FullFrameRect mFullFrameBlit;
    private final float[] mTmpMatrix = new float[16];
    private int mTextureId;
    private int mFrameNum;

    private Camera mCamera;
    private int mCameraPreviewThousandFps;

    private File mOutputFile;
    // private CircularEncoder mCircEncoder;
    private WindowSurface mEncoderSurface;
    private boolean mFileSaveInProgress;

    // private MainHandler mHandler;
    private float mSecondsOfVideo;

    // 主线程的Handler,子线程获取实例就能sendMessage到主线程中执行。

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_continuous);

    }
}

package com.android.mm.grafika.utils;

import android.media.MediaCodec;
import android.view.Choreographer;
import android.view.Surface;

import com.android.mm.grafika.GrafikaActivity;

import java.io.File;
import java.io.IOException;

/**
 * Plays the video track from a movie file to a Surface.
 * TODO: needs more advanced shuttle controls (pause/resume, skip)
 */
public class MoviePlayer {
    private static final String TAG = GrafikaActivity.TAG;
    private static final boolean VERBOSE = false;

    // Declare this here to reduce allocations.
    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    private File mSourceFile;
    private Surface mOutputSurface;
    FrameCallback mFrameCallback;
    private boolean mLoop;
    private int mVideoWidth;
    private int mVideoHeight;

    // Callback invoked when rendering video frames. The MoviePlayer client
    // must provide one of these.
    public interface FrameCallback {
        /**
         * Called immediately before the frame is rendered.
         * @param presentationTimeUsec The desired presentation time, in microseconds.
         */
        void preRender(long presentationTimeUsec);

        /**
         * Called immediately after the frame render call returns.  The frame may not have
         * actually been rendered yet.
         * TODO: is this actually useful?
         */
        void postRender();

        /**
         * Called after the last frame of a looped movie has been rendered.  This allows the
         * callback to adjust its expectations of the next presentation time stamp.
         */
        void loopReset();
    }

    /**
     * Constructs a MoviePlayer.
     *
     * @param sourceFile The video file to open.
     * @param outputSurface The Surface where frames will be sent.
     * @param frameCallback Callback object, used to pace output.
     * @throws IOException
     */
    public MoviePlayer(File sourceFile, Surface outputSurface, FrameCallback frameCallback)
            throws IOException {

    }
}

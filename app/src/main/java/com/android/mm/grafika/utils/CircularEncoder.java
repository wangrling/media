package com.android.mm.grafika.utils;

import android.media.MediaCodec;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import com.android.mm.grafika.ContinuousCaptureActivity;
import com.android.mm.grafika.GrafikaActivity;

/**
 * Encodes video in a fixed-size circular buffer.
 * <p>
 * The obvious way to do this would be to store each packet in its own buffer and hook it
 * into a linked list.  The trouble with this approach is that it requires constant
 * allocation, which means we'll be driving the GC to distraction as the frame rate and
 * bit rate increase.  Instead we create fixed-size pools for video data and metadata,
 * which requires a bit more work for us but avoids allocations in the steady state.
 * <p>
 * Video must always start with a sync frame (a/k/a key frame, a/k/a I-frame).  When the
 * circular buffer wraps around, we either need to delete all of the data between the frame at
 * the head of the list and the next sync frame, or have the file save function know that
 * it needs to scan forward for a sync frame before it can start saving data.
 * <p>
 * When we're told to save a snapshot, we create a MediaMuxer, write all the frames out,
 * and then go back to what we were doing.
 */

public class CircularEncoder {

    private static final String TAG = GrafikaActivity.TAG;
    private static final boolean VERBOSE = false;

    private static final String MIME_TYPE = "video/avc";    // H.264 Advanced Video Coding
    private static final int IFRAME_INTERVAL = 1;           // sync frame every second

    private EncoderThread mEncoderThread;
    private Surface mInputSurface;
    private MediaCodec mEncoder;

    /**
     * Callback function definitions. Circular caller must provide one.
     * {@link ContinuousCaptureActivity.MainHandler} 实现回调。
     */
    public interface Callback {
        /**
         * Called some time after saveVideo(), when all data has been written to the
         * output file.
         *
         * @param status Zero means success, nonzero indicates failure.
         */
        void fileSaveComplete(int status);

        /**
         * Called occasionally.
         *
         * @param totalTimeMsec Total length, in milliseconds, of buffered video.
         */
        void bufferStatus(long totalTimeMsec);
    }

    /**
     * Shuts down the encoder thread, and releases encoder resources.
     * <p>
     * Does not return until the encoder thread has stopped.
     */
    public void shutdown() {
        if (VERBOSE) Log.d(TAG, "releasing encoder objects");

        Handler handler = mEncoderThread.getHandler();
        handler.sendMessage(handler.obtainMessage(EncoderThread.EncoderHandler.MSG_SHUTDOWN));
        try {
            mEncoderThread.join();
        } catch (InterruptedException ie) {
            Log.w(TAG, "Encoder thread join() was interrupted", ie);
        }

        if (mEncoder != null) {
            mEncoder.stop();
            mEncoder.release();
            mEncoder = null;
        }
    }

    /**
     * Configures encoder, and prepares the input Surface.
     *
     * @param width Width of encoded video, in pixels.  Should be a multiple of 16.
     * @param height Height of encoded video, in pixels.  Usually a multiple of 16 (1080 is ok).
     * @param bitRate Target bit rate, in bits.
     *                6000000，大概需要6M
     * @param frameRate Expected frame rate.
     *                  选取相机的默认值。
     * @param desiredSpanSec How many seconds of video we want to have in our buffer at any time.
     *                       默认七秒钟。
     */
    public CircularEncoder(int width, int height, int bitRate, int frameRate, int desiredSpanSec,
                           Callback cb) {
        // The goal is to size the buffer so that we can accumulate N seconds worth of video,
        // where N is passed in as "desiredSpanSec".  If the codec generates data at roughly
        // the requested bit rate, we can compute it as time * bitRate / bitsPerByte.
        //
        // Sync frames will appear every (frameRate * IFRAME_INTERVAL) frames.  If the frame
        // rate is higher or lower than expected, various calculations may not work out right.
        //
        // Since we have to start muxing from a sync frame, we want to ensure that there's
        // room for at least one full GOP in the buffer, preferrably two.
        // 关键帧可能要看h264协议。
        if (desiredSpanSec < IFRAME_INTERVAL * 2) {
            throw new RuntimeException("Requested time span is too short: " + desiredSpanSec +
                    " vs. " + (IFRAME_INTERVAL * 2));
        }
        CircularEncoderBuffer encBuffer = new

    }

    private static class EncoderThread extends Thread {

    }
}

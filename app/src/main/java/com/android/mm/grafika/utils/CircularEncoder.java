package com.android.mm.grafika.utils;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import com.android.mm.grafika.ContinuousCaptureActivity;
import com.android.mm.grafika.GrafikaActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

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
 *
 * 重量级的接口
 * muxer.writeSampleData(videoTrack, buf, info);
 */

/**
 * 在H.264/AVC视频编码标准中，整个系统框架被分为了两个层面：视频编码层面（VCL）和网络抽象层面（NAL）。
 * 其中，前者负责有效表示视频数据的内容，而后者则负责格式化数据并提供头信息，
 * 以保证数据适合各种信道和存储介质上的传输。
 *
 * 使用CircularEncoderBuffer进行数据存储，使用EncoderThread进行编码工作。
 * CircularEncoder只是对两者的封装，提供几个public接口传递供Activity调用。
 */

public class CircularEncoder {

    private static final String TAG = GrafikaActivity.TAG;
    private static final boolean VERBOSE = true;

    private static final String MIME_TYPE = "video/avc";    // H.264 Advanced Video Coding
    private static final int IFRAME_INTERVAL = 1;           // sync frame every second

    private EncoderThread mEncoderThread;
    private Surface mInputSurface;
    private MediaCodec mEncoder;

    /**
     * Returns the encoder's input surface.
     */
    public Surface getInputSurface() {
        return mInputSurface;
    }

    /**
     * Initiates saving the currently-buffered frames to the specified output file.  The
     * data will be written as a .mp4 file.  The call returns immediately.  When the file
     * save completes, the callback will be notified.
     * <p>
     * The file generation is performed on the encoder thread, which means we won't be
     * draining the output buffers while this runs.  It would be wise to stop submitting
     * frames during this time.
     */
    public void saveVideo(File outputFile) {
        Handler handler = mEncoderThread.getHandler();
        handler.sendMessage(handler.obtainMessage(
                EncoderThread.EncoderHandler.MSG_SAVE_VIDEO, outputFile
        ));
    }

    // 通知编码器即将有新的帧到达，做好编码准备。
    /**
     * Notifies the encoder thread that a new frame will shortly be provided to the encoder.
     * <p>
     * There may or may not yet be data available from the encoder output.  The encoder
     * has a fair mount of latency due to processing, and it may want to accumulate a
     * few additional buffers before producing output.  We just need to drain it regularly
     * to avoid a situation where the producer gets wedged up because there's no room for
     * additional frames.
     * <p>
     * If the caller sends the frame and then notifies us, it could get wedged up.  If it
     * notifies us first and then sends the frame, we guarantee that the output buffers
     * were emptied, and it will be impossible for a single additional frame to block
     * indefinitely.
     */
    public void frameAvailableSoon() {
        Handler handler = mEncoderThread.getHandler();
        handler.sendMessage(handler.obtainMessage(
                EncoderThread.EncoderHandler.MSG_FRAME_AVAILABLE_SOON
        ));
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
                           Callback cb) throws IOException {
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
        CircularEncoderBuffer encBuffer = new CircularEncoderBuffer(bitRate, frameRate,
                desiredSpanSec);

        MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, width, height);

        // Set some properties.  Failing to specify some of these can cause the MediaCodec
        // configure() call to throw an unhelpful exception.
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
        if (VERBOSE) Log.d(TAG, "format: " + format);

        // Create a MediaCodec encoder, and configure it with our format. Get a surface
        // we can use for input and wrap it with a class that handle the EGL work.
        mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
        mEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mInputSurface = mEncoder.createInputSurface();
        mEncoder.start();

        // Start the encoder thread last. That way we're sure it can see all of the
        // state we've initialized.
        mEncoderThread = new EncoderThread(mEncoder, encBuffer, cb);
        mEncoderThread.start();
        mEncoderThread.waitUntilReady();
    }


    /**
     * Object that encapsulates the encoder thread.
     * <p>
     * We want to sleep until there's work to do.  We don't actually know when a new frame
     * arrives at the encoder, because the other thread is sending frames directly to the
     * input surface.  We will see data appear at the decoder output, so we can either use
     * an infinite timeout on dequeueOutputBuffer() or wait() on an object and require the
     * calling app wake us.  It's very useful to have all of the buffer management local to
     * this thread -- avoids synchronization -- so we want to do the file muxing in here.
     * So, it's best to sleep on an object and do something appropriate when awakened.
     * <p>
     * This class does not manage the MediaCodec encoder startup/shutdown.  The encoder
     * should be fully started before the thread is created, and not shut down until this
     * thread has been joined.
     */
    private static class EncoderThread extends Thread {
        private MediaCodec mEncoder;
        private MediaFormat mEncodedFormat;
        private MediaCodec.BufferInfo mBufferInfo;

        private EncoderHandler mHandler;
        private CircularEncoderBuffer mEncBuffer;
        private CircularEncoder.Callback mCallback;
        private int mFrameNum;

        private final Object mLock = new Object();
        private volatile boolean mReady = false;

        public EncoderThread(MediaCodec mediaCodec, CircularEncoderBuffer encBuffer,
                             CircularEncoder.Callback callback) {
            mEncoder = mediaCodec;
            mEncBuffer = encBuffer;
            mCallback = callback;

            mBufferInfo = new MediaCodec.BufferInfo();
        }

        /**
         * Thread entry point.
         * <p>
         * Prepares the Looper, Handler, and signals anybody watching that we're ready to go.
         */
        @Override
        public void run() {
            Looper.prepare();
            mHandler = new EncoderHandler(this);

            Log.d(TAG, "encoder thread ready");
            synchronized (mLock) {
                mReady = true;
                mLock.notify();    // signal waitUntilReady()
            }

            // 一直在循环消息。
            Looper.loop();

            synchronized (mLock) {
                mReady = false;
                mHandler = null;
            }
            Log.d(TAG, "looper quit");
        }

        /**
         * Waits until the encoder thread is ready to receive messages.
         * <p>
         * Call from non-encoder thread.
         * 等待notify通知。
         */
        public void waitUntilReady() {
            synchronized (mLock) {
                while (!mReady) {
                    try {
                        mLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * Returns the Handler used to send messages to the encoder thread.
         */
        public EncoderHandler getHandler() {
            synchronized (mLock) {
                // Confirm ready state.
                if (!mReady) {
                    throw new RuntimeException("not ready");
                }
            }
            return mHandler;
        }

        /**
         * Drains the encoder output.
         * <p>
         * See notes for {@link CircularEncoder#frameAvailableSoon()}.
         */
        void frameAvailableSoon() {
            if (VERBOSE) Log.d(TAG, "frameAvailableSoon");
            drainEncoder();

            mFrameNum++;
            if ((mFrameNum % 10) == 0) {    // TODO: should base off frame rate or clock?
                mCallback.bufferStatus(mEncBuffer.computeTimeSpanUsec());
            }
        }

        // 将数据保存到Circular Buffer中。
        public void drainEncoder() {
            // no timeout -- check for buffer, fail if none.
            final int TIMEOUT_USEC = 0;

            ByteBuffer[] encoderOutputBuffers = mEncoder.getOutputBuffers();
            while (true) {
                int encoderStatus = mEncoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
                if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    break;
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // not expected for an encoder
                    encoderOutputBuffers = mEncoder.getOutputBuffers();
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // Should happen before receiving buffers, and should only happen once.
                    // The MediaFormat contains the csd-0 and csd-1 keys, which we'll need
                    // for MediaMuxer.  It's unclear what else MediaMuxer might want, so
                    // rather than extract the codec-specific data and reconstruct a new
                    // MediaFormat later, we just grab it here and keep it around.
                    mEncodedFormat = mEncoder.getOutputFormat();
                    Log.d(TAG, "encoder output format changed: " + mEncodedFormat);
                } else if (encoderStatus < 0) {
                    Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " +
                            encoderStatus);
                    // let's ignore it
                } else {
                    ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                    if (encodedData == null) {
                        throw new RuntimeException("encoderOutputBuffer " + encoderStatus +
                                " was null");
                    }

                    if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                        // The codec config data was pulled out when we got the
                        // INFO_OUTPUT_FORMAT_CHANGED status.  The MediaMuxer won't accept
                        // a single big blob -- it wants separate csd-0/csd-1 chunks --
                        // so simply saving this off won't work.
                        if (VERBOSE) Log.d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                        mBufferInfo.size = 0;
                    }

                    if (mBufferInfo.size != 0) {
                        // adjust the ByteBuffer values to match BufferInfo (not needed?)
                        encodedData.position(mBufferInfo.offset);
                        encodedData.limit(mBufferInfo.offset + mBufferInfo.size);

                        // 将数据添加到EncoderBuffer中。
                        mEncBuffer.add(encodedData, mBufferInfo.flags,
                                mBufferInfo.presentationTimeUs);

                        if (VERBOSE) {
                            Log.d(TAG, "sent " + mBufferInfo.size + " bytes to muxer, ts=" +
                                    mBufferInfo.presentationTimeUs);
                        }
                    }

                    // 将Codec的Buffer释放回环形区域。
                    mEncoder.releaseOutputBuffer(encoderStatus, false);

                    if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        Log.w(TAG, "reached end of stream unexpectedly");
                        break;      // out of while
                    }
                }
            }
        }

        /**
         * Saves the encoder output to a .mp4 file.
         * <p>
         * We'll drain the encoder to get any lingering data, but we're not going to shut
         * the encoder down or use other tricks to try to "flush" the encoder.  This may
         * mean we miss the last couple of submitted frames if they're still working their
         * way through.
         * <p>
         * We may want to reset the buffer after this -- if they hit "capture" again right
         * away they'll end up saving video with a gap where we paused to write the file.
         */
        void saveVideo(File outputFile) {
            if (VERBOSE) Log.d(TAG, "saveVideo " + outputFile);

            int index = mEncBuffer.getFirstIndex();
            if (index < 0) {
                Log.w(TAG, "Unable to get first index");
                mCallback.fileSaveComplete(1);
                return;
            }

            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            MediaMuxer muxer = null;
            int result = -1;
            try {
                muxer = new MediaMuxer(outputFile.getPath(),
                        MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                int videoTrack = muxer.addTrack(mEncodedFormat);
                muxer.start();

                do {
                    ByteBuffer buf = mEncBuffer.getChunk(index, info);
                    if (VERBOSE) {
                        Log.d(TAG, "SAVE " + index + " flags=0x" + Integer.toHexString(info.flags));
                    }
                    // 不断将数据写入到文件中。
                    muxer.writeSampleData(videoTrack, buf, info);
                    index = mEncBuffer.getNextIndex(index);
                } while (index >= 0);
                result = 0;
            } catch (IOException ioe) {
                Log.w(TAG, "muxer failed", ioe);
                result = 2;
            } finally {
                if (muxer != null) {
                    muxer.stop();
                    muxer.release();
                }
            }

            if (VERBOSE) {
                Log.d(TAG, "muxer stopped, result=" + result);
            }
            mCallback.fileSaveComplete(result);
        }

        /**
         * Tells the Looper to quit.
         */
        void shutdown() {
            if (VERBOSE) Log.d(TAG, "shutdown");
            Looper.myLooper().quit();
        }

        /**
         * Handler for EncoderThread.  Used for messages sent from the UI thread (or whatever
         * is driving the encoder) to the encoder thread.
         * <p>
         * The object is created on the encoder thread.
         */
        public static class EncoderHandler extends Handler {
            public static final int MSG_FRAME_AVAILABLE_SOON = 1;
            public static final int MSG_SAVE_VIDEO = 2;
            public static final int MSG_SHUTDOWN = 3;

            // This shouldn't need to be a weak ref, since we'll go away when the Looper quits,
            // but no real harm in it.
            private WeakReference<EncoderThread> mWeakEncoderThread;

            /**
             * Constructor. Instantiate object from encoder thread.
             */
            public EncoderHandler(EncoderThread et) {
                mWeakEncoderThread = new WeakReference<>(et);
            }

            @Override   // runs on encoder thread
            public void handleMessage(Message msg) {
                int what = msg.what;
                if (VERBOSE) {
                    Log.v(TAG, "EncoderHandler: what=" + what);
                }

                EncoderThread encoderThread = mWeakEncoderThread.get();
                if (encoderThread == null) {
                    Log.w(TAG, "EncoderHandler.handleMessage: weak ref is null");
                    return;
                }
                switch (what) {
                    case MSG_FRAME_AVAILABLE_SOON:
                        encoderThread.frameAvailableSoon();
                        break;
                    case MSG_SAVE_VIDEO:
                        encoderThread.saveVideo((File) msg.obj);
                        break;
                    case MSG_SHUTDOWN:
                        encoderThread.shutdown();
                        break;
                    default:
                        throw new RuntimeException("unknown message " + what);
                }
            }
        }
    }
}

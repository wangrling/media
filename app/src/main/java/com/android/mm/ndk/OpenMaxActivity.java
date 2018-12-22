package com.android.mm.ndk;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.mm.R;

import java.io.IOException;

import androidx.annotation.Nullable;

/**
 * 使用OpenMax或者MediaPlayer进行播放，使用SurfaceView或者TextureView进行显示。
 * SurfaceView只能固定大小，TextureView可以通过OpenGL进行调整。
 */

// 没有TS流解码器，只能使用MediaPlayer播放MP4视频。
// 12-23 00:17:33.640 10380 10380 V OpenMax : Initially queueing 80 packets
// 12-23 00:17:33.645   636 10681 D NuPlayer: onSetVideoSurface(0xecebf800, no video decoder)
// 12-23 00:17:33.646   636 10681 W NuPlayer: Unknown video size, reporting 0x0!
// 12-23 00:17:33.646   636 10681 D NuPlayerDriver: notifyListener_l(0xef320ae0), (5, 0, 0), loop setting(0, 0)
// 12-23 00:17:33.646   636 10681 D NuPlayerDriver: notifyListener_l(0xef320ae0), (200, 801, 0), loop setting(0, 0)
// 12-23 00:17:33.646   636 10681 D NuPlayerDriver: notifyListener_l(0xef320ae0), (1, 0, 0), loop setting(0, 0)
// 12-23 00:17:33.647   636   874 E MediaPlayerService: getDuration returned -2147483648
// 12-23 00:17:33.648   636   874 D NuPlayerDriver: start(0xef320ae0), state is 4, eos is 0
// 12-23 00:17:33.651   636 10681 E NuPlayer: no metadata for either audio or video source

public class OpenMaxActivity extends Activity {
    static final String TAG = "NativeMedia";

    String mSourceString = "mpeg_4_avc_aac_24fps.mp4";
    String mSinkString = null;

    // member variable for Java media player
    MediaPlayer mMediaPlayer;
    boolean mMediaPlayerIsPrepared = false;
    SurfaceView mSurfaceView1;
    SurfaceHolder mSurfaceHolder1;

    // member variables for native media player
    boolean mIsPlayingStreaming = false;
    SurfaceView mSurfaceView2;
    SurfaceHolder mSurfaceHolder2;

    private MyGLSurfaceView mGLView1, mGLView2;

    VideoSink mSelectedVideoSink;
    VideoSink mJavaMediaPlayerVideoSink;
    VideoSink mNativeMediaPlayerVideoSink;

    SurfaceHolderVideoSink mSurfaceHolder1VideoSink, mSurfaceHolder2VideoSink;
    GLViewVideoSink mGLView1VideoSink, mGLView2VideoSink;

    AssetManager assetManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_max_open);

        assetManager = getApplication().getAssets();

        mGLView1 = findViewById(R.id.glSurfaceView1);
        mGLView2 = findViewById(R.id.glSurfaceView2);

        // initialize native media system.
        createEngine();

        // set up the Surface 1 video sink.
        mSurfaceView1 = findViewById(R.id.surfaceView1);
        mSurfaceView2 = findViewById(R.id.surfaceView2);

        mSurfaceHolder1 = mSurfaceView1.getHolder();
        mSurfaceHolder2 = mSurfaceView2.getHolder();

        mSurfaceHolder1.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.v(TAG, "surfaceCreated");
                setSurface(holder.getSurface());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.v(TAG, "surfaceChanged format=" + format + ", width=" + width + ", height="
                        + height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.v(TAG, "surfaceDestroyed");
            }
        });

        mSurfaceHolder2.addCallback(new SurfaceHolder.Callback() {

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.v(TAG, "surfaceChanged format=" + format + ", width=" + width + ", height="
                        + height);
            }

            public void surfaceCreated(SurfaceHolder holder) {
                Log.v(TAG, "surfaceCreated");
                setSurface(holder.getSurface());
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.v(TAG, "surfaceDestroyed");
            }

        });

        // create Java media player
        mMediaPlayer = new MediaPlayer();

        // set up Java media player listeners.
        mMediaPlayer.setOnPreparedListener((mp) -> {
                int width = mp.getVideoWidth();
                int height = mp.getVideoHeight();

                Log.v(TAG, "onPrepared width=" + width + ", height=" + height);
                if (width != 0 && height != 0 && mJavaMediaPlayerVideoSink != null) {
                    mJavaMediaPlayerVideoSink.setFixedSize(width, height);
                }
                mMediaPlayerIsPrepared = true;

                mp.start();
                if (mp.isPlaying()) {
                    ((ImageView)findViewById(R.id.startJava)).setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_pause, null));
                } else {
                    ((ImageView)findViewById(R.id.startJava)).setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_play, null));
                }
        });

        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
                Log.v(TAG, "onVideoSizeChanged width=" + width + ", height=" + height);
                if (width != 0 && height != 0 && mJavaMediaPlayerVideoSink != null) {
                    mJavaMediaPlayerVideoSink.setFixedSize(width, height);
                }
            }

        });




        // initialize video sink spinner
        Spinner sinkSpinner = (Spinner) findViewById(R.id.sinkSpinner);
        ArrayAdapter<CharSequence> sinkAdapter = ArrayAdapter.createFromResource(
                this, R.array.sink_array, android.R.layout.simple_spinner_item);
        sinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sinkSpinner.setAdapter(sinkAdapter);
        sinkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mSinkString = parent.getItemAtPosition(pos).toString();
                Log.v(TAG, "onItemSelected " + mSinkString);
                if ("Surface 1".equals(mSinkString)) {
                    if (mSurfaceHolder1VideoSink == null) {
                        mSurfaceHolder1VideoSink = new SurfaceHolderVideoSink(mSurfaceHolder1);
                    }
                    mSelectedVideoSink = mSurfaceHolder1VideoSink;
                } else if ("Surface 2".equals(mSinkString)) {
                    if (mSurfaceHolder2VideoSink == null) {
                        mSurfaceHolder2VideoSink = new SurfaceHolderVideoSink(mSurfaceHolder2);
                    }
                    mSelectedVideoSink = mSurfaceHolder2VideoSink;
                } else if ("SurfaceTexture 1".equals(mSinkString)) {
                    if (mGLView1VideoSink == null) {
                        mGLView1VideoSink = new GLViewVideoSink(mGLView1);
                    }
                    mSelectedVideoSink = mGLView1VideoSink;
                } else if ("SurfaceTexture 2".equals(mSinkString)) {
                    if (mGLView2VideoSink == null) {
                        mGLView2VideoSink = new GLViewVideoSink(mGLView2);
                    }
                    mSelectedVideoSink = mGLView2VideoSink;
                }
            }

            public void onNothingSelected(AdapterView parent) {
                Log.v(TAG, "onNothingSelected");
                mSinkString = null;
                mSelectedVideoSink = null;
            }

        });

        // initialize button click handlers
        // Java MediaPlayer start/pause
        ((findViewById(R.id.startJava))).setOnClickListener((v) -> {
            if (mJavaMediaPlayerVideoSink == null) {
                if (mSelectedVideoSink == null) {
                    return ;
                }
                // 这里使用MediaPlayer进行播放。
                mSelectedVideoSink.useAsSinkForJava(mMediaPlayer);
                mJavaMediaPlayerVideoSink = mSelectedVideoSink;
            }
            if (!mMediaPlayerIsPrepared) {
                if (mSourceString != null) {
                    try {
                        AssetFileDescriptor clipFd = assetManager.openFd(mSourceString);
                        mMediaPlayer.setDataSource(clipFd.getFileDescriptor(),
                                clipFd.getStartOffset(),
                                clipFd.getLength());
                        clipFd.close();
                    } catch (IOException e) {
                        Log.e(TAG, "IOException " + e);
                    }
                    mMediaPlayer.prepareAsync();
                }
            } else if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                ((ImageView)findViewById(R.id.startJava)).setImageDrawable(
                        getResources().getDrawable(R.drawable.ic_play, null));
            } else {
                mMediaPlayer.start();
                ((ImageView)findViewById(R.id.startJava)).setImageDrawable(
                        getResources().getDrawable(R.drawable.ic_pause, null));
            }

        });

        // native MediaPlayer start/pause
        (findViewById(R.id.startNative)).setOnClickListener(new View.OnClickListener() {

            boolean created = false;
            public void onClick(View view) {
                if (!created) {
                    if (mNativeMediaPlayerVideoSink == null) {
                        if (mSelectedVideoSink == null) {
                            return;
                        }
                        mSelectedVideoSink.useAsSinkForNative();
                        mNativeMediaPlayerVideoSink = mSelectedVideoSink;
                    }
                    if (mSourceString != null) {
                        created = createStreamingMediaPlayer(assetManager, mSourceString);
                    }
                }
                if (created) {
                    mIsPlayingStreaming = !mIsPlayingStreaming;
                    setPlayingStreamingMediaPlayer(mIsPlayingStreaming);
                    if (mIsPlayingStreaming) {
                        ((ImageView)findViewById(R.id.startNative)).setImageDrawable(
                                getResources().getDrawable(R.drawable.ic_pause, null));
                    } else {
                        ((ImageView)findViewById(R.id.startNative)).setImageDrawable(
                                getResources().getDrawable(R.drawable.ic_play, null));
                    }
                }
            }

        });

        // finish
        (findViewById(R.id.finish)).setOnClickListener((v) -> {
            finish();
        });

        // Java MediaPlayer rewind
        (findViewById(R.id.rewindJava)).setOnClickListener((v) -> {
            if (mMediaPlayerIsPrepared) {
                mMediaPlayer.seekTo(0);
            }
        });

        // native MediaPlayer rewind
        (findViewById(R.id.rewindNative)).setOnClickListener((v) -> {
            if (mNativeMediaPlayerVideoSink != null) {
                rewindStreamingMediaPlayer();
            }
        });
    }

    @Override
    protected void onPause() {
        mIsPlayingStreaming = false;
        setPlayingStreamingMediaPlayer(false);
        mGLView1.onPause();
        mGLView2.onPause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView1.onResume();
        mGLView2.onResume();
    }

    @Override
    protected void onDestroy()
    {
        shutdown();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }

    // VideoSink abstracts out the difference between Surface and SurfaceTexture
    // aka SurfaceHolder and GLSurfaceView
    static abstract class VideoSink {
        abstract void setFixedSize(int width, int height);
        abstract void useAsSinkForJava(MediaPlayer mediaPlayer);
        abstract void useAsSinkForNative();
    }

    static class SurfaceHolderVideoSink extends VideoSink {

        private final SurfaceHolder mSurfaceHolder;

        SurfaceHolderVideoSink(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        @Override
        void setFixedSize(int width, int height) {
            mSurfaceHolder.setFixedSize(width, height);
        }

        @Override
        void useAsSinkForJava(MediaPlayer mediaPlayer) {
            // Use the newer MediaPlayer.setSurface(Surface) since API level 14
            // instead of MediaPlayer.setDisplay(mSurfaceHolder) since API level 1,
            // because setSurface also works with a Surface derived from a SurfaceTexture.
            Surface s = mSurfaceHolder.getSurface();
            mediaPlayer.setSurface(s);
            s.release();
        }

        @Override
        void useAsSinkForNative() {
            Surface s = mSurfaceHolder.getSurface();
            setSurface(s);
            s.release();
        }
    }

    static class GLViewVideoSink extends VideoSink {
        private final MyGLSurfaceView myGLSurfaceView;

        GLViewVideoSink(MyGLSurfaceView glSurfaceView) {
            myGLSurfaceView = glSurfaceView;
        }

        @Override
        void setFixedSize(int width, int height) {

        }

        @Override
        void useAsSinkForJava(MediaPlayer mediaPlayer) {
            SurfaceTexture st = myGLSurfaceView.getSurfaceTexture();
            Surface s = new Surface(st);
            mediaPlayer.setSurface(s);
            s.release();
        }

        @Override
        void useAsSinkForNative() {
            SurfaceTexture st = myGLSurfaceView.getSurfaceTexture();
            Surface s = new Surface(st);
            setSurface(s);
            s.release();
        }
    }

    static {
        System.loadLibrary("androidndk");
    }

    public static native void createEngine();
    public static native boolean createStreamingMediaPlayer(AssetManager assetManager,
                                                            String filename);
    public static native void setPlayingStreamingMediaPlayer(boolean isPlaying);
    public static native void shutdown();
    public static native void setSurface(Surface surface);
    public static native void rewindStreamingMediaPlayer();
}

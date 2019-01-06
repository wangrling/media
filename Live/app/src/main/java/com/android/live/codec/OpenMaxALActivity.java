package com.android.live.codec;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.live.R;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OpenMaxALActivity extends AppCompatActivity {

    private final String TAG = "OpenMaxAL";

    // 输入
    String mJavaSourceString = "mpeg4_avc_aac_24fps.mp4";

    String mNativeSourceString = "mpeg2_avc_aac.ts";

    // member variables for java media player
    MediaPlayer mMediaPlayer;
    boolean mMediaPlayerIsPrepared = false;

    // member variables for native media player
    boolean mIsPlayingStreaming = false;


    VideoSink mSelectVideoSink;
    // 对Holder的封装
    VideoSink mJavaMediaPlayerVideoSink;
    VideoSink mNativeMediaPlayerVideoSink;

    // SurfaceView相关变量。
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    SurfaceHolderVideoSink mSurfaceHolderVideoSink;

    // GLSurfaceView相关变量。
    MyGLSurfaceView mGLView;
    SurfaceHolder mGLViewSurfaceHolder;
    GLViewVideoSink mGLViewVideoSink;

    AssetManager assetManager;

    boolean engineCreated = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_openmax_al);

        assetManager = getApplication().getAssets();


        // initialize native media system
        createEngine();

        mSurfaceView = findViewById(R.id.surfaceView);
        mGLView = findViewById(R.id.glSurfaceView);

        mSurfaceHolder = mSurfaceView.getHolder();
        mGLViewSurfaceHolder = mGLView.getHolder();

        // Create Java media player.
        mMediaPlayer = new MediaPlayer();

        // Set up Java media player listeners.
        mMediaPlayer.setOnPreparedListener((mp) -> {
                    int width = mp.getVideoWidth();
                    int height = mp.getVideoHeight();
                    Log.v(TAG, "onPrepared width=" + width + ", height=" + height);
                    if (width != 0 && height != 0 && mJavaMediaPlayerVideoSink != null) {
                        mJavaMediaPlayerVideoSink.setFixedSize(width, height);
                    }
                    mMediaPlayerIsPrepared = true;
                    mp.start();
                    ((ImageButton) findViewById(R.id.startJava)).setImageDrawable(getDrawable(R.drawable.ic_pause));
            ((ImageButton) findViewById(R.id.startNative)).setEnabled(false);
        });

        mMediaPlayer.setOnCompletionListener((mp) -> {
            ((ImageButton) findViewById(R.id.startJava)).setImageDrawable(getDrawable(R.drawable.ic_play_arrow));
        });


        ((ImageButton) findViewById(R.id.startJava)).setOnClickListener((v) -> {
            if (mJavaMediaPlayerVideoSink == null) {
                mSelectVideoSink.useAsSinkForJava(mMediaPlayer);
                mJavaMediaPlayerVideoSink = mSelectVideoSink;
            }
            if (!mMediaPlayerIsPrepared) {
                if (mJavaSourceString != null) {
                    try {
                        AssetFileDescriptor clipFd = assetManager.openFd(mJavaSourceString);
                        mMediaPlayer.setDataSource(clipFd.getFileDescriptor(),
                                clipFd.getStartOffset(), clipFd.getLength());
                        clipFd.close();

                    } catch (IOException e) {
                        Log.e(TAG, "IOException");
                        e.printStackTrace();
                    }
                    mMediaPlayer.prepareAsync();
                }
            } else if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                ((ImageButton) v).setImageDrawable(getDrawable(R.drawable.ic_play_arrow));
            } else {
                mMediaPlayer.start();
                ((ImageButton) v).setImageDrawable(getDrawable(R.drawable.ic_pause));
                ((ImageButton) findViewById(R.id.startNative)).setEnabled(false);
            }
        });


        ((ImageButton) findViewById(R.id.rewindJava)).setOnClickListener((v) -> {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.seekTo(0);
                mMediaPlayer.start();
            }
        });

        ((ImageButton) findViewById(R.id.startNative)).setOnClickListener((v) -> {
            if (!engineCreated) {

                if (mNativeMediaPlayerVideoSink == null) {
                    if (mSelectVideoSink == null)
                        return;
                }
                // 都是在java创建的两个Surface
                mSelectVideoSink.useAsSinkForNative();
                mNativeMediaPlayerVideoSink = mSelectVideoSink;

                if (mNativeSourceString != null) {
                    // 不能传入空值
                    engineCreated = createStreamingMediaPlayer(assetManager, mNativeSourceString);
                }
            }

            if (engineCreated) {
                mIsPlayingStreaming = !mIsPlayingStreaming;
                setPlayingStreamMediaPlayer(mIsPlayingStreaming);
                if (mIsPlayingStreaming)
                    ((ImageButton) v).setImageDrawable(getDrawable(R.drawable.ic_pause));
                else
                    ((ImageButton) v).setImageDrawable(getDrawable(R.drawable.ic_play_arrow));
                ((ImageButton) findViewById(R.id.startJava)).setEnabled(false);
            }

        });


        ((RadioGroup) findViewById(R.id.viewRadioGroup)).setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.surfaceViewRadioButton: {
                    if (mJavaMediaPlayerVideoSink != null)
                        return;
                    mSelectVideoSink = new SurfaceHolderVideoSink(mSurfaceHolder);
                    break;
                }
                case R.id.glSurfaceViewRadioButton: {
                    if (mNativeMediaPlayerVideoSink != null)
                        return;

                    mSelectVideoSink = new GLViewVideoSink(mGLView);
                    break;
                }
            }
        });

        // 设置默认为SurfaceView播放
        ((RadioButton) findViewById(R.id.surfaceViewRadioButton)).toggle();
        mSelectVideoSink = new SurfaceHolderVideoSink(mSurfaceHolder);
    }

    @Override
    protected void onPause() {

        mIsPlayingStreaming = false;
        setPlayingStreamMediaPlayer(false);
        mGLView.onPause();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
        }

        shutdown();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    /**
     * Native methods
     */
    public static native void createEngine();

    // 创建流播放器。
    public static native boolean createStreamingMediaPlayer(AssetManager assetManager, String filename);

    // 播放和暂停控制。
    public static native void setPlayingStreamMediaPlayer(boolean isPlaying);

    // 关闭。
    public static native void shutdown();

    // 设置播放界面。
    public static native void setSurface(Surface surface);

    // 重新开始播放。
    public static native void rewindStreamingMediaPlayer();

    /** Load .so on initialization */
    static {
        System.loadLibrary("codec");
    }

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
            // 传递到底层
            setSurface(s);
            s.release();
        }
    }

    static class GLViewVideoSink extends VideoSink {

        private final MyGLSurfaceView mMyGLSurfaceView;

        GLViewVideoSink(MyGLSurfaceView myGLSurfaceView) {
            mMyGLSurfaceView = myGLSurfaceView;
        }

        @Override
        void setFixedSize(int width, int height) {
            // nothing to do.
        }

        @Override
        void useAsSinkForJava(MediaPlayer mediaPlayer) {
            SurfaceTexture st = mMyGLSurfaceView.getSurfaceTexture();
            Surface s = new Surface(st);
            mediaPlayer.setSurface(s);
            s.release();
        }

        @Override
        void useAsSinkForNative() {
            SurfaceTexture st = mMyGLSurfaceView.getSurfaceTexture();
            Surface s = new Surface(st);
            setSurface(s);
            s.release();
        }
    }
}

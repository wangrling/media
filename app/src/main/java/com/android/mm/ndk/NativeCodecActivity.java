package com.android.mm.ndk;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.android.mm.R;

import androidx.annotation.Nullable;

public class NativeCodecActivity extends Activity {

    static {
        System.loadLibrary("androidndk");
    }

    static final String TAG = "NativeCodec";

    String mSourceString = "mpeg_4_avc_aac_24fps.mp4";

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    // SurfaceHolder的包裹类。
    VideoSink mSurfaceHolderVideoSink;

    GLViewVideoSink mGLViewVideoSink;
    MyGLSurfaceView mGLView;


    // 最终的目的是VideoSink，也是就是画布。
    VideoSink mSelectedVideoSink;
    // 本地正在使用的画布
    VideoSink mNativeCodecPlayerVideoSink;

    private RadioButton mSurfaceViewRadio;
    private RadioButton mGLSurfaceViewRadio;


    // 视频源是否已经创建
    boolean mCreated = false;

    boolean mIsPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_codec_native);

        mGLView = findViewById(R.id.glSurfaceView);

        // set up the Surface video sink
        mSurfaceView = findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.v(TAG, "surfaceCreated");
                if (mSurfaceViewRadio.isChecked()) {
                    setSurface(holder.getSurface());
                }
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

        mSurfaceViewRadio = findViewById(R.id.radioSurfaceView);
        mGLSurfaceViewRadio = findViewById(R.id.radioGLSurfaceView);

        CompoundButton.OnCheckedChangeListener checkListener = (buttonView, isChecked) -> {

                if (buttonView == mSurfaceViewRadio && isChecked) {
                    mGLSurfaceViewRadio.setChecked(false);
                    Log.d(TAG, "surface view toggle");
                }
                if (buttonView == mGLSurfaceViewRadio && isChecked) {
                    mSurfaceViewRadio.setChecked(false);
                    Log.d(TAG, "gl surface view toggle");
                }
                if (isChecked) {
                    if (mSurfaceViewRadio.isChecked()) {
                        if (mSurfaceHolderVideoSink == null) {
                            mSurfaceHolderVideoSink = new SurfaceHolderVideoSink(mSurfaceHolder);
                        }
                        mSelectedVideoSink = mSurfaceHolderVideoSink;
                        mGLView.onPause();
                        Log.d(TAG, "gl view pause");
                    } else {
                        mGLView.onResume();
                        Log.d(TAG, "gl view resume");
                        if (mGLViewVideoSink == null) {
                            mGLViewVideoSink = new GLViewVideoSink(mGLView);
                        }
                        mSelectedVideoSink = mGLViewVideoSink;
                    }
                    switchSurface();
                }
        };
        mSurfaceViewRadio.setOnCheckedChangeListener(checkListener);
        mGLSurfaceViewRadio.setOnCheckedChangeListener(checkListener);
        // 默认
        mGLSurfaceViewRadio.toggle();
        // mSurfaceViewRadio.toggle();

        // The surfaces themselves are easier targets than the radio buttons.
        mSurfaceView.setOnClickListener((v)-> {
            mSurfaceViewRadio.toggle(); });
        mGLView.setOnClickListener((v) -> {
            mGLSurfaceViewRadio.toggle(); });

        // initialize button click handlers.
        // native MediaPlayer start/pause
        (findViewById(R.id.startNative)).setOnClickListener((v) -> {
            if (!mCreated) {
                if (mNativeCodecPlayerVideoSink == null) {
                    if(mSelectedVideoSink == null) {

                        return ;
                    }
                    mSelectedVideoSink.useAsSinkForNative();
                    mNativeCodecPlayerVideoSink = mSelectedVideoSink;
                }
                if (mSourceString != null) {
                    mCreated = createStreamingMediaPlayer(getResources().getAssets(), mSourceString);
                }
            }

            if(mCreated) {
                mIsPlaying = !mIsPlaying;

                setPlayingStreamingMediaPlayer(mIsPlaying);
            }
        });

        // native MediaPlayer rewind.
        (findViewById(R.id.rewindNative)).setOnClickListener((v) -> {
            if (mNativeCodecPlayerVideoSink != null) {
                rewindStreamingMediaPlayer();
            }
        });
    }

    void switchSurface() {
        Log.d(TAG, "media player is created " + mCreated);
        if (mCreated && mNativeCodecPlayerVideoSink != mSelectedVideoSink) {
            // shutdown and recreate on other surface
            Log.i(TAG, "shutting down player");
            shutdown();
            mCreated = false;
            mSelectedVideoSink.useAsSinkForNative();
            mNativeCodecPlayerVideoSink = mSelectedVideoSink;
            if (mSourceString != null) {
                Log.i(TAG, "recreating player");
                mCreated = createStreamingMediaPlayer(getResources().getAssets(), mSourceString);
                mIsPlaying = false;
            }
        }
    }

    // VideoSink abstracts out the difference between Surface and SurfaceTexture.
    // aka SurfaceHolder as GLSurfaceView.
    static abstract class VideoSink {

        abstract void setFixedSize(int width, int height);
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
        void useAsSinkForNative() {
            Surface s = mSurfaceHolder.getSurface();
            Log.i(TAG, "setting surface " + s);
            setSurface(s);
        }
    }

    static class GLViewVideoSink extends VideoSink {
        private final MyGLSurfaceView mGLSurfaceView;

        GLViewVideoSink(MyGLSurfaceView glSurfaceView) {
            mGLSurfaceView = glSurfaceView;
        }
        @Override
        void setFixedSize(int width, int height) {

        }

        @Override
        void useAsSinkForNative() {
            // 传递对应的TextureID值。
            SurfaceTexture st = mGLSurfaceView.getSurfaceTexture();
            Surface s = new Surface(st);
            setSurface(s);
            s.release();
        }
    }

    public static native boolean createStreamingMediaPlayer(AssetManager assetMgr, String filename);
    public static native void setPlayingStreamingMediaPlayer(boolean isPlaying);
    public static native void shutdown();
    public static native void setSurface(Surface surface);
    public static native void rewindStreamingMediaPlayer();
}

package com.android.mm.ndk;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.mm.R;

import androidx.annotation.Nullable;

public class NativeCodecActivity extends Activity {

    static {
        System.loadLibrary("androidndk");
    }

    static final String TAG = "NativeCodec";

    String mSourceString = null;

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    private CodecGLSurfaceView mGLSurfaceView;

    private RadioButton mSurfaceViewRadio;
    private RadioButton mGLSurfaceViewRadio;

    VideoSink mSelectedVideoSink;
    VideoSink mSurfaceHolderVideoSink;
    VideoSink mGLViewVideoSink;

    // 视频源是否已经创建
    boolean mCreated = false;

    boolean mIsPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_codec_native);

        mGLSurfaceView = findViewById(R.id.glSurfaceView);

        // set up the Surface video sink
        mSurfaceView = findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.v(TAG, "SurfaceView surfaceCreated");
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

        // initialize content source spinner
        Spinner sourceSpinner = (Spinner) findViewById(R.id.sourceSpinner);
        ArrayAdapter<CharSequence> sourceAdapter = ArrayAdapter.createFromResource(
                this, R.array.sourceVideo, android.R.layout.simple_spinner_item);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(sourceAdapter);
        sourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mSourceString = parent.getItemAtPosition(pos).toString();
                Log.v(TAG, "onItemSelected " + mSourceString);
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
                Log.v(TAG, "onNothingSelected");
                mSourceString = null;
            }
        });

        mSurfaceViewRadio = findViewById(R.id.radioSurfaceView);
        mGLSurfaceViewRadio = findViewById(R.id.radioGLSurfaceView);

        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == mSurfaceViewRadio && isChecked) {
                    mGLSurfaceViewRadio.setChecked(false);
                }
                if (buttonView == mGLSurfaceViewRadio && isChecked) {
                    mSurfaceViewRadio.setChecked(false);
                }
                if (isChecked) {
                    if (mSurfaceViewRadio.isChecked()) {
                        if (mSurfaceHolderVideoSink == null) {
                            mSurfaceHolderVideoSink = new SurfaceHolderVideoSink(mSurfaceHolder);
                        }
                        mSelectedVideoSink = mSurfaceHolderVideoSink;
                        mGLSurfaceView.onPause();
                        Log.d(TAG, "gl view pause");
                    } else {
                        mGLSurfaceView.onResume();
                        if (mGLViewVideoSink == null) {
                            mGLViewVideoSink = new GLViewVideoSink(mGLSurfaceView);
                        }
                        mSelectedVideoSink = mGLViewVideoSink;
                    }
                    switchSurface();
                }
            }
        };
        mSurfaceViewRadio.setOnCheckedChangeListener(checkListener);
        mGLSurfaceViewRadio.setOnCheckedChangeListener(checkListener);
        // 默认
        mGLSurfaceViewRadio.toggle();

        // The surfaces themselves are easier targets than the radio buttons.
        mSurfaceView.setOnClickListener((v)-> {
            mSurfaceViewRadio.toggle(); });
        mGLSurfaceView.setOnClickListener((v) -> {
            mGLSurfaceViewRadio.toggle(); });

        // native MediaPlayer start/pause
        ((Button) findViewById(R.id.startNative)).setOnClickListener((v) -> {
            if (!mCreated) {
                if (mGLViewVideoSink == null) {
                    if(mSelectedVideoSink == null) {
                        return ;
                    }
                    mSelectedVideoSink.useAsSinkForNative();
                    mGLViewVideoSink = mSelectedVideoSink;
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
        ((Button) findViewById(R.id.rewindNative)).setOnClickListener((v) -> {
            if (mGLViewVideoSink != null) {
                rewindStreamingMediaPlayer();
            }
        });
    }

    void switchSurface() {
        if (mCreated && mGLViewVideoSink != mSelectedVideoSink) {
            // shutdown and recreate on other surface
            Log.i(TAG, "shutting down player");
            shutdown();
            mCreated = false;
            mSelectedVideoSink.useAsSinkForNative();
            mGLViewVideoSink = mSelectedVideoSink;
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
        private final CodecGLSurfaceView codecGLSurfaceView;

        GLViewVideoSink(CodecGLSurfaceView glSurfaceView) {
            codecGLSurfaceView = glSurfaceView;
        }
        @Override
        void setFixedSize(int width, int height) {

        }

        @Override
        void useAsSinkForNative() {
            SurfaceTexture st =codecGLSurfaceView.getSurfaceTexture();
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

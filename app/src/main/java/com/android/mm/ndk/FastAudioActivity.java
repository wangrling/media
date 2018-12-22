package com.android.mm.ndk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.mm.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class FastAudioActivity extends Activity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int AUDIO_ECHO_REQUEST = 0;

    private Button controlButton;
    private TextView statusView;
    private String  nativeSampleRate;
    private String  nativeSampleBufSize;

    private SeekBar delaySeekBar;
    private TextView curDelayTV;
    private int echoDelayProgress;

    private SeekBar decaySeekBar;
    private TextView curDecayTV;
    private float echoDecayProgress;

    private boolean supportRecording;
    private Boolean isPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_fast);

        controlButton = (Button)findViewById((R.id.captureControlButton));
        statusView = (TextView)findViewById(R.id.statusView);
        queryNativeAudioParameters();

        delaySeekBar = (SeekBar)findViewById(R.id.delaySeekBar);
        curDelayTV = (TextView)findViewById(R.id.curDelay);
        echoDelayProgress = delaySeekBar.getProgress() * 1000 / delaySeekBar.getMax();
        delaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float curVal = (float)progress / delaySeekBar.getMax();
                curDelayTV.setText(String.format("%s", curVal));
                setSeekBarPromptPosition(delaySeekBar, curDelayTV);
                if (!fromUser) return;

                echoDelayProgress = progress * 1000 / delaySeekBar.getMax();
                configureEcho(echoDelayProgress, echoDecayProgress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        delaySeekBar.post(new Runnable() {
            @Override
            public void run() {
                setSeekBarPromptPosition(delaySeekBar, curDelayTV);
            }
        });

        decaySeekBar = (SeekBar)findViewById(R.id.decaySeekBar);
        curDecayTV = (TextView)findViewById(R.id.curDecay);
        echoDecayProgress = (float)decaySeekBar.getProgress() / decaySeekBar.getMax();
        decaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float curVal = (float)progress / seekBar.getMax();
                curDecayTV.setText(String.format("%s", curVal));
                setSeekBarPromptPosition(decaySeekBar, curDecayTV);
                if (!fromUser)
                    return;

                echoDecayProgress = curVal;
                configureEcho(echoDelayProgress, echoDecayProgress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        decaySeekBar.post(new Runnable() {
            @Override
            public void run() {
                setSeekBarPromptPosition(decaySeekBar, curDecayTV);
            }
        });

        // initialize native audio system
        updateNativeAudioUI();

        if (supportRecording) {
            createSLEngine(
                    Integer.parseInt(nativeSampleRate),
                    Integer.parseInt(nativeSampleBufSize),
                    echoDelayProgress,
                    echoDecayProgress);
        }
    }

    private void queryNativeAudioParameters() {
        supportRecording = true;
        AudioManager myAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(myAudioMgr == null) {
            supportRecording = false;
            return;
        }
        nativeSampleRate  =  myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        nativeSampleBufSize =myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);

        // hardcoded channel to mono: both sides -- C++ and Java sides
        int recBufSize = AudioRecord.getMinBufferSize(
                Integer.parseInt(nativeSampleRate),
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        if (recBufSize == AudioRecord.ERROR ||
                recBufSize == AudioRecord.ERROR_BAD_VALUE) {
            supportRecording = false;
        }

    }

    private void setSeekBarPromptPosition(SeekBar seekBar, TextView label) {
        float thumbX = (float)seekBar.getProgress()/ seekBar.getMax() *
                seekBar.getWidth() + seekBar.getX();
        label.setX(thumbX - label.getWidth()/2.0f);
    }

    @Override
    protected void onDestroy() {
        if (supportRecording) {
            if (isPlaying) {
                stopPlay();
            }
            deleteSLEngine();
            isPlaying = false;
        }
        super.onDestroy();
    }

    // button click
    public void onEchoClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { Manifest.permission.RECORD_AUDIO },
                    AUDIO_ECHO_REQUEST);
            return;
        }
        startEcho();
    }

    private void startEcho() {
        if(!supportRecording){
            return;
        }
        if (!isPlaying) {
            if(!createSLBufferQueueAudioPlayer()) {
                statusView.setText(getString(R.string.player_error_msg));
                return;
            }
            if(!createAudioRecorder()) {
                deleteSLBufferQueueAudioPlayer();
                statusView.setText(getString(R.string.recorder_error_msg));
                return;
            }
            startPlay();   // startPlay() triggers startRecording()
            statusView.setText(getString(R.string.echoing_status_msg));
        } else {
            stopPlay();  // stopPlay() triggers stopRecording()
            updateNativeAudioUI();
            deleteAudioRecorder();
            deleteSLBufferQueueAudioPlayer();
        }
        isPlaying = !isPlaying;
        controlButton.setText(getString(isPlaying ?
                R.string.cmd_stop_echo: R.string.cmd_start_echo));
    }

    private void updateNativeAudioUI() {
        if (!supportRecording) {
            statusView.setText(getString(R.string.mic_error_msg));
            controlButton.setEnabled(false);
            return;
        }

        statusView.setText(getString(R.string.fast_audio_info_msg,
                nativeSampleRate, nativeSampleBufSize));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        /*
         * if any permission failed, the sample could not play
         */
        if (AUDIO_ECHO_REQUEST != requestCode) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 1  ||
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            /*
             * When user denied permission, throw a Toast to prompt that RECORD_AUDIO
             * is necessary; also display the status on UI
             * Then application goes back to the original state: it behaves as if the button
             * was not clicked. The assumption is that user will re-click the "start" button
             * (to retry), or shutdown the app in normal way.
             */
            return;
        }
        // The callback runs on app's thread, so we are safe to resume the action
        startEcho();
    }

    static {
        System.loadLibrary("androidndk");
    }
    static native void createSLEngine(int rate, int framesPerBuf,
                                      long delayInMs, float decay);
    static native void deleteSLEngine();
    static native boolean configureEcho(int delayInMs, float decay);
    static native boolean createSLBufferQueueAudioPlayer();
    static native void deleteSLBufferQueueAudioPlayer();

    static native boolean createAudioRecorder();
    static native void deleteAudioRecorder();
    static native void startPlay();
    static native void stopPlay();
}

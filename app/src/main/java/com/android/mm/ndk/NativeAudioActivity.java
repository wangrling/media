package com.android.mm.ndk;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;

import com.android.mm.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

// 只实现本地播放，回放和录制都没有实现。
public class NativeAudioActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    static boolean isPlaying;
    static {
        System.loadLibrary("androidndk");
        isPlaying = false;
    }
    private static final int AUDIO_ECHO_REQUEST = 0;
    static AssetManager assetManager;

    static String URI = "mpeg_44.1kz_2_356kb.mp3";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_native);

        assetManager = getAssets();

        // initialize native audio system.
        createEngine();

        (findViewById(R.id.play)).setOnClickListener((v) -> {
            if (isPlaying)
                return ;
            boolean created = createAssetAudioPlayer(assetManager, URI);
            if (created) {
                setPlayingAssetAudioPlayer(true);
                isPlaying = true;
            }
        });

        (findViewById(R.id.pause)).setOnClickListener((v) -> {
            if (isPlaying) {
                setPlayingAssetAudioPlayer(false);
                isPlaying = false;
            }
        });
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
             * When user denied the permission, throw a Toast to prompt that RECORD_AUDIO
             * is necessary; on UI, we display the current status as permission was denied so
             * user know what is going on.
             * This application go back to the original state: it behaves as if the button
             * was not clicked. The assumption is that user will re-click the "start" button
             * (to retry), or shutdown the app in normal way.
             */
            return;
        }

        // The callback runs on app's thread, so we are safe to resume the action
        recordAudio();
    }

    // Single out recording for run-permission needs.
    static boolean created = false;
    private void recordAudio() {
        if (!created) {
            created = createAudioRecorder();
        }
        if (created) {
            startRecording();
        }
    }

    @Override
    protected void onPause() {

        // turn off all audio.
        setPlayingAssetAudioPlayer(false);

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        shutdown();
        super.onDestroy();
    }

    public static native void createEngine();
    public static native boolean createAssetAudioPlayer(AssetManager assetManager, String filename);
    public static native void setPlayingAssetAudioPlayer(boolean isPlaying);
    public static native boolean createAudioRecorder();
    public static native void startRecording();
    public static native void shutdown();
}

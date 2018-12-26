package com.android.mm.libgdx.android;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import com.android.mm.libgdx.gdx.Audio;
import com.android.mm.libgdx.gdx.audio.AudioDevice;
import com.android.mm.libgdx.gdx.audio.AudioRecorder;
import com.android.mm.libgdx.gdx.audio.Music;
import com.android.mm.libgdx.gdx.audio.Sound;
import com.android.mm.libgdx.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;


public final class AndroidAudio implements Audio {

    private final SoundPool soundPool;
    private final AudioManager manager;

    protected final List<AndroidMusic> musics = new ArrayList<AndroidMusic>();

    public AndroidAudio(Context context, AndroidApplicationConfiguration config) {
        if (!config.disableAudio) {
            AudioAttributes audioAttrib = new AudioAttributes
                    .Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib)
                    // 最大同时播放数目
                    .setMaxStreams(config.maxSimultaneousSounds).build();
        } else {
            soundPool = null;
        }

        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (context instanceof Activity) {
            ((Activity)context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }



    @Override
    public AudioDevice newAudioDevice(int samplingRate, boolean isMono) {
        return null;
    }

    @Override
    public AudioRecorder newAudioRecorder(int sampleRate, boolean isMono) {
        return null;
    }

    @Override
    public Sound newSound(FileHandle fileHandle) {
        return null;
    }

    @Override
    public Music newMusic(FileHandle file) {
        return null;
    }
}

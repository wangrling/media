package com.android.mm.libgdx.android;

import com.android.mm.libgdx.gdx.ApplicationListener;
import com.android.mm.libgdx.gdx.Audio;
import com.android.mm.libgdx.gdx.audio.AudioDevice;
import com.android.mm.libgdx.gdx.audio.AudioRecorder;
import com.android.mm.libgdx.gdx.audio.Music;
import com.android.mm.libgdx.gdx.audio.Sound;
import com.android.mm.libgdx.gdx.files.FileHandle;


public final class AndroidAudio implements Audio {


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

package com.android.mm.sonic;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioDevice {
    AudioTrack track;

    private int findFormatFromChannels(int numChannels) {
        switch (numChannels) {
            case 1:
                return AudioFormat.CHANNEL_OUT_MONO;
            case 2:
                return AudioFormat.CHANNEL_OUT_STEREO;
            default:
                return -1;  // Error;
        }
    }

    public AudioDevice(int sampleRate, int numChannels) {
        int format = findFormatFromChannels(numChannels);

        int minSize = AudioTrack.getMinBufferSize(sampleRate, format, AudioFormat.ENCODING_PCM_16BIT);
        track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, format,
                AudioFormat.ENCODING_PCM_16BIT, minSize * 4, AudioTrack.MODE_STREAM);
        track.play();
    }

    public void flush() {
        track.flush();
    }

    public void writeSamples(byte[] samples, int length) {
        track.write(samples, 0, length);
    }
}

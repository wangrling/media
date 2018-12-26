package com.android.mm.libgdx.android;

import android.media.MediaPlayer;

import com.android.mm.libgdx.gdx.audio.Music;

public class AndroidMusic implements Music, MediaPlayer.OnCompletionListener {
    private final AndroidAudio audio;
    private MediaPlayer player;
    private boolean isPrepared = true;
    protected boolean wasPlaying = false;
    private float volume = 1f;
    protected OnCompletionListener onCompletionListener;

    AndroidMusic(AndroidAudio audio, MediaPlayer player) {
        this.audio = audio;
        this.player = player;
        this.onCompletionListener = null;
        // 设置播放完成的监听。
        this.player.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void setLooping(boolean isLooping) {

    }

    @Override
    public boolean isLooping() {
        return false;
    }

    @Override
    public void setVolume(float volume) {

    }

    @Override
    public float getVolume() {
        return 0;
    }

    @Override
    public void setPan(float pan, float volume) {

    }

    @Override
    public void setPosition(float position) {

    }

    @Override
    public float getPosition() {
        return 0;
    }

    @Override
    public void dispose() {
        if (player == null)
            return ;
        try {
            player.release();
        } catch (Throwable t) {
        }
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {

    }
}

package com.google.android.camera;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;

import java.nio.channels.IllegalChannelGroupException;

/**
 * Loads a plays custom sounds. For playing system-standard sounds for avrious
 * camera actions, please refer to {@link SoundClips}.
 */
public class SoundPlayer {

    private final Context mAppContext;

    private final SoundPool mSoundPool;

    /** Keeps a mapping from sound resource ID to sound ID. */
    private final SparseArray mResourceToSoundId = new SparseArray();
    private boolean mIsReleased = false;

    public SoundPlayer(Context context) {
        mAppContext = context;
        final int audioType = getAudioTypeForSoundPool();
        mSoundPool  = new SoundPool(1, audioType, 0);
    }

    private static int getAudioTypeForSoundPool() {
        // 使用音乐流
        return AudioManager.STREAM_MUSIC;
    }

    public void loadSound(int resourceId) {
        int soundId = mSoundPool.load(mAppContext, resourceId, 1);
        mResourceToSoundId.put(resourceId, soundId);
    }

    // Play the sound with the given resource. The resource has to be loaded before it can be
    // played, otherwise an exception will be thrown.
    public void play(int resourceId, float volume) {
        Integer soundId = (Integer) mResourceToSoundId.get(resourceId);
        if (soundId == null) {
            throw  new IllegalStateException("Sound not loaded. Must call #loadSound first.");
        }
        mSoundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    // Upload the given sound if it's not needed anymore to release memory.
    public void unloadSound(int resourceId) {
        Integer soundId = (Integer) mResourceToSoundId.get(resourceId);
        if (soundId == null)
            throw  new IllegalStateException("Sound not loaded. Must call #loadSound first.");
        mSoundPool.unload(soundId);
    }

    /**
     * Call this if you don't need the SoundPlayer anymore. All memory will be released and the
     * object cannot be re-used.
     */
    public void release() {
        mIsReleased = true;
        mSoundPool.release();
    }

    public boolean isReleased() {
        return mIsReleased;
    }
}

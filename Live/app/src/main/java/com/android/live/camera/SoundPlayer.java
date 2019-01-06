package com.android.live.camera;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;

import com.android.live.camera.util.ApiHelper;

/**
 * Loads a plays custom sounds. For playing system-standard sounds for various
 * camera actions, please refer to {@link SoundClips}.
 */

public class SoundPlayer {
    private final Context mAppContext;
    private final SoundPool mSoundPool;

    /** Keeps a mapping from sound resource ID to sound ID */
    private final SparseArray mResourceToSoundId = new SparseArray();
    private boolean mIsReleased = false;

    /**
     * Construct a new sound player.
     */
    public SoundPlayer(Context appContext) {
        mAppContext = appContext;
        final int audioType = getAudioTypeForSoundPool();
        mSoundPool = new SoundPool(1 /* max streams */, audioType, 0 /* quality */);
    }

    /**
     * Load the sound from a resource.
     */
    public void loadSound(int resourceId) {
        int soundId = mSoundPool.load(mAppContext, resourceId, 1);
        mResourceToSoundId.put(resourceId, soundId);
    }

    /**
     * Play the sound with the given resource. The resource has to be loaded
     * before it can be played, otherwise an exception will be thrown.
     */
    public void play(int resourceId, float volume) {
        Integer soundId = (Integer) mResourceToSoundId.get(resourceId);
        if (soundId == null) {
            throw new IllegalStateException("Sound not loaded. Must call #loadSound first");
        }
        mSoundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    public void unloadSound(int resouceId) {
        Integer soundId = (Integer) mResourceToSoundId.get(resouceId);
        if (soundId == null) {
            throw new IllegalStateException("Sound not loaded. Must call #loadSound first.");
        }
        mSoundPool.unload(soundId);
    }


    /**
     * Call this if you don't need the SoundPlayer anymore. All memory will be
     * released and the object cannot be re-used.
     */
    public void release() {
        mIsReleased = true;
        mSoundPool.release();
    }

    public boolean isReleased() {
        return mIsReleased;
    }

    private static int getAudioTypeForSoundPool() {
        // STREAM_SYSTEM_ENFORCED is hidden API.
        return ApiHelper.getIntFieldIfExists(AudioManager.class,
                "STREAM_SYSTEM_ENFORCED", null, AudioManager.STREAM_RING);
    }
}

package com.google.android.core.audio;


/** A listener for changes in audio configuration. */

import android.media.AudioAttributes;

import com.google.android.core.SimpleExoPlayer;

/**
 * {@link AnalyticsCollector} 实现AudioListener接口。
 * {@link SimpleExoPlayer} 调用AudioListener接口。
 * audioListener.onAudioSessionId(sessionId);
 */

public interface AudioListener {

    /**
     * Called when the audio session is set.
     * AudioSession代表声音流的会话实例。
     *
     * @param audioSessionId    The audio session id.
     */
    default void onAudioSessionId(int audioSessionId) {

    }

    /**
     * Called when the audio attributes change.
     * AudioAttributes主要规定声音流的用途。
     *
     * @param audioAttributes   The audio attributes.
     */
    default void onAudioAttributesChanged(AudioAttributes audioAttributes) {

    }

    /**
     * Called when the volume changes.
     *
     * @param volume    The new volume, with 0 being silence and 1 being unity gain.
     */
    default void onVolumeChanged(float volume) {

    }
}

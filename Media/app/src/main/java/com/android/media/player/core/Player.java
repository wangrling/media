package com.android.media.player.core;

import com.android.media.player.core.audio.AudioAttributes;
import com.android.media.player.core.audio.AudioListener;
import com.android.media.player.core.audio.AuxEffectInfo;

/**
 * A media player interface defining traditional high-level functionality,
 * such as the ability to play, pause, seek and query properties of the currently playing media.
 *
 * <p>Some important properties of media players that implement this interface are:</p>
 * <ul>
 *     <li>They can provide a {@link Timeline} representing the structure of the media being played,
 *     which can be obtained by calling {@link #getCurrentTimeline()}.</li>
 *     <li>They can provide a {@link TrackGroupArray} defining the currently available tracks,
 *     which can be obtained by calling {@link #getCurrentTrackGroups()}.</li>
 *     <li>They contain a number of renderers, each of which is able to render tracks of a single
 *     type (e.g. audio, video or text). The number of renderers and their respective track types
 *     can be obtained by calling {@link #getRendererCount()} and {@link #getRendererType(int)}.</li>
 *     <li>They can provide a {@link TrackSelectionArray} defining which of the currently available
 *     tracks are selected to be rendered by each renderer. This can be obtained by calling
 *     {@link #getCurrentTrackSelections()}.</li>
 * </ul>
 */

// 总体的意思是给App client程序提供各种各种函数操作player，比如常规的暂停和开始，可以选择Track，想要播放什么声音，
// 可以选择Renderer，渲染不同的Track，通常都是有默认的。

public interface Player {

    /** The audio component of a {@link Player}*/
    interface AudioComponent {

        // 设置监听，然后在Client中实现相关函数，就能知道Server端的变化。

        /**
         * Adds a listener to receive audio events.
         *
         * @param listener  The listener to register.
         */
        void addAudioListener(AudioListener listener);

        /**
         * Removes a listener of audio events.
         *
         * @param listener  The listener to unregister.
         */
        void removeAudioListener(AudioListener listener);

        /**
         * @see {@link #setAudioAttributes(AudioAttributes, boolean)}
         */
        @Deprecated
        void setAudioAttributes(AudioAttributes audioAttributes);

        /**
         * Sets the attributes for audio playback, used by the underlying audio track. If not set, the
         * default audio attributes will be used. They are suitable for general media playback.
         *
         * <p>Setting the audio attributes during playback may introduce a short gap in audio output as
         * the audio track is recreated. A new audio session id will also be generated.
         *
         * <p>If tunneling is enabled by the track selector, the specified audio attributes will be
         * ignored, but they will take effect if audio is later played without tunneling.
         *
         * <p>If the device is running a build before platform API version 21, audio attributes cannot
         * be set directly on the underlying audio track. In this case, the usage will be mapped onto an
         * equivalent stream type using {@link Util#getStreamTypeForAudioUsage(int)}.
         *
         * <p>If audio focus should be handled, the {@link AudioAttributes#usage} must be {@link
         * C#USAGE_MEDIA} or {@link C#USAGE_GAME}. Other usages will throw an {@link
         * IllegalArgumentException}.
         *
         * @param audioAttributes The attributes to use for audio playback.
         * @param handleAudioFocus True if the player should handle audio focus, false otherwise.
         */
        void setAudioAttributes(AudioAttributes audioAttributes, boolean handleAudioFocus);

        /**
         * @return  The attributes for audio playback.
         */
        AudioAttributes getAudioAttributes();

        /**
         * @return  The audio session identifier, or {@link C#AUDIO_SESSION_ID_UNSET} if not set.
         */
        int getAudioSessionId();

        /**
         * Sets information on an auxiliary audio effect to attach to the underlying audio track.
         */
        void setAuxEffectInfo(AuxEffectInfo auxEffectInfo);

        /**
         * Detached any previously attached audio effect from the underlying audio track.
         */
        void clearAuxEffectInfo();

        /**
         * Sets the audio volume, with 0 being silence and 1 being unity gain.
         *
         * @param audioVolume   The audio volume.
         */
        void setVolume(float audioVolume);

        /**
         * @return  The audio volume, with 0 being silence and 1 being unity again.
         */
        float getVolume();
    }

    /** The video component of a {@link Player}. */
    interface VideoComponent {

        void setVideoScalingMode(@VideoScalingMode int videoScalingMode);

        @VideoScalingMode
        int getVideoScalingMode();

        void addVideoListener(VideoListener listener);

        void removeVideoListener(VideoListener listener);
    }
}

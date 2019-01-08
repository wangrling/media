package com.android.media.player.core;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.android.media.player.core.audio.AudioAttributes;
import com.android.media.player.core.audio.AudioListener;
import com.android.media.player.core.audio.AuxEffectInfo;

import com.android.media.player.core.metadata.MetadataOutput;
import com.android.media.player.core.source.TrackGroupArray;
import com.android.media.player.core.text.TextOutput;
import com.android.media.player.core.video.VideoFrameMetadataListener;
import com.android.media.player.core.video.VideoListener;
import com.android.media.player.core.video.spherical.CameraMotionListener;
import com.android.media.player.trackselection.TrackSelectionArray;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

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

        /**
         * Sets the {@link C.VideoScalingMode}.
         *
         * @param videoScalingMode The {@link C.VideoScalingMode}.
         */
        void setVideoScalingMode(@C.VideoScalingMode int videoScalingMode);

        /** Returns the {@link C.VideoScalingMode}. */
        @C.VideoScalingMode
        int getVideoScalingMode();

        /**
         * Adds a listener to receive video events.
         *
         * @param listener The listener to register.
         */
        void addVideoListener(VideoListener listener);

        /**
         * Removes a listener of video events.
         *
         * @param listener  The listener to unregister.
         */
        void removeVideoListener(VideoListener listener);

        /**
         * Sets a listener to receive video frame metadata events.
         *
         * <p>This method is intended to be called by the same component that sets the {@link Surface}
         * onto which video will be rendered. If using ExoPlayer's standard UI components, this method
         * should not be called directly from application code.
         *
         * @param listener The listener.
         */
        void setVideoFrameMetadataListener(VideoFrameMetadataListener listener);

        /**
         * Clears the listener which receives video frame metadata events if it matches the one passed.
         * Else does nothing.
         *
         * @param listener The listener to clear.
         */
        void clearVideoFrameMetadataListener(VideoFrameMetadataListener listener);

        /**
         * Sets a listener of camera motion events.
         *
         * @param listener The listener.
         */
        void setCameraMotionListener(CameraMotionListener listener);

        /**
         * Clears the listener which receives camera motion events if it matches the one passed. Else
         * does nothing.
         *
         * @param listener The listener to clear.
         */
        void clearCameraMotionListener(CameraMotionListener listener);

        /**
         * Clears any {@link Surface}, {@link SurfaceHolder}, {@link SurfaceView} or {@link TextureView}
         * currently set on the player.
         */
        void clearVideoSurface();

        /**
         * Clears the {@link Surface} onto which video is being rendered if it matches the one passed.
         * Else does nothing.
         *
         * @param surface The surface to clear.
         */
        void clearVideoSurface(Surface surface);

        /**
         * Sets the {@link Surface} onto which video will be rendered. The caller is responsible for
         * tracking the lifecycle of the surface, and must clear the surface by calling {@code
         * setVideoSurface(null)} if the surface is destroyed.
         *
         * <p>If the surface is held by a {@link SurfaceView}, {@link TextureView} or {@link
         * SurfaceHolder} then it's recommended to use {@link #setVideoSurfaceView(SurfaceView)}, {@link
         * #setVideoTextureView(TextureView)} or {@link #setVideoSurfaceHolder(SurfaceHolder)} rather
         * than this method, since passing the holder allows the player to track the lifecycle of the
         * surface automatically.
         *
         * @param surface The {@link Surface}.
         */
        void setVideoSurface(@Nullable Surface surface);

        /**
         * Sets the {@link SurfaceHolder} that holds the {@link Surface} onto which video will be
         * rendered. The player will track the lifecycle of the surface automatically.
         *
         * @param surfaceHolder The surface holder.
         */
        void setVideoSurfaceHolder(SurfaceHolder surfaceHolder);

        /**
         * Clears the {@link SurfaceHolder} that holds the {@link Surface} onto which video is being
         * rendered if it matches the one passed. Else does nothing.
         *
         * @param surfaceHolder The surface holder to clear.
         */
        void clearVideoSurfaceHolder(SurfaceHolder surfaceHolder);

        /**
         * Sets the {@link SurfaceView} onto which video will be rendered. The player will track the
         * lifecycle of the surface automatically.
         *
         * @param surfaceView The surface view.
         */
        void setVideoSurfaceView(SurfaceView surfaceView);

        /**
         * Clears the {@link SurfaceView} onto which video is being rendered if it matches the one
         * passed. Else does nothing.
         *
         * @param surfaceView The texture view to clear.
         */
        void clearVideoSurfaceView(SurfaceView surfaceView);

        /**
         * Sets the {@link TextureView} onto which video will be rendered. The player will track the
         * lifecycle of the surface automatically.
         *
         * @param textureView The texture view.
         */
        void setVideoTextureView(TextureView textureView);

        /**
         * Clears the {@link TextureView} onto which video is being rendered if it matches the one
         * passed. Else does nothing.
         *
         * @param textureView The texture view to clear.
         */
        void clearVideoTextureView(TextureView textureView);
    }

    /**
     * The text component of a {@ink Player}
     */
    interface TextComponent {

        /**
         * Registers an output to receive text events.
         *
         * @param listener The output to register.
         */
        void addTextOutput(TextOutput listener);

        /**
         * Removes a text output.
         *
         * @param listener  The output to remove.
         */
        void removeTextOutput(TextOutput listener);
    }

    /**
     * The metadata component of a {@link Player}.
     */
    interface MetadataComponent {

        /**
         * Adds a {@link MetadataOutput} to receive metadata.
         *
         * @param output The output to register.
         */
        void addMetadataOutput(MetadataOutput output);

        /**
         * Removes a {@link MetadataOutput}.
         *
         * @param output The output to remove.
         */
        void removeMetadataOutput(MetadataOutput output);
    }

    interface EventListener {
        default void onTimelineChanged(
                Timeline timeline, @Nullable Object manifest, @TimelineChangeReason int reason) {}

        default void onTracksChanged(
                TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

        default void onLoadingChanged(boolean isLoading) {}

        default void onPlayerStateChanged(boolean playWhenReady, int playbackState) {}

        default void onRepeatModeChanged(@RepeatMode int repeatMode) {}

        default void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}

        default void onPlayerError(ExoPlaybackException error) {}

        default void onPositionDiscontinuity(@DiscontinuityReason int reason) {}

        default void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

        default void onSeekProcessed() {}
    }

    @Deprecated
    abstract class DefaultEventListener implements EventListener {

        @Override
        @SuppressWarnings("deprecation")
        public void onTimelineChanged(
                Timeline timeline, @Nullable Object manifest, @TimelineChangeReason int reason) {

            // Call deprecated version. Otherwise, do nothing.
            onTimelineChanged(timeline, manifest);
        }

        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest) {
            // Do nothing.
        }
    }

    /**
     * The player does not have any media to play.
     */
    int STATE_IDLE = 1;

    /**
     * The player is not able to immediately play from its current position. This state typically
     * occurs when more data needs to be loaded.
     */
    int STATE_BUFFERING = 2;

    /**
     * The player is able to immediately play from its current position. The player will be playing if
     * {@link #getPlayWhenReady()} is true, and paused otherwise.
     */
    int STATE_READY = 3;

    /**
     * The player has finished playing the media.
     */
    int STATE_ENDED = 4;

    /**
     * Repeat modes for playback. One of {@link #REPEAT_MODE_OFF}, {@link #REPEAT_MODE_ONE} or {@link
     * #REPEAT_MODE_ALL}.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REPEAT_MODE_OFF, REPEAT_MODE_ONE, REPEAT_MODE_ALL})
    @interface RepeatMode {}

    /**
     * Normal playback without repetition.
     */
    int REPEAT_MODE_OFF = 0;

    /**
     * "Repeat One" mode to repeat the currently playing window infinitely.
     */
    int REPEAT_MODE_ONE = 1;

    /**
     * "Repeat All" mode to repeat the entire timeline infinitely.
     */
    int REPEAT_MODE_ALL = 2;


    /**
     * Reasons for position discontinuities. One of {@link #DISCONTINUITY_REASON_PERIOD_TRANSITION},
     * {@link #DISCONTINUITY_REASON_SEEK}, {@link #DISCONTINUITY_REASON_SEEK_ADJUSTMENT}, {@link
     * #DISCONTINUITY_REASON_AD_INSERTION} or {@link #DISCONTINUITY_REASON_INTERNAL}.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            DISCONTINUITY_REASON_PERIOD_TRANSITION,
            DISCONTINUITY_REASON_SEEK,
            DISCONTINUITY_REASON_SEEK_ADJUSTMENT,
            DISCONTINUITY_REASON_INTERNAL
    })
    @interface DiscontinuityReason {}

    /**
     * Automatic playback transition from one period in the timeline to the next. The period index may
     * be the same as it was before the discontinuity in case the current period is repeated.
     */
    int DISCONTINUITY_REASON_PERIOD_TRANSITION = 0;

    /** Seek within the current period or to another period. */
    int DISCONTINUITY_REASON_SEEK = 1;

    /**
     * Seek adjustment due to being unable to seek to the requested position or because the seek was
     * permitted to be inexact.
     */
    int DISCONTINUITY_REASON_SEEK_ADJUSTMENT = 2;

    /** Discontinuity to or from an ad within one period in the timeline. */
    int DISCONTINUITY_REASON_AD_INSERTION = 3;

    /** Discontinuity introduced internally by the source. */
    int DISCONTINUITY_REASON_INTERNAL = 4;

    /**
     * Reasons for timeline and/or manifest changes. One of {@link #TIMELINE_CHANGE_REASON_PREPARED},
     * {@link #TIMELINE_CHANGE_REASON_RESET} or {@link #TIMELINE_CHANGE_REASON_DYNAMIC}.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TIMELINE_CHANGE_REASON_PREPARED,
            TIMELINE_CHANGE_REASON_RESET,
            TIMELINE_CHANGE_REASON_DYNAMIC
    })
    @interface TimelineChangeReason {}

    /**
     * Timeline and manifest changed as a result of a player initialization with new media.
     */
    int TIMELINE_CHANGE_REASON_PREPARED = 0;

    /**
     * Timeline and manifest changed as a result of a player reset.
     */
    int TIMELINE_CHANGE_REASON_RESET = 1;

    /**
     * Timeline or manifest changed as a result of an dynamic update introduced by the played media.
     */
    int TIMELINE_CHANGE_REASON_DYNAMIC = 2;


}

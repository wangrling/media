package com.android.media.player.core;


// Application code <<== ExoPlayer interface ==>> ExoPlayer internal loop

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.android.media.player.core.audio.AudioAttributes;
import com.android.media.player.core.audio.AudioListener;
import com.android.media.player.core.audio.AuxEffectInfo;
import com.android.media.player.core.text.TextOutput;
import com.android.media.player.core.video.VideoFrameMetadataListener;
import com.android.media.player.core.video.VideoListener;
import com.android.media.player.core.video.spherical.CameraMotionListener;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

/**
 * A media player interface defining traditional high-level functionality, such as the ability to
 * play, pause, seek and query properties of the currently playing media.
 *
 * <p>Some important properties of media players that implement this interface are: </p>
 * <ul>
 * <li>They can provide a {@link Timeline} representing the structure of the media being played,
 * which can be obtained by calling {@link #getCurrentTimeline()}.</li>
 * <li>They can provide a {@link TrackGroupArray} defining the currently available tracks,
 * which can be obtained by calling {@link #getCurrentTrackGroups()}.</li>
 * <li>They contain a number of renderers, each of which is able to render tracks of a single
 * type (e.g. audio, video or text). The number of renderers and their respective track types
 * can be obtained by calling {@link #getRendererCount()} and {@link #getRendererType(int)}.</li>
 * <li>They can provide a {@link TrackSelectionArray} defining which of the currently available
 * tracks are selected to be rendered by each renderer. This can be obtained by calling
 * {@link #getCurrentTrackSelections()}.</li>
 * </ul>
 */

public interface Player {

    /**
     * The audio component of a {@link Player}.
     */
    interface AudioComponent {

        /**
         * Adds a listener to receive audio events.
         *
         * @param listener The listener to register.
         */
        void addAudioListener(AudioListener listener);

        /**
         * Removes a listener of audio events.
         *
         * @param listener The listener to unregister.
         */
        void removeAudioListener(AudioListener listener);

        /**
         * @see #setAudioAttributes(AudioAttributes, boolean)
         */
        @Deprecated
        void setAudioAttributes(AudioAttributes audioAttributes);

        /**
         * Sets the attributes for audio playback, used by the underlying audio track. If not set,
         * the default audio attributes will be used. They are suitable for general media playback.
         *
         * <p>Setting the audio attributes during playback may introduce a short gap in audio output
         * as the audio track is recreated. A new audio session id will also be generated.</p>
         *
         * <p>If tunneling is enabled by the track selector, the specified audio attributes will be
         * ignored, but they will take effect if audio is later played without tunneling.</p>
         *
         * <p>If the device is running a build before platform API version 21, audio attributes
         * cannot be set directly on the underlying audio track. In this case, the usage will be
         * mapped onto an equivalent stream type using {@link Util#getStreamTypeForAudioUsage(int)}.</p>
         *
         * <p>If audio focus should be handled, the {@link AudioAttributes#usage} must be
         * {@link android.media.AudioAttributes#USAGE_MEDIA} or
         * {@link android.media.AudioAttributes#USAGE_GAME}. Other usages will throw an
         * {@link IllegalArgumentException}.</p>
         *
         * @param audioAttributes  The attributes to use for audio playback.
         * @param handleAudioFocus True if the player should handle audio focus, false otherwise.
         */
        void setAudioAttributes(AudioAttributes audioAttributes, boolean handleAudioFocus);

        /**
         * @return The attributes for audio playback.
         */
        AudioAttributes getAudioAttributes();

        /**
         * @return The audio session identifier, or {@link C#AUDIO_SESSION_ID_UNSET} if not set.
         */
        int getAudioSessionId();

        // 音效

        /**
         * Sets information on an auxiliary audio effect to attach to the underlying audio track.
         */
        void setAuxEffectInfo(AuxEffectInfo auxEffectInfo);

        /**
         * Detaches any previously attached auxiliary audio effect from the underlying audio track.
         */
        void clearAuxEffectInfo();

        /**
         * Sets the audio volume, with 0 being silence and 1 being unity gain.
         *
         * @param audioVolume The audio volume.
         */
        void setVolume(float audioVolume);

        /**
         * @return The audio volume, with 0 being silence and 1 being unity gain.
         */
        float getVolume();
    }

    /**
     * The video component of a {@link Player}.
     */
    interface VideoComponent {

        /**
         * Sets the {@link C.VideoScalingMode}.
         *
         * @param videoScalingMode The {@link C.VideoScalingMode}.
         */
        void setVideoScalingMode(@C.VideoScalingMode int videoScalingMode);

        @C.VideoScalingMode
        int getVideoScalingMode();

        /**
         * Adds a listener to receive video events.
         *
         * @param listener The listener to register.
         */
        void addVideoListener(VideoListener listener);

        /**
         * Remove a listener of video events.
         *
         * @param listener The listener to unregister.
         */
        void removeVideoListener(VideoListener listener);

        /**
         * Sets a listener to receive video frame metadata events.
         *
         * <p>This method is intended to be called by the same component that sets the
         * {@link Surface} onto which video will be rendered. If using ExoPlayer's standard UI
         * components, this method should not be called directly from application code.</p>
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
         */
        void setCameraMotionListener(CameraMotionListener listener);

        /**
         * Clears the listener which receives camera motion events if it matches the one passed.
         * Else does nothing.
         */
        void clearCameraMotionListener(CameraMotionListener listener);

        // Surface相关

        /**
         * Clear any {@link Surface}, {@link SurfaceHolder}, {@link SurfaceView} or
         * {@link TextureView} currently set on the player.
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
         * tracking the lifecycle of the surface, and must clear the surface by calling
         * {@code setVideoSurface(null)} if the surface is destroyed.
         *
         * <p>If the surface is held by a {@link SurfaceView}, {@link TextureView} or {@link SurfaceHolder}
         * then it's recommended to use {@link #setVideoSurfaceView(SurfaceView)},
         * {@link #setVideoTextureView(TextureView)} or {@link #setVideoSurfaceHolder(SurfaceHolder)}
         * rather than this method, since passing the holder allows the player to track the lifecycle
         * of the surface automatically.</p>
         *
         * @param surface The {@lin Surface}.
         */
        void setVideoSurface(@Nullable Surface surface);

        /**
         * Sets the {@link SurfaceHolder} that holds the {@link Surface} onto which video will be
         * rendered. The player will track the lifecycle of the surface automatically.
         *
         * @param surfaceHolder The surface holder.
         */
        void setVideoSurfaceHolder(SurfaceHolder surfaceHolder);

        void clearVideoSurfaceHolder(SurfaceHolder surfaceHolder);

        /**
         * Sets the {@link SurfaceView} onto which video will be rendered. The player will track
         * the lifecycle of the surface automatically.
         *
         * @param surfaceView The surface view.
         */
        void setVideoSurfaceView(SurfaceView surfaceView);

        void clearVideoSurfaceView(SurfaceView surfaceView);

        /**
         * Sets the {@link TextureView} onto which video will be rendered. The player will track
         * lifecycle of the surface automatically.
         *
         * @param textureView The texture view.
         */
        void setVideoTextureView(TextureView textureView);

        void clearVideoTextureView(TextureView textureView);
    }

    /**
     * The text component of a {@link Player}.
     */
    interface TextComponent {

        /**
         * Registers an output to receive text events.
         *
         * @param listener The output to register.
         */
        void addTextOutput(TextOutput listener);

        void removeTextOutput(TextOutput listener);
    }

    /**
     * Listener of changes in player state. All methods have no-op default implementations to allow
     * selective overrides.
     */
    interface EventListener {
        /**
         * @param timeline
         * @param manifest
         * @param reason
         */
        default void onTimelineChanged(Timeline timeline, @Nullable Object manifest,
                                       @TimelineChangeReason int reason) {

        }

        /**
         * @param trackGroup
         * @param trackSelections
         */
        default void onTracksChanged(TrackGroupArray trackGroup, TrackSelectionArray trackSelections) {

        }

        /**
         * @param isLoading
         */
        default void onLoadingChanged(boolean isLoading) {

        }

        /**
         * @param playWhenReady
         * @param playbackState
         */
        default void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        }

        default void onRepeatModeChanged(@RepeatMode int repeatMode) {

        }

        default void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        /**
         * @param error
         */
        default void onPlayerError(ExoPlaybackException error) {

        }

        /**
         * @param reason
         */
        default void onPositionDiscontinuity(@DiscontinuityReason int reason) {

        }

        /**
         * @param playbackParameters
         */
        default void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        /**
         *
         */
        default void onSeekProcessed() {

        }
    }


    @Deprecated
    abstract class DefaultEventListener implements EventListener {

        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest,
                                      @TimelineChangeReason int reason) {

        }

        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest) {
            // Do nothing.
        }
    }

    int STATE_IDLE = 1;

    int STATE_BUFFERING = 2;

    int STATE_READY = 3;

    int STATE_ENDED = 4;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            REPEAT_MODE_OFF,
            REPEAT_MODE_ONE,
            REPEAT_MODE_ALL
    })
    @interface RepeatMode {

    }

    int REPEAT_MODE_OFF = 0;

    int REPEAT_MODE_ONE = 1;

    int REPEAT_MODE_ALL = 2;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            DISCONTINUITY_REASON_PERIOD_TRANSITION,
            DISCONTINUITY_REASON_SEEK,
            DISCONTINUITY_REASON_SEEK_ADJUSTMENT,
            DISCONTINUITY_REASON_AD_INSERTION,
            DISCONTINUITY_REASON_INTERNAL
    })
            @interface DiscontinuityReason {

    }
    int DISCONTINUITY_REASON_PERIOD_TRANSITION = 0;

    int DISCONTINUITY_REASON_SEEK = 1;

    int DISCONTINUITY_REASON_SEEK_ADJUSTMENT = 2;

    int DISCONTINUITY_REASON_AD_INSERTION = 3;

    int DISCONTINUITY_REASON_INTERNAL = 4;

    int TIMELINE_CHANGE_REASON_PREPARED = 0;
}

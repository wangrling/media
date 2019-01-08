package com.android.media.player.core;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.android.media.player.core.audio.AudioAttributes;
import com.android.media.player.core.audio.AudioListener;
import com.android.media.player.core.audio.AuxEffectInfo;

import com.android.media.player.core.video.VideoFrameMetadataListener;
import com.android.media.player.core.video.VideoListener;

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
        void removeTextOutput(TexOutput listener);
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
        void addMetadataOutput(MetadataComponent output);

        /**
         * Removes a {@link MetadataOutput}.
         *
         * @param output The output to remove.
         */
        void removeMetadataOutput(MetadataComponent output);
    }

    interface EventListener {
        default void onTimelineChanged(
                Timeline timeline, @Nullable Object manifest, @TimelineChangeReason int reason
        ) {}


    }
}

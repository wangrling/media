package com.google.android.core;

import android.media.AudioAttributes;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.google.android.core.audio.AudioListener;
import com.google.android.core.audio.AuxEffectInfo;
import com.google.android.core.text.TextOutput;
import com.google.android.core.video.VideoFrameMetadataListener;
import com.google.android.core.video.VideoListener;
import com.google.android.core.video.spherical.CameraMotionListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

/**
 * A media player interface defining traditional high-level functionality, such as the ability to
 * play, pause, seek and query properties of the currently playing media.
 * <p>
 * Some important properties of media players that implement this interface are:
 * <ul>
 *      <li>They can provide a {@link Timeline} representing the structure of the media being played,
 *      which can be obtained by calling {@link #getCurrentTimeline()}.</li>
 *      <li>The can provide a {@link TrackGroupArray} defining the currently available tracks,
 *      which can e obtained by calling {@link #getCurrentTrackGroups()}.</li>
 *      <li>They contain a number of renderers, each of which is able to render tracks of a single
 *      type (e.g. audio, video or text). The number of renderers and their respective track types
 *      can be obtained by calling {@link #getRendererCount()} and {@link #getRendererType(int)}.</li>
 *      <li>They can provide a {@link TrackSelectionArray} defining which of the currently available
 *      tracks are selected to be rendered by each renderer. This can be obtain by calling
 *      {@link #getCurrentTrackSelections()}.</li>
 * </ul>
 *
 * Player接口提供播放控制。
 * SimpleExoPlayer实现该接口。
 */

public interface Player {

    /** The audio component of a {@link Player}. */
    interface AudioComponent {


        void addAudioListener(AudioListener listener);


        void removeAudioListener(AudioListener listener);

        /**
         * @param audioAttributes   The attributes to use for audio playback.
         * @deprecated Use {@link AudioComponent#setAudioAttributes(AudioAttributes, boolean)}.
         */
        @Deprecated
        void setAudioAttributes(AudioAttributes audioAttributes);

        /**
         * Sets the audio attributes for audio playback, used by the underlying audio track. If not
         * set, the default audio attributes will be used. They are suitable for general media playback.
         *
         * <p>Setting the audio attribute during playback may introduce a short gap in audio output
         * as the audio track is recreated. A new audio session id will also be generated.
         *
         * <p>If tunneling is enabled by the track selector, the specified audio attributes will be
         * ignored, but they will take effect if audio is later played without tunneling.
         *
         * <p>If the device is running a build before platform API version 21, audio attributes cannot
         * be set directly on the underlying audio track. In this case, the usage will be mapped onto
         * an equivalent stream type using {@link Util#getStreamTypeForAudioUsage(int)}.
         *
         * <p>If audio focus should be handled, the {@link AudioAttributes#getUsage()} must be
         * {@link C#USAGE_MEDIA} or {@link C#USAGE_GAME}. Other usages will throw an {@link IllegalArgumentException}.
         *
         * @param audioAttributes   The attributes to use for audio playback.
         * @param handleAudioFocus  True if the player should handle audio focus, false otherwise.
         */
        void setAudioAttributes(AudioAttributes audioAttributes, boolean handleAudioFocus);

        /** Returns the attributes for audio playback. */
        AudioAttributes getAudioAttributes();

        /** Returns the audio session identifier, or {@link C#AUDIO_SESSION_ID_UNSET} if not set. */
        int getAudioSessionId();

        /** Sets information on an auxiliary (辅助) audio effect to attach to the underlying audio track. */
        void setAuxEffectInfo(AuxEffectInfo auxEffectInfo);

        /** Detaching any previously attached auxiliary audio effect from the underlying audio track. */
        void clearAuxEffectInfo();

        /** Returns the audio volume, with 0 being silence and 1 being unity gain. */
        float getVolume();
    }

    /** The Video component of a {@link Player}. */
    interface VideoComponent {


        /**
         * Sets the {@link C.VideoScalingMode}.
         * @param videoScalingMode  The {@link C.VideoScalingMode}.
         */
        void setVideoScalingMode(@C.VideoScalingMode int videoScalingMode);

        /** Return the {@link C.VideoScalingMode}. */
        @C.VideoScalingMode
        int getVideoScalingMode();

        /**
         * Adds a listener to receive video events.
         *
         * @param listener  The listener to unregister.
         */
        void addVideoListener(VideoListener listener);

        /**
         * Sets a listener to receive video frame metadata events.
         *
         * <p>This method is intended to be called by the same component that ses the {@link Surface}
         * onto which video will be rendered. If using ExoPlayer's standard UI components, this method
         * should not be called directly from application code.
         *
         * @param listener  The listener.
         */
        void setVideoFrameMetadataListener(VideoFrameMetadataListener listener);

        /**
         * Clears the listener which receives video frame metadata event if it matches the one passed.
         * Else does nothing.
         *
         * @param listener  The listener to clear.
         */
        void clearVideoFrameMetadataListener(VideoFrameMetadataListener listener);

        /**
         * Sets a listener of camera motion events.
         *
         * @param listener The listener.
         */
        void setCameraMotionListener(CameraMotionListener listener);

        /**
         * Clears the listener which receives camera motion events if it matches the one passed.
         * Else does nothing.
         *
         * @param listener  The listener to clear.
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
         * @param surface   The surface to clear.
         */
        void clearVideoSurface(Surface surface);

        /**
         * Sets the {@link Surface} onto which video will be rendered. The caller is responsible for
         * tracking the lifecycle of the surface, and must clear the surface by calling
         * {@code setVideoSurface(null)} if the surface is destroyed.
         *
         * <p>If the surface is held by a {@lin SurfaceView}, {@link TextureView} or
         * {@link SurfaceHolder} then it's recommended to use {@link #setVideoSurfaceView(SurfaceView)},
         * {@link #setVideoTextureView(TextureView)} or {@link #setVideoSurfaceHolder(SurfaceHolder)} rather
         * than this method, since padding the holder allows the player to track the lifecycle of the
         * surface automatically.
         *
         * @param  surface The {@link Surface}.
         */
        void setVideoSurface(@Nullable Surface surface);

        /**
         * Sets the {@link SurfaceHolder} that holds the {@link Surface} onto which video will be
         * rendered. The player will track the lifecycle of the surface automatically.
         * @param surfaceHolder
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
         * @param surfaceView
         */
        void setVideoSurfaceView(SurfaceView surfaceView);

        /**
         * Clears the {@link SurfaceView} onto which video is being rendered if it matches the one
         * passed. Else does nothing.
         *
         * @param surfaceView The surface view to clear.
         */
        void clearVideoSurfaceView(SurfaceView surfaceView);

        /**
         * Sets the {@link TextureView} onto which video will be rendered. The player will track the
         * lifecycle of the surface automatically.
         *
         * @param textureView   The texture view.
         */
        void setVideoTextureView(TextureView textureView);

        /**
         * Clears the {@link TextureView} onto which video is being rendered if it matches the one
         * passed. Else does nothing.
         *
         * @param textureView   The texture view to clear.
         */
        void clearVideoTextureView(TextureView textureView);
    }

    /** The text component of a {@link Player}. */
    interface TextComponent {

        /**
         * Registers an output to receive text events.
         *
         * @param listener  The output to register.
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
     * Repeat modes for playback. One of {@link #REPEAT_MODE_OFF}, {@link #REPEAT_MODE_ONE} or
     * {@link #REPEAT_MODE_ALL}.
     */
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
}

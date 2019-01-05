package com.android.live.player.core;

import android.os.Looper;

import com.android.live.player.core.source.MediaSource;

import androidx.annotation.Nullable;

public interface ExoPlayer extends Player {

    /**
     * @deprecated Use {@link Player.EventListener} instead.
     */
    @Deprecated
    interface EventListener extends Player.EventListener {}

    /** @deprecated Use {@link PlayerMessage.Target} instead. */
    @Deprecated
    interface ExoPlayerComponent extends PlayerMessage.Target {}

    /** @deprecated Use {@link PlayerMessage} instead. */
    @Deprecated
    final class ExoPlayerMessage {

        /** The target to receive the message. */
        public final PlayerMessage.Target target;
        /** The type of the message. */
        public final int messageType;
        /** The message. */
        public final Object message;

        /** @deprecated Use {@link ExoPlayer#createMessage(PlayerMessage.Target)} instead. */
        @Deprecated
        public ExoPlayerMessage(PlayerMessage.Target target, int messageType, Object message) {
            this.target = target;
            this.messageType = messageType;
            this.message = message;
        }
    }

    /**
     * @deprecated Use {@link Player#STATE_IDLE} instead.
     */
    @Deprecated
    int STATE_IDLE = Player.STATE_IDLE;
    /**
     * @deprecated Use {@link Player#STATE_BUFFERING} instead.
     */
    @Deprecated
    int STATE_BUFFERING = Player.STATE_BUFFERING;
    /**
     * @deprecated Use {@link Player#STATE_READY} instead.
     */
    @Deprecated
    int STATE_READY = Player.STATE_READY;
    /**
     * @deprecated Use {@link Player#STATE_ENDED} instead.
     */
    @Deprecated
    int STATE_ENDED = Player.STATE_ENDED;

    /**
     * @deprecated Use {@link Player#REPEAT_MODE_OFF} instead.
     */
    @Deprecated
    @RepeatMode int REPEAT_MODE_OFF = Player.REPEAT_MODE_OFF;
    /**
     * @deprecated Use {@link Player#REPEAT_MODE_ONE} instead.
     */
    @Deprecated
    @RepeatMode int REPEAT_MODE_ONE = Player.REPEAT_MODE_ONE;
    /**
     * @deprecated Use {@link Player#REPEAT_MODE_ALL} instead.
     */
    @Deprecated
    @RepeatMode int REPEAT_MODE_ALL = Player.REPEAT_MODE_ALL;

    /** Returns the {@link Looper} associated with the playback thread. */
    Looper getPlaybackLooper();

    /**
     * Retries a failed or stopped playback. Does nothing if the player has been reset, or if playback
     * has not failed or been stopped.
     */
    void retry();


    /**
     * Prepares the player to play the provided {@link MediaSource}. Equivalent to
     * {@code prepare(mediaSource, true, true)}.
     * <p>
     * Note: {@link MediaSource} instances are not designed to be re-used. If you want to prepare a
     * player more than once with the same piece of media, use a new instance each time.
     */
    void prepare(MediaSource mediaSource);

    /**
     * 非常重要函数
     * Prepares the player to play the provided {@link MediaSource}, optionally resetting the playback
     * position the default position in the first {@link Timeline.Window}.
     * <p>
     * Note: {@link MediaSource} instances are not designed to be re-used. If you want to prepare a
     * player more than once with the same piece of media, use a new instance each time.
     *
     * @param mediaSource   The {@link MediaSource} to play.
     * @param resetPosition Whether the playback position should be reset to the default position in
     *                      the first {@link Timeline.Window}. If false, playback will start from the
     *                      position defined by {@link #getCurrentWindowIndex()} and {@link #getCurrentPosition()}.
     * @param resetState    Whether the timeline, manifest, tracks and track selections should be
     *                      reset. Should be true unless the player is being prepared to play the same
     *                      media as it was playing previously (e.g. if playback failed and is being retried).
     */
    void prepare(MediaSource mediaSource, boolean resetPosition, boolean resetState);

    /**
     * Creates a message that can be sent to a {@link PlayerMessage.Target}. By default, the message
     * will be delivered immediately without blocking on the playback thread. The default {@link
     * PlayerMessage#getType()} is 0 and the default {@link PlayerMessage#getPayload()} is null. If a
     * position is specified with {@link PlayerMessage#setPosition(long)}, the message will be
     * delivered at this position in the current window defined by {@link #getCurrentWindowIndex()}.
     * Alternatively, the message can be sent at a specific window using {@link
     * PlayerMessage#setPosition(int, long)}.
     */
    PlayerMessage createMessage(PlayerMessage.Target target);


    /** @deprecated Use {@link #createMessage(PlayerMessage.Target)} instead. */
    @Deprecated
    @SuppressWarnings("deprecation")
    void sendMessages(ExoPlayerMessage... messages);

    /**
     * @deprecated Use {@link #createMessage(PlayerMessage.Target)} with {@link
     *     PlayerMessage#blockUntilDelivered()}.
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    void blockingSendMessages(ExoPlayerMessage... messages);

    /**
     * Sets the parameters that control how seek operations are performed.
     *
     * @param seekParameters The seek parameters, or {@code null} to use the defaults.
     */
    void setSeekParameters(@Nullable SeekParameters seekParameters);

    /** Returns the currently active {@link SeekParameters} of the player. */
    SeekParameters getSeekParameters();
}

package com.android.media.player.core.source;

import com.android.media.player.core.Timeline;

import androidx.annotation.Nullable;

/**
 * Defines and provides media to be played by an {@link ExoPlayer}. A MediaSource has two main
 * responsibilities:
 *
 * <ul>
 *     <li>To provide the player with a {@link Timeline} defining the structure of its media, and to
 *     provide a new timeline whenever the structure of the media changes. The MediaSource
 *     provides these timelines by calling {@link SourceInfoRefreshListener#onSourceInfoRefreshed}
 *     on the {@link SourceInfoRefreshListener}s passed to {@link #prepareSource(ExoPlayer,
 *     boolean, SourceInfoRefreshListener, TransferListener)}.
 *     </li>
 *     <li>To provide {@link MediaPeriod} instances for the periods in its timeline. MediaPeriods
 *     are obtained by calling {@link #createPeriod(MediaPeriodId, Allocator)}, and provide a way
 *     for the player to load and read the media.
 *     </li>
 * </ul>
 *
 * All methods are called on the player's internal playback thread, as described in the
 * {@link ExoPlayer} Javadoc. They should not be called directly from application code. Instances
 * can be re-used, but only for one {@link ExoPlayer} instance simultaneously.
 */

// listener是直接传递到{@link BaseMediaSource}类中，然后调用onSourceInfoRefreshed函数。
// 是MediaSource通知Player数据发生变化，传入参数，然后Player对参数作相关的处理。
// 慢慢理解Source和Player的关系，它们是通过{@link Timeline}和{@link MediaPeriod}参数联系。

public interface MediaSource {

    /** Listener for source events. */
    interface SourceInfoRefreshListener {

        /**
         * Called when manifest and/or timeline has been refreshed.
         * <p>Called on the playback thread.</p>
         *
         * @param source    The {@link MediaSource} whose info has been refreshed.
         * @param timeline  The source's timeline.
         * @param manifest  The loaded manifest. May be null.
         */
        void onSourceInfoRefreshed(MediaSource source, Timeline timeline, @Nullable Object manifest);
    }

    /**
     * Identifier for a {@link MediaPeriod}.
     */
    final class MediaPeriodId {

    }
}

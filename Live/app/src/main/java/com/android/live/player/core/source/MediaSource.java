package com.android.live.player.core.source;

import com.android.live.player.core.ExoPlayer;
import com.android.live.player.core.Timeline;

/**
 * Defines and provides media to be played by an {@link ExoPlayer}. A MediaSource has two main
 * responsibilities:
 *
 * <ul>
 *   <li>To provide the player with a {@link Timeline} defining the structure of its media, and to
 *       provide a new timeline whenever the structure of the media changes. The MediaSource
 *       provides these timelines by calling {@link SourceInfoRefreshListener#onSourceInfoRefreshed}
 *       on the {@link SourceInfoRefreshListener}s passed to {@link #prepareSource(ExoPlayer,
 *       boolean, SourceInfoRefreshListener, TransferListener)}.
 *   <li>To provide {@link MediaPeriod} instances for the periods in its timeline. MediaPeriods are
 *       obtained by calling {@link #createPeriod(MediaPeriodId, Allocator)}, and provide a way for
 *       the player to load and read the media.
 * </ul>
 *
 * All methods are called on the player's internal playback thread, as described in the {@link
 * ExoPlayer} Javadoc. They should not be called directly from application code. Instances can be
 * re-used, but only for one {@link ExoPlayer} instance simultaneously.
 */

public interface MediaSource {


}

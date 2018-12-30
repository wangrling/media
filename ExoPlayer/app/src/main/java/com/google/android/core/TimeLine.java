package com.google.android.core;

import androidx.annotation.Nullable;

/**
 * A flexible representation of the structure of media. A timeline is able to represent the
 * structure of a wide variety of media, from simple cases like a single media file through to
 * complex compositions of media such as playlists and streams with inserted ads. Instances are
 * immutable. For cases where media is changing dynamically (e.g. live streams), a timeline provides
 * a snapshot of the current state.
 * <p>
 * A timeline consists of related {@link Period} and {@link Window}s. A period defines a single
 * logical piece of media, for example a media file. It may also define groups of ads inserted into
 * the media, along with information about whether those ads have been loaded and played. A window
 * spans one or more periods, defining the region within those periods that's currently available
 * for playback along with additional information such as whether seeking is supported within the
 * window. Each window defines a default position, which is the position from which playback will
 * start when the player start playing the window. The following examples illustrate timelines for
 * various use cases.
 * 默认位置表示媒体播放的起点。
 * 详细图解 http://google.github.io/ExoPlayer/doc/reference/
 */

public abstract class TimeLine {

    /**
     * Holds information about a window in a {@link TimeLine}. A window defines a region of media
     * currently available for playback along with additional information such as whether seeking is
     * supported within the window. The figure below shows some of the information defined by a
     * window,as well as how this information relates to corresponding {@link Period}s in the
     * timeline.
     * 图中定义五个变量: firstPeriodIndex, lastPeriodIndex, positionInFirstPeriod, duration,
     * defaultStartPosition，其中defaultStartPosition用黑点表示。
     * <p align="center">
     *     <img src="doc-files/timeline-window.svg" alt="Information defined by a timeline window">
     * </p>
     */
    public static final class Window {

        /** A tag for the window. Not necessarily unique. */
        public @Nullable Object tag;

        /**
         * epoch 1970年1月1日00:00:00 UTC
         * The start time of the presentation to which this window belongs in milliseconds since the
         * epoch, or {@link C#TIME_UNSET} if unknown or not applicable. For informational purposes only.
         */
        public long presentationStartTimeMs;

        /**
         * The window's start time in milliseconds since the epoch, or {@link C#TIME_UNSET} if unknown
         * or not applicable. For informational purposes only.
         */
        public long windowStartTimeMs;

        /**
         * Whether it's possible to seek within this window.
         */
        public boolean isSeekable;

        /**
         * Whether this window may change when the timeline is updated.
         * Live stream with limited availability
         * The window will have Timeline.Window.isDynamic set to true if the stream is still live.
         */
        public boolean isDynamic;

        /**
         * The index of the first period that belongs to this window.
         */
        public int firstPeriodIndex;

        /**
         * The index of the last period that belongs to this window.
         */
        public int lastPeriodIndex;

        /**
         * The default position relative to the start of the window at which to begin playback, in
         * microseconds. May be {@link C#TIME_UNSET} if and only if the window was populated with a
         * non-zero default position projection, and if the specified projection cannot be performed
         * whilst (当…的时候) remaining within the bounds of the window.
         */
        public long defaultPositionUs;

        /**
         * The duration of this window in microseconds, or {@link C#TIME_UNSET} if unknown.
         */
        public long durationUs;

        /**
         * The position of the start of this window relative to the start of the first period
         * belonging to it, in microseconds.
         */
        public long positionInFirstPeriodUs;
    }
}

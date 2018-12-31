package com.google.android.core;

import android.util.Pair;
import android.view.WindowInsets;

import com.google.android.core.source.ads.AdPlaybackState;
import com.google.android.core.util.Assertions;
import com.google.android.core.util.Log;

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
 *
 * <h3 id="single-file>Single media file or on-demand stream</h3>
 * <p align="center">
 *     <img src="doc-files/timeline-single-file.svg" alt="Example timeline for a single file">
 * </p>
 * A timeline for a single media file or on-demand stream consists of a single period and window.
 * The window spans the whole period, indicating taht all parts of the media are available for
 * playback. The window's default position is typically at the start of the period (indicated by the
 * black dot in the figure above).
 *
 *
 */

public abstract class Timeline {

    /**
     * Holds information about a window in a {@link Timeline}. A window defines a region of media
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

        /** Sets the data held by this window. */
        public Window set(
                @Nullable Object tag,
                long presentationStartTimeMs,
                long windowStartTimeMs,
                boolean isSeekable,
                boolean isDynamic,
                long defaultPositionUs,
                long durationUs,
                int firstPeriodIndex,
                int lastPeriodIndex,
                long positionInFirstPeriodUs) {
            this.tag = tag;
            this.presentationStartTimeMs = presentationStartTimeMs;
            this.windowStartTimeMs = windowStartTimeMs;
            this.isSeekable = isSeekable;
            this.isDynamic = isDynamic;
            this.defaultPositionUs = defaultPositionUs;
            this.durationUs = durationUs;
            this.firstPeriodIndex = firstPeriodIndex;
            this.lastPeriodIndex = lastPeriodIndex;
            this.positionInFirstPeriodUs = positionInFirstPeriodUs;
            return this;
        }

        /**
         * @return  Returns the default position relative to the start of the window at which to begin
         * playback, in milliseconds. May e {@link C#TIME_UNSET} if and only if the window was populated with
         * a non-zero default position projection, and if the specified projection cannot be performed
         * whilst remaining within the bounds of the window.
         * 相当于播放本地文件的开头位置。
         */
        public long getDefaultPositionMs() {
            return C.usToMs(defaultPositionUs);
        }

        // 1s = 1000ms = 1000,000us

        /**
         * @return Returns the default position relative to the start of the window at which to begin
         * playback, in microseconds. May be {@link C#TIME_UNSET} if and only if the window was populated
         * with a non-zero default position projection, and if the specified projection cannot be performed
         * whilst remaining within the bounds of the window.
         */
        public long getDefaultPositionUs() {
            return defaultPositionUs;
        }

        /**
         * @return  the duration of the window in milliseconds, or {@link C#TIME_UNSET} if unknown.
         */
        public long getDurationMs() {
            return C.usToMs(durationUs);
        }

        /**
         * @return  the duration of this window in microseconds, or {@link C#TIME_UNSET} if unknown.
         */
        public long getDurationUs() {
            return durationUs;
        }

        // 只能获取三个参数，起始位置，长度，开始播放位置，都是以时间作为基准轴。
        // 三者的相对值不同，相对Period周期的起始时间，相对Window窗口的起始时间，相对epoch的时间。

        /**
         * @return  the position of the start of this window relative to the start of the first period
         * belonging to it, in milliseconds.
         */
        public long getPositionInFirstPeriodMs() {
            return C.usToMs(positionInFirstPeriodUs);
        }

        /**
         * @return  the position of the start of this window relative to the start of the first period
         * belonging to it, in microseconds.
         */
        public long getPositionInFirstPeriodUs() {
            return positionInFirstPeriodUs;
        }
    }

    /**
     * Holds information about a period in a {@link Timeline}. A period defines a single logical
     * piece of media, for example a media file. It may also define groups of ads inserted into the media,
     * along with information about whether those ads have been loaded and played.
     * <p>
     * The figure below shows some of the information defined by a period, as well as how this
     * information relates to a corresponding {@link Window} in the timeline.
     * <p>
     *     <img src="doc-files/timeline-period.svg" alt="Information defined by a period">
     * </p>
     */
    public static final class Period {

        /**
         * An identifier for the period. Not necessarily unique.
         */
        public Object id;

        /**
         * A unique identifier for the period.
         */
        public Object uid;

        /**
         * The index of the window to which this period belongs.
         */
        public int windowIndex;

        /**
         * The duration of this period in microseconds, or {@link C#TIME_UNSET} if unknown.
         */
        public long durationUs;

        private long positionInWindowUs;

        // 代表一组广告以及它们的状态。
        private AdPlaybackState adPlaybackState;

        /**
         * Sets the data held by this period.
         *
         * @param id
         * @param uid
         * @param windowIndex
         * @param durationUs
         * @param positionInWindowUs
         * @return
         */
        public Period set(Object id, Object uid, int windowIndex, long durationUs,
                          long positionInWindowUs) {
            return set(id, uid, windowIndex, durationUs, positionInWindowUs, AdPlaybackState.NONE);
        }

        public Period set(
                Object id,
                Object uid,
                int windowIndex,
                long durationUs,
                long positionInWindowUs,
                AdPlaybackState adPlaybackState) {
            this.id = id;
            this.uid = uid;
            this.windowIndex = windowIndex;
            this.durationUs = durationUs;
            this.positionInWindowUs = positionInWindowUs;
            this.adPlaybackState = adPlaybackState;
            return this;
        }


        /**
         * @return  the duration of this period in microseconds, or {@link C#TIME_UNSET} if unknown.
         */
        public long getDurationUs() {
            return durationUs;
        }
    }

    /** An empty timeline. */
    public static final Timeline EMPTY =
            new Timeline() {
                @Override
                public int getWindowCount() {
                    return 0;
                }

                @Override
                public Window getWindow(int windowIndex, Window window, boolean setTag, long defaultPositionProjectionUs) {
                    throw  new IndexOutOfBoundsException();
                }

                @Override
                public int getPeriodCount() {
                    return 0;
                }

                @Override
                public Period getPeriod(int periodIndex, Period period, boolean setIds) {
                    throw  new IndexOutOfBoundsException();
                }

                @Override
                public int getIndexOfPeriodUid(Object uid) {
                    return C.INDEX_UNSET;
                }

                @Override
                public Object getUidOfPeriod(int periodIndex) {
                    return new IndexOutOfBoundsException();
                }
            };

    /**
     * @return  whether the timeline is empty.
     */
    public final boolean isEmpty() {
        return getWindowCount() == 0;
    }

    // 获取Window和Period数量。

    /**
     * 获取Window数量
     *
     * @return  the number of windows in the timeline.
     */
    public abstract int getWindowCount();

    public int getNextWindowIndex(int windowIndex, @Player.RepeatMode int repeatMode,
                                  boolean shuffleModeEnabled) {
        switch (repeatMode) {
            case Player.REPEAT_MODE_OFF:
                // 如果index是最后一个index则返回C.INDEX_UNSET
                return windowIndex == getLastWindowIndex(shuffleModeEnabled) ? C.INDEX_UNSET :
                        windowIndex + 1;
            case Player.REPEAT_MODE_ONE:
                return windowIndex;
            case Player.REPEAT_MODE_ALL:
                // 如果index是最后一个index则返回最开始的index
                return windowIndex == getLastWindowIndex(shuffleModeEnabled) ?
                        getFirstWindowIndex(shuffleModeEnabled) : windowIndex + 1;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Returns the index of the window before the window at index {@code windowIndex} depending on the
     * {@code repeatMode} and whether shuffling is enabled.
     *
     * @param windowIndex   Index of window in the timeline.
     * @param repeatMode    A repeat mode.
     * @param shuffleModeEnabled    Whether shuffling is enabled.
     * @return  The index of the previous window, or {@link C#INDEX_UNSET} if this is the first window.
     */
    public int getPreviousWindowIndex(int windowIndex, @Player.RepeatMode int repeatMode,
                                      boolean shuffleModeEnabled) {
        switch (repeatMode) {
            case Player.REPEAT_MODE_OFF:
                // 如果等于最开始的index则返回C.INDEX_UNSET
                return windowIndex == getFirstWindowIndex(shuffleModeEnabled) ? C.INDEX_UNSET :
                        windowIndex - 1;
            case Player.REPEAT_MODE_ONE:
                return windowIndex;
            case Player.REPEAT_MODE_ALL:
                // 如果是最开始的index则返回最后的index
                return windowIndex == getFirstWindowIndex(shuffleModeEnabled)
                        ? getLastWindowIndex(shuffleModeEnabled) : windowIndex - 1;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Returns the index of the last window in the playback order depending on whether shuffling is
     * enabled.
     *
     * @param shuffleModeEnabled    Whether shuffling is enabled.
     *                              参数并没有使用。
     * @return  The index of the last window in the playback order, or {@link C#INDEX_UNSET} if the
     * timeline is empty.
     */
    public int getLastWindowIndex(boolean shuffleModeEnabled) {
        return isEmpty() ? C.INDEX_UNSET : getWindowCount() - 1;
    }

    /**
     * Returns the index of the first window in the playback order depending on whether shuffling is
     * enabled.
     *
     * @param shuffleModeEnabled    Whether shuffling is enabled.
     *                              参数并没有使用
     * @return  The index of the first window in the playback order, or {@link C#TIME_UNSET} if the
     *          timeline is empty.
     */
    public int getFirstWindowIndex(boolean shuffleModeEnabled) {
        return isEmpty() ? C.INDEX_UNSET : 0;
    }

    /**
     * Populates a {@link Window} with data for the window at the specified index. Does not populate
     * {@link Window#tag}.
     *
     * @param windowIndex   The index of the window.
     * @param window        The {@link Window} to populate. Must not be null.
     * @return  The populated {@link Window}, for convenience.
     */
    public final Window getWindow(int windowIndex, Window window) {
        return getWindow(windowIndex, window, false);
    }

    /**
     * Populates a {@link Window} with data for the window at the specified index.
     *
     * @param windowIndex   The index of the window.
     * @param window    The {@link Window} to populate. Must not be null.
     * @param setTag    Whether {@link Window#tag} should be populated. If false, the field will be
     *                  set to null. The caller should pass false for efficiency reasons unless the
     *                  field is required.
     * @return  The populated {@link Window}, for convenience.
     */
    public final Window getWindow(int windowIndex, Window window, boolean setTag) {
        return getWindow(windowIndex, window, setTag, 0);
    }

    /**
     * Populates a {@link Window} with data for the window at the specified index.
     *
     * @param windowIndex   The index of the window.
     * @param window    The {@link Window} to populate. Must not be null.
     * @param setTag    Whether {@link Window#tag} should be populated. If false, the field will be
     *                  set to null. The caller should pass false for efficiency reasons unless the field
     *                  is required.
     * @param defaultPositionProjectionUs   A duration into the future that the populated window's
     *                                      default start position should be projected.
     *                                     　描述窗口未来默认的开始位置。
     * @return  The populated {@link Window}, for convenience.
     */
    public abstract Window getWindow(
            int windowIndex, Window window, boolean setTag, long defaultPositionProjectionUs);

    /**
     * 获取Period的数量
     *
     * @return  the number of periods in the timeline.
     */
    public abstract int getPeriodCount();

    /**
     * Returns the index of the period after the period at index {@code periodIndex} depending on the
     * {@code repeatMode} and whether shuffling is enabled.
     * @param periodIndex   Index of a period in the timeline.
     * @param period    A {@link Period} to be used internally. Must not be null.
     * @param window    A {@link Window} to be used internally. Must not be null.
     * @param repeatMode    A repeat mode.
     * @param shuffleModeEnabled    Whether shuffling is enabled.
     * @return  The index of the next period, or {@link C#INDEX_UNSET} if this is the last period.
     */
    public final int getNextPeriodIndex(int periodIndex, Period period, Window window,
                                        @Player.RepeatMode int repeatMode, boolean shuffleModeEnabled) {
        int windowIndex = getPeriod(periodIndex, period).windowIndex;
        // 如果这是最后一个Period
        if (getWindow(windowIndex, window).lastPeriodIndex == periodIndex) {
            int nextWindowIndex = getNextWindowIndex(windowIndex, repeatMode, shuffleModeEnabled);
            if (nextWindowIndex == C.INDEX_UNSET) {
                // 同时也是最后一个Window就返回C.INDEX_UNSET
                return C.INDEX_UNSET;
            }
            // 返回下一个Window的第一个Period
            return getWindow(nextWindowIndex, window).firstPeriodIndex;
        }
        return periodIndex + 1;
    }

    public final boolean isLastPeriod(int periodIndex, Period period, Window window,
                                      @Player.RepeatMode int repeatMode, boolean shuffleModeEnabled) {
        return getNextPeriodIndex(periodIndex, period, window, repeatMode, shuffleModeEnabled)
                == C.INDEX_UNSET;
    }

    /**
     * Calls {@link #getPeriodPosition(Window, Period, int, long)} with zero default position
     * projection.
     */
    public final Pair<Object, Long> getPeriodPosition(
            Window window, Period period, int windowIndex, long windowPositionUs) {
        return getPeriodPosition(window, period, windowIndex, windowPositionUs, 0);
    }

    /**
     * Converts (windowIndex, windowPositionUs) to the corresponding (periodUid, periodPositionUs).
     *
     * @param window    A {@link Window} that may be overwritten.
     * @param period    A {@link Period} that may be overwritten.
     * @param windowIndex   The window index.
     * @param windowPositionUs  The Window time, or {@link C#TIME_UNSET} to use the window's default
     *                          start position.
     * @param defaultPositionProjectionUs   if {@code windowPositionUs} is {@link C#TIME_UNSET}, the
     *                                      duration into the future by which the window's position
     *                                      should be projected.
     * @return  The corresponding (periodUid, periodPositionUs), or null if {@code #windowPosistionUs}
     * is {@link C#TIME_UNSET}, {@code defaultPositionProjectionUs} is non-zero, and the window's
     * position could be projected by {@code defaultPositionProjectionUs}.
     */
    public final Pair<Object, Long> getPeriodPosition(
            Window window,
            Period period,
            int windowIndex,
            long windowPositionUs,  // period.positionInWindow这个变量
            long defaultPositionProjectionUs) {
        Assertions.checkIndex(windowIndex, 0, getWindowCount());
        getWindow(windowIndex, window, false, defaultPositionProjectionUs);
        if (windowPositionUs == C.TIME_UNSET) {
            // 如果没有设置就设置为默认值
            windowPositionUs = window.getDefaultPositionUs();
            if (windowPositionUs == C.TIME_UNSET) {
                // 如果默认值也没有设置就返回null
                return null;
            }
        }
        // 根据Window获取Period
        int periodIndex = window.firstPeriodIndex;
        long periodPositionUs = window.getPositionInFirstPeriodUs();
        // Period的时间长度
        long periodDurationUs = getPeriod(periodIndex, period, /* setIds= */ true).getDurationUs();
        // 核心
        while (periodDurationUs != C.TIME_UNSET && periodPositionUs >= periodDurationUs
                && periodIndex < window.lastPeriodIndex) {
            periodPositionUs -= periodDurationUs;
            periodDurationUs = getPeriod(++periodIndex, period, /* setIds= */ true).getDurationUs();
        }
        Log.d("create period position " + periodPositionUs);
        return Pair.create(period.uid, periodPositionUs);
    }

    /**
     * Populates a {@link Period} with data for the period with the specified unique identifier.
     *
     * @param periodIndex   The unique identifier of the period.
     * @param period    The {@link Period} to populate. Must not be null.
     * @return  The Populated {@link Period}, for convenience.
     */
    public final Period getPeriod(int periodIndex, Period period) {
        return getPeriod(periodIndex, period, false);
    }

    /**
     * Populates a {@link Period} with data for the period at the specified index.
     *
     * @param periodIndex   The index of the period.
     * @param period    The {@link Period} to populate. Must not be null.
     * @param setIds    Whether {@link Period#id} and {@link Period#uid} should be populated. If false,
     *                  the fields will be set to null. The caller should pass false for efficiency
     *                  reasons unless the fields are required.
     * @return  The populated {@link Period}, for convenience.
     */
    public abstract Period getPeriod(int periodIndex, Period period, boolean setIds);

    /**
     * Populates a {@link Period} with data for the period with the specified unique identifier.
     *
     * @param periodUid The unique identifier of the period.
     * @param period    The {@link Period} to populate. Must not be null.
     * @return  the populated {@link Period}, for convenience.
     */
    public Period getPeriodByUid(Object periodUid, Period period) {
        return getPeriod(getIndexOfPeriodUid(periodUid), period, /* setIds= */ true);
    }


    /**
     * Returns the index of the period identified by its unique {@code id}, or {@link C#INDEX_UNSET}
     * if the period is not in the timeline.
     *
     * @param uid   A unique identifier for a period.
     * @return  The index of the period, or {@link C#INDEX_UNSET} if the period was not found.
     */
    public abstract int getIndexOfPeriodUid(Object uid);

    /**
     * Returns the unique id of the period identified by its index in the timeline.
     *
     * @param periodIndex   The index of the period.
     * @return  The unique id of the period.
     */
    public abstract Object getUidOfPeriod(int periodIndex);
}

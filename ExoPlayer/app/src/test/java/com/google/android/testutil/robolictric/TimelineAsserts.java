package com.google.android.testutil.robolictric;

import com.google.android.core.C;
import com.google.android.core.Player;
import com.google.android.core.Timeline;

import static com.google.common.truth.Truth.assertThat;

/** Unit test for {@link Timeline}. */
public final class TimelineAsserts {

    private static final int[] REPEAT_MODES = {
            Player.REPEAT_MODE_OFF, Player.REPEAT_MODE_ONE, Player.REPEAT_MODE_ALL
    };

    private TimelineAsserts() {}

    /** Assert that timeline is empty (i.e. has no windows or periods). */
    public static void assertEmpty(Timeline timeline) {
        assertWindowTags(timeline);
        assertPeriodCounts(timeline);

        for (boolean shuffled : new boolean[] {false, true}) {
            assertThat(timeline.getFirstWindowIndex(shuffled)).isEqualTo(C.INDEX_UNSET);
            assertThat(timeline.getLastWindowIndex(shuffled)).isEqualTo(C.INDEX_UNSET);
        }
    }

    /**
     * Asserts that window tags are set correctly.
     *
     * @param expectedWindowTags    A list of expected window tags. If a tag is unknown or not important
     *                              {@code null} can be passed to skip this window.
     */
    public static void assertWindowTags(Timeline timeline, Object... expectedWindowTags) {
        Timeline.Window window = new Timeline.Window();
        assertThat(timeline.getWindowCount()).isEqualTo(expectedWindowTags.length);
        for (int i = 0; i < timeline.getWindowCount(); i++) {
            if (expectedWindowTags[i] != null) {
                assertThat(window.tag).isEqualTo(expectedWindowTags[i]);
            }
        }
    }

    public static void assertPeriodCounts(Timeline timeline, int... expectedPeriodCounts) {
        int windowCount = timeline.getWindowCount();
        assertThat(windowCount).isEqualTo(expectedPeriodCounts.length);
        int[] accumulatedPeriodCounts = new int[windowCount + 1];
        accumulatedPeriodCounts[0] = 0;
        for (int i = 0; i < windowCount; i++) {
            accumulatedPeriodCounts[i+1] = accumulatedPeriodCounts[i] + expectedPeriodCounts[i];
        }
        assertThat(timeline.getPeriodCount())
                .isEqualTo(accumulatedPeriodCounts[accumulatedPeriodCounts.length-1]);
        Timeline.Window window = new Timeline.Window();
        Timeline.Period period = new Timeline.Period();

        for (int i = 0; i < windowCount; i++) {
            timeline.getWindow(i, window, true);
            assertThat(window.firstPeriodIndex).isEqualTo(accumulatedPeriodCounts[i]);
            assertThat(window.lastPeriodIndex).isEqualTo(accumulatedPeriodCounts[i+1]-1);
        }
        int expectedWindowIndex = 0;
        for (int i = 0; i < timeline.getPeriodCount(); i++) {
            timeline.getPeriod(i, period, true);
            while (i >= accumulatedPeriodCounts[expectedWindowIndex+1]) {
                expectedWindowIndex++;
            }
            assertThat(period.windowIndex).isEqualTo(expectedWindowIndex);
            assertThat(timeline.getIndexOfPeriodUid(period.uid)).isEqualTo(i);
            assertThat(timeline.getUidOfPeriod(i)).isEqualTo(period.uid);
            for (int repeatMode : REPEAT_MODES) {
                if (i < accumulatedPeriodCounts[expectedWindowIndex+1] - 1) {
                    assertThat(timeline.getNextPeriodIndex(i, period, window, repeatMode, false))
                            .isEqualTo(i+1);
                } else {
                    int nextWindow = timeline.getNextWindowIndex(expectedWindowIndex, repeatMode, false);
                    int nextPeriod = nextWindow == C.INDEX_UNSET ? C.INDEX_UNSET : accumulatedPeriodCounts[nextWindow];
                    assertThat(timeline.getNextPeriodIndex(i, period, window, repeatMode, false))
                            .isEqualTo(nextPeriod);
                }
            }
        }
    }
}

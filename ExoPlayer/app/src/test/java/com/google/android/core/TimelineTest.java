package com.google.android.core;

import com.google.android.testutil.robolictric.TimelineAsserts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/** Unit test for {@link Timeline}. */

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class TimelineTest {

    @Test
    public void testEmptyTimeline() {
        TimelineAsserts.assertEmpty(Timeline.EMPTY);
    }
}

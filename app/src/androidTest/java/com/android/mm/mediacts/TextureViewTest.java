package com.android.mm.mediacts;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;


@MediumTest
public class TextureViewTest {
    static final int EGL_GL_COLORSPACE_SRGB_KHR = 0x3089;
    static final int EGL_GL_COLORSPACE_DISPLAY_P3_EXT = 0x3363;
    static final int EGL_GL_COLORSPACE_SCRGB_LINEAR_EXT = 0x3350;

    @Rule
    public ActivityTestRule<TextureViewCtsActivity> mActivityRule =
            new ActivityTestRule<>(TextureViewCtsActivity.class, false, false);

    @Test
    public void testFirstFrames() {
        final TextureViewCtsActivity activity = mActivityRule.launchActivity(null);

    }
}

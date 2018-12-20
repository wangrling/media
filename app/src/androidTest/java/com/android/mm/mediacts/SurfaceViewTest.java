package com.android.mm.mediacts;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Region;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.android.mm.mediacts.util.PollingCheck;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.annotation.MatchesPattern;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SurfaceViewTest {


    @Rule
    public ActivityTestRule<SurfaceViewCtsActivity> mActivityRule =
            new ActivityTestRule<>(SurfaceViewCtsActivity.class);

    private Context mContext;
    private SurfaceViewCtsActivity.MockSurfaceView mMockSurfaceView;

    private int mSurfaceViewId;
    @Before
    public void setUp() throws Exception {

        final SurfaceViewCtsActivity activity =  mActivityRule.getActivity();
        mContext = activity.getApplicationContext();

        new PollingCheck() {
            @Override
            protected boolean check() {
                return activity.hasWindowFocus();
            }
        }.run();

        mMockSurfaceView = activity.getSurfaceView();
    }

    @UiThreadTest
    public void testConstructor() {
        new SurfaceView(mContext);
        new SurfaceView(mContext, null);
        new SurfaceView(mContext, null, 0);
    }

    @Test
    public void testSurfaceView() {
        final int left = 40;
        final int top = 30;
        final int right = 320;
        final int bottom = 240;

        assertTrue(mMockSurfaceView.isDraw());
        assertTrue(mMockSurfaceView.isOnAttachedToWindow());
        assertTrue(mMockSurfaceView.isDispatchDraw());
        assertTrue(mMockSurfaceView.isSurfaceChanged());

        assertTrue(mMockSurfaceView.isOnWindowVisibilityChanged());

        int expectedVisibility = mMockSurfaceView.getVisibility();
        int actualVisibility = mMockSurfaceView.getVInOnWindowVisibilityChanged();
        assertEquals(expectedVisibility, actualVisibility);

        assertTrue(mMockSurfaceView.isOnMeasureCalled());
        int expectedWidth = mMockSurfaceView.getMeasuredWidth();
        int expectedHeight = mMockSurfaceView.getMeasuredHeight();
        int actualWidth = mMockSurfaceView.getWidthInOnMeasure();
        int actualHeight = mMockSurfaceView.getHeightInOnMeasure();

        assertEquals(expectedWidth, actualWidth);
        assertEquals(expectedHeight, actualHeight);

        Region region = new Region();
        region.set(left, top, right, bottom);
        assertTrue(mMockSurfaceView.gatherTransparentRegion(region));
        mMockSurfaceView.setFormat(PixelFormat.TRANSPARENT);
        assertFalse(mMockSurfaceView.gatherTransparentRegion(region));

        SurfaceHolder actual = mMockSurfaceView.getHolder();
        assertNotNull(actual);
        assertTrue(actual instanceof SurfaceHolder);
    }

    @UiThreadTest
    /**
     * Check point:
     * check surfaceView scroll X and y before and after scrollTo
     */
    public void testOnScrollChanged() {
        final int scrollToX = 200;
        final int scrollToY = 200;

        int oldHorizontal = mMockSurfaceView.getScrollX();
        int oldVertical = mMockSurfaceView.getScrollY();
        assertFalse(mMockSurfaceView.isOnScrollChanged());

        mMockSurfaceView.scrollTo(scrollToX, scrollToY);
        assertTrue(mMockSurfaceView.isOnScrollChanged());

        assertEquals(oldHorizontal, mMockSurfaceView.getOldHorizontal());
        assertEquals(oldVertical, mMockSurfaceView.getOldVertical());

        assertEquals(scrollToX, mMockSurfaceView.getScrollX());
        assertEquals(scrollToY, mMockSurfaceView.getScrollY());
    }

    @Test
    public void testOnDetachedFromWindow() {
        final SurfaceViewCtsActivity.MockSurfaceView mockSurfaceView = mActivityRule.getActivity().getSurfaceView();
        assertFalse(mockSurfaceView.isDetachedFromWindow());

        assertTrue(mockSurfaceView.isShown());

        onView().perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                pressBack();
            }
        });

        new PollingCheck() {
            @Override
            protected boolean check() {
                return mockSurfaceView.isDetachedFromWindow() &&
                        !mockSurfaceView.isShown();
            }
        }.run();
    }
}

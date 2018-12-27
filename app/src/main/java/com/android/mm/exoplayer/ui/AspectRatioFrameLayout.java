package com.android.mm.exoplayer.ui;

import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * A {@link FrameLayout} that resizes itself to match a specified aspect ratio.
 */

public class AspectRatioFrameLayout {

    /** Listener to be notified about changes of the aspect ratios of this view. */
    public interface AspectRatioListener {

        /**
         * Called when either the target aspect ratio or the view aspect ratio is updated.
         *
         * @param targetAspectRatio The aspect ratio that has been set in {@link #setAspectRatio(float)}
         * @param naturalAspectRatio The natural aspect ratio of this view (before its width and height
         *     are modified to satisfy the target aspect ratio).
         * @param aspectRatioMismatch Whether the target and natural aspect ratios differ enough for
         *     changing the resize mode to have an effect.
         */
        void onAspectRatioUpdated(
                float targetAspectRatio, float naturalAspectRatio, boolean aspectRatioMismatch
        );
    }

    // LINT.IfChange
    /**
     * Resize modes for {@link AspectRatioFrameLayout}. One of {@link #RESIZE_MODE_FIT}, {@link
     * #RESIZE_MODE_FIXED_WIDTH}, {@link #RESIZE_MODE_FIXED_HEIGHT}, {@link #RESIZE_MODE_FILL} or
     * {@link #RESIZE_MODE_ZOOM}.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            RESIZE_MODE_FIT,
            RESIZE_MODE_FIXED_WIDTH,
            RESIZE_MODE_FIXED_HEIGHT,
            RESIZE_MODE_FILL,
            RESIZE_MODE_ZOOM
    })
    public @interface ResizeMode {}

    /**
     * Either the width or height is decreased to obtain the desired aspect ratio.
     */
    public static final int RESIZE_MODE_FIT = 0;
    /**
     * The width is fixed and the height is increased or decreased to obtain the desired aspect ratio.
     */
    public static final int RESIZE_MODE_FIXED_WIDTH = 1;
    /**
     * The height is fixed and the width is increased or decreased to obtain the desired aspect ratio.
     */
    public static final int RESIZE_MODE_FIXED_HEIGHT = 2;
    /**
     * The specified aspect ratio is ignored.
     */
    public static final int RESIZE_MODE_FILL = 3;
    /**
     * Either the width or height is increased to obtain the desired aspect ratio.
     */
    public static final int RESIZE_MODE_ZOOM = 4;
    // LINT.ThenChange(../../../../../../res/values/attrs.xml)


}

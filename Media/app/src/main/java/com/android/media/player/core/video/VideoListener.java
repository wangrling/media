package com.android.media.player.core.video;

import com.android.media.player.core.C;

/**
 * A listener for metadata corresponding to video being rendered.
 */
public interface VideoListener {

    /**
     * Called each time there's a change in the size of the video being rendered.
     *
     * @param width The video width in pixels.
     * @param height    The video height in pixels.
     * @param unappliedRotationDegrees  This value will always be zero on API levels 21 and above,
     *                                  since the renderer will apply all necessary rotations internally.
     * @param pixelWidthHeightRatio The width to height ratio of each pixel. For the normal case of
     *                              square pixels this will be equal to 1.0. Different values are
     *                              indicative of anamorphic.
     */
    default void onVideoSizeChanged(
            int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    /**
     * Called each time there's a change in the size of the surface onto which the video is being
     * rendered.
     *
     * @param width The surface width in pixels. May be {@link C#LENGTH_UNSET} if unknown, or 0 if
     *              the video is not rendered onto a surface.
     * @param height    The surface height in pixels. May be {@link C#LENGTH_UNSET} if unknown, or
     *                  0 if the video is not rendered onto the surface.
     */
    default void onSurfaceSizeChanged(int width, int height) {

    }

    /**
     * Called when a frame is rendered for the first time since setting the surface, and then a
     * frame is rendered for the first time since a video track was selected.
     */
    default void onRenderedFirstFrame() {

    }
}

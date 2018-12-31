package com.google.android.core.video;

import android.graphics.Matrix;
import android.view.TextureView;

import com.google.android.core.C;

/**
 * A listener for metadata corresponding to video being rendered.
 */
public interface VideoListener {

    /**
     * Called each time there's a change in the size of the video being rendered.
     *
     * @param width     The video width in pixels.
     * @param height    The video height in pixels.
     * @param unappliedRotationDegrees  For videos that require a rotation, this is the clockwise
     *                                  rotation in degrees that the application should apply for
     *                                  the video for it to be rendered in the correct orientation.
     *                                  This value will always be zero on API level 21 and above,
     *                                  since the renderer will apply all necessary rotations internally.
     *                                  On earlizer API levels this is not possible. Applications that
     *                                  use {@ink TextureView} can apply the rotation by calling
     *                                  {@link TextureView#setTransform(Matrix)}. Applications that
     *                                  do not expect to encounter rotated videos can safely ignore
     *                                  this parameter.
     * @param pixelWidthHeightRatio The width to height ratio of each pixel. For the normal case of
     *                              square pixels this wil be equal to 1.0. Different values are
     *                              indicative of anamorphic (变形的) content.
     */
    default void onVideoSizeChanged(
            int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {}

    /**
     * Called each time there's a change in the size of the surface onto which the video is being
     * rendered.
     *
      * @param width    The surface width in pixels. May be {@link C#LENGTH_UNSET} if unknown, or
     *                 0 if the video is not rendered onto a surface.
     * @param height    The surface height in pixels. May be {@link C#LENGTH_UNSET} if unknown, or
     *                  0 is the video is not rendered onto a surface.
     */
    default void onSurfaceSizeChanged(int width, int height) {}

    /**
     * Called when a frame is rendered for the first time since setting the surface, and when a
     * frame is rendered for the first time since a video track was selected.
     */
    default void onRenderedFirstFrame() {}
}

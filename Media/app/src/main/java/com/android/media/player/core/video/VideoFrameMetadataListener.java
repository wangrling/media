package com.android.media.player.core.video;

import com.android.media.player.core.Format;

/**
 * A listener for metadata corresponding to video frame being rendered.
 */

public interface VideoFrameMetadataListener {

    /**
     * Called when the video frame about to be rendered. This method is called on the playback
     * thread.
     *
     * @param presentationTimeUs    The presentation time of the output buffer, in microseconds
     * @param releaseTimeNs The wallclock time at which the frame should be displayed, in nanoseconds.
     *                      If the platform API version of the device is less than 21, then this is
     *                      te base effort.
     * @param format    The format associated with the frame.
     */
    void onVideoFrameAboutToBeRendered(long presentationTimeUs, int releaseTimeNs, Format format);
}

package com.google.android.core.video;


import com.google.android.core.Format;

/* A listener for metadata corresponding to video frame being rendered. */
public interface VideoFrameMetadataListener {

    /**
     * Call when the video frame about to be rendered. This method is called on the playback thread.
     *
     * @param presentationTimeUs    The presentation time of the output buffer, in microseconds.
     * @param releaseTimeNs     The wallclock time at which the frame should be displayed, in nanoseconds.
     *                          If the platform API version of the device is less than 21, then this is the
     *                          best effort.
     * @param format    The format assoiated with the frame.
     */

    void onVideoFrameAboutToBeRenderer(long presentationTimeUs, long releaseTimeNs, Format format);
}

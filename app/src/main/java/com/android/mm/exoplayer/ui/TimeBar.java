package com.android.mm.exoplayer.ui;

/**
 * Interface for time bar views that can display a playback position, buffered position, duration
 * and ad markers, and that have listener for scrubbing (seeking) events.
 */

public interface TimeBar {

    // 拖动进度条

    /**
     * Adds a listener for scrubbing events.
     *
     * @param listener  The listener to add.
     */
    void addListener(OnScrubListener listener);

    void removeListener(OnScrubListener listener);


    /**
     * Listener for scrubbing events.
     */
    interface OnScrubListener {


    }
}

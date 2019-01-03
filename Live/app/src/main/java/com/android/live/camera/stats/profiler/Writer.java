package com.android.live.camera.stats.profiler;

/**
 * Used to write strings to an arbitrary output source.
 */
public interface Writer {
    /**
     * Used to write messages to another stream or object.
     */
    public void write(String message);
}

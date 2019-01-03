package com.android.live.camera.stats.profiler;

/**
 * Creates profile instances for timing or guarding
 * methods.
 */
public interface Profiler {

    /**
     * Create a new profile object.
     */
    Profile create(String name);
}

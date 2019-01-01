package com.google.android.camera.stats.profiler;

/**
 * Create profile instances for timing or guarding methods.
 */
public interface Profiler {

    /**
     * Create a new profile object.
     */
    Profile create(String name);
}

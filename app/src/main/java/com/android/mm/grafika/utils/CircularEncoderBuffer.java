package com.android.mm.grafika.utils;

import com.android.mm.grafika.GrafikaActivity;

/**
 * Holds encoded video data in a circular buffer.
 * <p>
 * This is actually a pair of circular buffers, one for the raw data and one for the meta-data
 * (flags and PTS).
 * <p>
 * Not thread-safe.
 */
public class CircularEncoderBuffer {

    private static final String TAG = GrafikaActivity.TAG;
    private static final boolean EXTRA_DEBUG = true;
    private static final boolean VERBOSE = false;


}

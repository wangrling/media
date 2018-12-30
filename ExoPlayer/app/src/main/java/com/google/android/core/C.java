package com.google.android.core;

public class C {

    /**
     * Special constant representing a time corresponding to the end of a source. Suitable for use in
     * any time base.
     */
    public static final long TIME_END_OF_SOURCE = Long.MIN_VALUE;

    /**
     * Special constant representing a time corresponding to the end of a source. Suitable for use in
     * any time base.
     * 最小负数+1
     */
    public static final long TIME_UNSET = Long.MIN_VALUE + 1;

    public static long usToMs(long timeUs) {
        return (timeUs == TIME_UNSET || timeUs == TIME_END_OF_SOURCE) ? timeUs : (timeUs / 1000);
    }
}

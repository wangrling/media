package com.android.media.player.core.util;

import android.os.Build;

/**
 * Miscellaneous utility methods.
 */
public final class Util {

    /**
     * Like {@link Build.VERSION#SDK_INT}, but in a place where it can be conveniently
     * overridden for local testing.
     */
    public static final int SDK_INT = Build.VERSION.SDK_INT;

    /**
     * Like {@link Build#DEVICE}, but in a place where it can be conveniently overridden for
     * local testing.
     */
    public static final String DEVICE = Build.DEVICE;

    /**
     * Like {@link Build#MANUFACTURER}, but in a place where it can be conveniently overriden for
     * local testing.
     */
    public static final String MANUFACTURER = Build.MANUFACTURER;

    /**
     * Like {@link Build#MODEL}, but in a place where it can be conveniently overridden for local
     * testing.
     */
    public static final String MODEL = Build.MODEL;

    /**
     * A concise description of the device that is can be useful to log for debugging purposes.
     */
    public static final String DEVICE_DEBUG_INFO = DEVICE + ", " + MODEL + ", " + MANUFACTURER + ", "
            + SDK_INT;

    /**
     * An empty byte array.
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private static final String TAG = "Util";


}

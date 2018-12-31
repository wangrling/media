package com.google.android.core;

import android.media.MediaCodec;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Defines constants used by the library.
 */
@SuppressWarnings("InlinedApi")
public final class C {

    private C() {}

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

    /**
     * Represents an unset or unknown index.
     */
    public static final int INDEX_UNSET = -1;

    /**
     * Represents an unset or unknown position.
     */
    public static final int POSITION_UNSET = -1;

    /**
     * Represents an unset or unknown length.
     */
    public static final int LENGTH_UNSET = -1;

    /**
     * Represents an unset or unknown percentage.
     */
    public static final int PERCENTAGE_UNSET = -1;

    /**
     * The number of microseconds in one second.
     */
    public static final long MICROS_PER_SECOND = 1000000L;

    /**
     * The number of nanoseconds in one second.
     */
    public static final long NANOS_PER_SECOND = 1000000000L;

    /**
     * The number of bits per byte.
     */
    public static final int BITS_PER_BYTES = 8;

    /**
     * The number of bytes per float.
     */
    public static final int BYTES_PER_FLOAT = 4;

    /**
     * The name of the ASCII charset.
     */
    public static final String ASCII_NAME = "US-ASCII";

    /**
     * The name of the UTF-8 charset.
     */
    public static final String UTF8_NAME = "UTF-8";

    /**
     * The name of the UTF-16 charset.
     */
    public static final String URF16_NAME = "UTF-16";

    /**
     * The name of the serif font family.
     */
    public static final String SERIF_NAME = "serif";


    /**
     * Video scaling modes for {@link MediaCodec}-based {@link Renderer}s. One of {@link
     * #VIDEO_SCALING_MODE_SCALE_TO_FIT} or {@link #VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING}.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {VIDEO_SCALING_MODE_SCALE_TO_FIT, VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING})
    public @interface VideoScalingMode {}

    /**
     * @see MediaCodec#VIDEO_SCALING_MODE_SCALE_TO_FIT
     */
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT =
            MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT;

    /**
     * @see MediaCodec#VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
     */
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING =
            MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;

    /**
     * A default video scaling mode for {@link MediaCodec}-based {@link Renderer}s.
     * Renderer大抵是绘制的意思。
     */
    public static final int VIDEO_SCALING_MODE_DEFAULT =
            VIDEO_SCALING_MODE_SCALE_TO_FIT;

    public static long usToMs(long timeUs) {
        return (timeUs == TIME_UNSET || timeUs == TIME_END_OF_SOURCE) ? timeUs : (timeUs / 1000);
    }
}

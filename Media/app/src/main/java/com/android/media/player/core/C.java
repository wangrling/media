package com.android.media.player.core;

/**
 * Defines constants used by the library.
 */
@SuppressWarnings("InlinedApi")
public class C {

    private C() {}

    /**
     * Special constant representing a time corresponding to the end of a source. Suitable for use
     * in any time base.
     */
    public static final long TIME_END_OF_SOURCE = Long.MIN_VALUE;

    /**
     * Special constant representing an unset or unknown time or duration. Suitable for use in any
     * time base.
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

    // 1 second = 1000 millis = 1000000 micros = 1000000000 nanos

    /**
     * The number of microseconds in one second.
     */
    public static final long MICROS_PER_SECOND = 1000000L;

    /**
     * The number of nanoseconds in one second.
     */
    public static final long NANOS_PER_SECOND = 1000000000L;

    /** The number of bits per bytes. */
    public static final int BITS_PER_BYTE = 8;

    /** The number of bytes per float. */
    public static final int BYTES_PER_FLOAT = 4;

    /**
     * The name of the ASCII charset.
     */
    public static final String ASCII_NAME = "US-ASCII";

    /**
     * UTF-16采用双字节，UTF-8采用可变字节，会根据前面的bit数值来决定采用几个字节。
     * The first 128 characters (US-ASCII) need one byte. The next 1,920 characters need two bytes
     * to encode, which covers the remainder of almost all Latin-script alphabets, and also Greek,
     * Cyrillic, Coptic, Armenian, Hebrew, Arabic, Syriac, Thaana and N'Ko alphabets, as well as
     * Combining Diacritical Marks. Three bytes are needed for characters in the rest of the Basic
     * Multilingual Plane, which contains virtually all characters in common use[13] including most
     * Chinese, Japanese and Korean characters. Four bytes are needed for characters in the other
     * planes of Unicode, which include less common CJK characters, various historic scripts,
     * mathematical symbols, and emoji (pictographic symbols).
     */

    /**
     * The name of the UTF-8 charset.
     */
    public static final String UTF_8_NAME = "UTF-8";

    /**
     * The name of the UTF-16 charset.
     */
    public static final String UTF16_NAME = "UTF-16";

    /**
     * The name of the serif font family.
     */
    public static final String SERIF_NAME = "serif";

    /**
     * The name of the sans-serif font family.
     */
    public static final String SANS_SERIF_NAME = "sans-serif";
}

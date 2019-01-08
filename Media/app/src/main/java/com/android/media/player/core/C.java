package com.android.media.player.core;

import android.media.AudioAttributes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

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

    /**
     * Content types for {@link com.android.media.player.core.audio.AudioAttributes}. One of
     * {@link #CONTENT_TYPE_MOVIE}, {@link #CONTENT_TYPE_MUSIC}, {@link #CONTENT_TYPE_SONIFICATION},
     * {@link #CONTENT_TYPE_SPEECH} or {@link #CONTENT_TYPE_UNKNOWN}.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            CONTENT_TYPE_MOVIE,
            CONTENT_TYPE_MUSIC,
            CONTENT_TYPE_SONIFICATION,
            CONTENT_TYPE_SPEECH,
            CONTENT_TYPE_UNKNOWN
    })
    public @interface AudioContentType {}

    /**
     * @see AudioAttributes#CONTENT_TYPE_MOVIE
     */
    public static final int CONTENT_TYPE_MOVIE = AudioAttributes.CONTENT_TYPE_MOVIE;
    /**
     * @see AudioAttributes#CONTENT_TYPE_MUSIC
     */
    public static final int CONTENT_TYPE_MUSIC = AudioAttributes.CONTENT_TYPE_MUSIC;
    /**
     * @see AudioAttributes#CONTENT_TYPE_SONIFICATION
     */
    public static final int CONTENT_TYPE_SONIFICATION = AudioAttributes.CONTENT_TYPE_SONIFICATION;
    /**
     * @see AudioAttributes#CONTENT_TYPE_SPEECH
     */
    public static final int CONTENT_TYPE_SPEECH = AudioAttributes.CONTENT_TYPE_SPEECH;
    /**
     * @see AudioAttributes#CONTENT_TYPE_UNKNOWN
     */
    public static final int CONTENT_TYPE_UNKNOWN = AudioAttributes.CONTENT_TYPE_UNKNOWN;

    /**
     * audibility 可听性
     * Flags for {@link com.android.media.player.core.audio.AudioAttributes}. Possible flag value
     * is {@link #FlAG_AUDIBILITY_ENFORCED}.
     *
     * <p>Note that {@link AudioAttributes#FLAG_HW_AV_SYNC} is not available because the player
     * takes care of setting the flag when tunneling is enabled via a track selector.</p>
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    // flag属性
    @IntDef(
            flag = true,
            value = {FlAG_AUDIBILITY_ENFORCED})
    public @interface AudioFlags {}
    /**
     * @see AudioAttributes#FLAG_AUDIBILITY_ENFORCED
     */
    public static final int FlAG_AUDIBILITY_ENFORCED = AudioAttributes.FLAG_AUDIBILITY_ENFORCED;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            USAGE_ALARM,
            USAGE_ASSISTANCE_ACCESSIBILITY,
            USAGE_ASSISTANCE_NAVIGATION_GUIDANCE,
            USAGE_ASSISTANCE_SONIFICATION,
            USAGE_ASSISTANT,
            USAGE_GAME,
            USAGE_MEDIA,
            USAGE_NOTIFICATION,
            USAGE_NOTIFICATION_COMMUNICATION_DELAYED,
            USAGE_NOTIFICATION_COMMUNICATION_INSTANT,
            USAGE_NOTIFICATION_COMMUNICATION_REQUEST,
            USAGE_NOTIFICATION_EVENT,
            USAGE_NOTIFICATION_RINGTONE,
            USAGE_UNKNOWN,
            USAGE_VOICE_COMMUNICATION,
            USAGE_VOICE_COMMUNICATION_SIGNALLING
    })
    public @interface AudioUsage {}

    // 分得真细致

    public static final int USAGE_ALARM = AudioAttributes.USAGE_ALARM;

    public static final int USAGE_ASSISTANCE_ACCESSIBILITY =
            AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY;

    public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE =
            AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE;

    public static final int USAGE_ASSISTANCE_SONIFICATION =
            AudioAttributes.USAGE_ASSISTANCE_SONIFICATION;

    public static final int USAGE_ASSISTANT =
            AudioAttributes.USAGE_ASSISTANT;

    public static final int USAGE_GAME = AudioAttributes.USAGE_GAME;

    public static final int USAGE_MEDIA = AudioAttributes.USAGE_MEDIA;

    public static final int USAGE_NOTIFICATION = AudioAttributes.USAGE_NOTIFICATION;

    public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED =
            AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED;

    public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT =
            AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT;

    public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST =
            AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST;

    public static final int USAGE_NOTIFICATION_EVENT =
            AudioAttributes.USAGE_NOTIFICATION_EVENT;

    public static final int USAGE_NOTIFICATION_RINGTONE =
            AudioAttributes.USAGE_NOTIFICATION_RINGTONE;

    public static final int USAGE_UNKNOWN =
            AudioAttributes.USAGE_UNKNOWN;

    public static final int USAGE_VOICE_COMMUNICATION =
            AudioAttributes.USAGE_VOICE_COMMUNICATION;

    public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING =
            AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING;


}

package com.android.media.player.core;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

import androidx.annotation.IntDef;

/**
 * Defines constants used by the library.
 */

public final class C {

    private C() {

    }

    /**
     * Special constant representing a time corresponding to the end of a source. Suitable for use
     * in any time base.
     */
    public static final long TIME_END_OFS_SOURCE = Long.MIN_VALUE;

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
     * Represents an unset or unknown length.
     */
    public static final int PERCENTAGE_UNSET = -1;

    /**
     * The number of microseconds in one second.
     */
    public static final long MICRO_PER_SECOND = 1000000L;

    /**
     * The number of nanoseconds in one second.
     */
    public static final long NANOS_PER_SECOND = 1000000000L;

    /**
     * The number of bits per byte.
     */
    public static final int BITS_PER_BYTE = 0;

    /**
     * The number of bytes per float.
     */
    public static final int BYTES_PER_FLOAT = 4;

    /**
     * The name of the ASCII charset.
     */
    public static final String ASCII_NAME = "US-ASCII";

    // UTF-8是可变字节，可以使用1-4字节，根据前几位的值。

    /**
     * The name of the UTF-8 charset.
     */
    public static final String UTF8_NAME = "UTF-8";

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
     * Crypto modes for a codec.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            MediaCodec.CRYPTO_MODE_UNENCRYPTED,
            MediaCodec.CRYPTO_MODE_AES_CTR,
            MediaCodec.CRYPTO_MODE_AES_CBC
    })
    public @interface CryptoMode {

    }

    /**
     * Represents an unset {@link android.media.AudioTrack} session identifier. Equal to
     * {@link AudioManager#AUDIO_SESSION_ID_GENERATE}.
     */
    public static final int AUDIO_SESSION_ID_UNSET = AudioManager.AUDIO_SESSION_ID_GENERATE;

    /**
     * Represents an audio encoding, or an invalid or unset value.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Format.NO_VALUE,
            AudioFormat.ENCODING_INVALID,
            AudioFormat.ENCODING_PCM_8BIT,
            AudioFormat.ENCODING_PCM_16BIT,
            ENCODING_PCM_24_BIT,
            ENCODING_PCM_32_BIT,
            AudioFormat.ENCODING_PCM_FLOAT,
            ENCODING_PCM_MU_LAW,
            ENCODING_PCM_A_LAW,
            AudioFormat.ENCODING_AC3,
            AudioFormat.ENCODING_E_AC3,
            AudioFormat.ENCODING_DTS,
            AudioFormat.ENCODING_DTS_HD,
            AudioFormat.ENCODING_DOLBY_TRUEHD
    })
    public @interface Encoding {

    }

    /**
     * Represents a PCM audio encoding, or an invalid or unset value.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Format.NO_VALUE,
            AudioFormat.ENCODING_INVALID,
            AudioFormat.ENCODING_PCM_8BIT,
            AudioFormat.ENCODING_PCM_16BIT,
            ENCODING_PCM_24_BIT,
            ENCODING_PCM_32_BIT,
            AudioFormat.ENCODING_PCM_FLOAT,
            ENCODING_PCM_MU_LAW,
            ENCODING_PCM_A_LAW
    })
    public @interface PcmEncoding {

    }

    /**
     * PCM encoding with 24 bits per sample.
     */
    public static final int ENCODING_PCM_24_BIT = 0x80000000;
    /**
     * PCM encoding with 32 bits per sample.
     */
    public static final int ENCODING_PCM_32_BIT = 0x40000000;
    /**
     * Audio encoding for MU-law.
     */
    public static final int ENCODING_PCM_MU_LAW = 0x10000000;
    /**
     * Audio encoding for A-law.
     */
    public static final int ENCODING_PCM_A_LAW = 0x20000000;

    /**
     * Stream types for an {@link android.media.AudioTrack}.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            AudioManager.STREAM_ALARM,
            AudioManager.STREAM_DTMF,
            AudioManager.STREAM_MUSIC,
            AudioManager.STREAM_NOTIFICATION,
            AudioManager.STREAM_RING,
            AudioManager.STREAM_SYSTEM,
            AudioManager.STREAM_VOICE_CALL,
            AudioManager.USE_DEFAULT_STREAM_TYPE
    })
    public @interface StreamType {

    }

    /**
     * The default stream type used by audio renderers.
     */
    public static final int STREAM_TYPE_DEFAULT = AudioManager.STREAM_MUSIC;

    /**
     * Content types for {@link com.android.media.player.core.audio.AudioAttributes}.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            AudioAttributes.CONTENT_TYPE_MOVIE,
            AudioAttributes.CONTENT_TYPE_MUSIC,
            AudioAttributes.CONTENT_TYPE_SONIFICATION,
            AudioAttributes.CONTENT_TYPE_SPEECH,
            AudioAttributes.CONTENT_TYPE_UNKNOWN
    })
    public @interface AudioContentType {

    }

    /**
     * Flags for {@link com.android.media.player.core.audio.AudioAttributes}.
     *
     * <p>Note that {@code FLAG_HW_AV_SYNC} is not available because the player takes care of setting
     * the flag when tunneling is enabled via a track selector.</p>
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(
            flag = true,
            value = {AudioAttributes.FLAG_AUDIBILITY_ENFORCED}
    )
    public @interface AudioFlags {

    }

    /**
     * Usage types for {@link com.android.media.player.core.audio.AudioAttributes}
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            AudioAttributes.USAGE_ALARM,
            AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY,
            AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE,
            AudioAttributes.USAGE_ASSISTANCE_SONIFICATION,
            AudioAttributes.USAGE_ASSISTANT,
            AudioAttributes.USAGE_GAME,
            AudioAttributes.USAGE_MEDIA,
            AudioAttributes.USAGE_NOTIFICATION,
            AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED,
            AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT,
            AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST,
            AudioAttributes.USAGE_NOTIFICATION_EVENT,
            AudioAttributes.USAGE_NOTIFICATION_RINGTONE,
            AudioAttributes.USAGE_UNKNOWN,
            AudioAttributes.USAGE_VOICE_COMMUNICATION,
            AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING
    })
    public @interface AudioUsage {

    }

    /**
     * Audio focus types.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            AudioManager.AUDIOFOCUS_NONE,
            AudioManager.AUDIOFOCUS_GAIN,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE
    })
    public @interface AudioFocusGain {

    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(
            flag = true,
            value = {
                    // Indicates that a buffer holds a synchronization sample.
                    MediaCodec.BUFFER_FLAG_KEY_FRAME,
                    // Flag for empty buffer that signal that the end of the stream was reached.
                    MediaCodec.BUFFER_FLAG_END_OF_STREAM,
                    BUFFER_FLAG_ENCRYPTED,
                    BUFFER_FLAG_DECODE_ONLY
            })
    public @interface BufferFlags {

    }

    /**
     * Indicates that a buffer is (at least partially) encrypted.
     */
    public static final int BUFFER_FLAG_ENCRYPTED = 1 << 30;    // 0X40000000
    /**
     * Indicates that a buffer should be decoded but not rendered.
     */
    @SuppressWarnings("NumericOverflow")
    public static final int BUFFER_FLAG_DECODE_ONLY = 1 << 31;  // 0x80000000

    /**
     * Video scaling mode for {@link MediaCodec}-based {@link Renderer}s.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(
            value = {
                    MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT,
                    MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            })
    public @interface VideoScalingMode {

    }

    /**
     * A default video scaling mode for {@link MediaCodec}-based {@link Renderer}s.
     */
    public static final int VIDEO_SCALING_MODE_DEFAULT =
            MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(
            flag = true,
            value = {
                    SELECTION_FLAG_DEFAULT,
                    SELECTION_FLAG_FORCED,
                    SELECTION_FLAG_AUTOSELECT
            })
    public @interface SelectionFlags {

    }

    /**
     * Indicates that the track should be selected if user preferences do not state otherwise.
     */
    public static final int SELECTION_FLAG_DEFAULT = 1;
    /**
     * Indicates that the track must be displayed. Only applies to text tracks.
     */
    public static final int SELECTION_FLAG_FORCED = 1 << 1;     // 2
    /**
     * Indicates that the player may choose to play the track in absence of an explicit user
     * preference.
     */
    public static final int SELECTION_FLAG_AUTOSELECT = 1 << 2;     // 4

    /**
     * Represents an undetermined language as an ISO 639 alpha-3 language code.
     */
    public static final String LANGUAGE_UNDETERMINED = "und";

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TYPE_DASH,
            TYPE_SS,
            TYPE_HLS,
            TYPE_OTHER
    })
    public @interface ContentType {

    }

    /**
     * Value returned by {@link Util#inferContentType(String)} for DASH manifests.
     */
    public static final int TYPE_DASH = 0;
    /**
     * Value returned by {@link Util#inferContentType(String)} for Smooth Streaming manifest.
     */
    public static final int TYPE_SS = 1;

    /**
     * Value returned by {@link Util#inferContentType(String)} for HLS manifests.
     */
    public static final int TYPE_HLS = 2;

    /**
     * Value returned by {@link Util#inferContentType(String)} for files other than DASH, HLS or
     * Smooth Streaming manifests.
     */
    public static final int TYPE_OTHER = 3;

    /**
     * A return value for methods where the end of an input was encountered.
     */
    public static final int RESULT_END_OF_INPUT = -1;
    /**
     * A return value for methods where the length of parsed data exceeds the maximum length allowed.
     */
    public static final int RESULT_MAX_LENGTH_EXCEEDED = -2;
    /**
     * A return value for methods where nothing was read.
     */
    public static final int RESULT_NOTHING_READ = -3;
    /**
     * A return value for methods where a buffer was read.
     */
    public static final int RESULT_BUFFER_READ = -4;
    /**
     * A return value for methods where a format was read.
     */
    public static final int RESULT_FORMAT_READ = -5;

    /**
     * A data type constant for data of unknown or unspecified type.
     */
    public static final int DATA_TYPE_UNKNOWN = 0;
    /**
     * A data type constant for media, typically containing media samples.
     */
    public static final int DATA_TYPE_MEDIA = 1;
    /**
     * A data type constant for media, typically containing only initialization data.
     */
    public static final int DATA_TYPE_INITIALIZATION = 2;
    /**
     * A data type constant for drm or encryption data.
     */
    public static final int DATA_TYPE_DRM = 3;
    /**
     * A data type constant for a manifest file.
     */
    public static final int DATA_TYPE_MANIFEST = 4;
    /**
     * A data type constant for time synchronization data.
     */
    public static final int DATA_TYPE_TIME_SYNCHRONIZATION = 5;
    /**
     * A data type constant for ads loader data.
     */
    public static final int DATA_TYPE_AD = 6;
    /**
     * A data type constants for live progressive media streams, typically containing media samples.
     */
    public static final int DATA_TYPE_MEDIA_PROGRESSIVE_LIVE = 7;
    /**
     * Applications or extensions may define custom {@code DATA_TYPE_*} constants  greater than or
     * equal to this value.
     */
    public static final int DATA_TYPE_CUSTOM_BASE = 10000;

    /**
     * A selection reason constant for selections whose reasons are unknown or unspecified.
     */
    public static final int SELECTION_REASON_UNKNOWN = 0;
    /**
     * A selection reason constant for an initial track selection.
     */
    public static final int SELECTION_REASON_INITIAL = 1;
    /**
     * A selection reason constant for an manual (i.e. user initiated) track selection.
     */
    public static final int SELECTION_REASON_MANUAL = 2;
    /**
     * A selection reason constant for an adaptive track selection.
     */
    public static final int SELECTION_REASON_ADAPTIVE = 3;
    /**
     * A selection reason constant for a trick play track selection.
     */
    public static final int SELECTION_REASON_TRICK_PLAY = 4;
    /**
     * Applications or extensions may define custom {@code SELECTION_REASON_*} constants greater
     * than or equal to this value.
     */
    public static final int SELECTION_REASON_CUSTOM_BASE = 10000;

    /**
     * A default size in bytes for an individual allocation that forms part of a larger buffer.
     */
    public static final int DEFAULT_BUFFER_SEGMENT_SIZE = 64 * 1024;
    /**
     * A default size in bytes for a video buffer.
     */
    public static final int DEFAULT_VIDEO_BUFFER_SIZE = 200 * DEFAULT_BUFFER_SEGMENT_SIZE;
    /**
     * A default size in bytes for an audio buffer.
     */
    public static final int DEFAULT_AUDIO_BUFFER_SIZE = 54 * DEFAULT_BUFFER_SEGMENT_SIZE;
    /**
     * A default size in bytes for a text buffer.
     */
    public static final int DEFAULT_TEXT_BUFFER_SIZE = 2 * DEFAULT_BUFFER_SEGMENT_SIZE;
    /**
     * A default size in bytes for a metadata buffer.
     */
    public static final int DEFAULT_METADATA_BUFFER_SIZE = 2 * DEFAULT_BUFFER_SEGMENT_SIZE;
    /**
     * A default size in bytes for a camera motion buffer.
     */
    public static final int DEFAULT_CAMERA_MOTION_BUFFER_SIZE = 2 * DEFAULT_BUFFER_SEGMENT_SIZE;
    /**
     * A default size in bytes for a muxed buffer (e.g. containing video, audio and text).
     */
    public static final int DEFAULT_MUXED_BUFFER_SIZE =
            DEFAULT_VIDEO_BUFFER_SIZE + DEFAULT_AUDIO_BUFFER_SIZE + DEFAULT_TEXT_BUFFER_SIZE;

    /**
     * Scheme type name as defined in ISO/IEC 23001-7:2016.
     */
    @SuppressWarnings("ConstantField")
    public static final String CENC_TYPE_cenc = "cenc";

    @SuppressWarnings("ConstantField")
    public static final String CENC_TYPE_cbc1 = "cbc1";

    @SuppressWarnings("ConstantField")
    public static final String CENC_TYPE_cens = "cens";

    @SuppressWarnings("ConstantField")
    public static final String CENC_TYPE_cbcs = "cbcs";

    /**
     * The nil UUID is special form of UUID that is specified to have all 128 bits set to zero.
     * defined by <a href="https://tools.ietf.org/html/rfc4122#section-4.1.7">RFC4122</a>
     */
    public static final UUID UUID_NIL = new UUID(0L, 0L);

    /**
     * The SystemID is 1077efec-c0b2-4d02-ace3-3c1e52e2fb4b.
     * defined by <a href="https://w3c.github.io/encrypted-media/format-registry/initdata/cenc.html">
     *     Common PSSH box</a>
     */
    public static final UUID COMMON_PSSH_UUID = new UUID(0x1077EFECC0B24D02L, 0xACE33C1E52E2FB4BL);

    /**
     * UUID for the ClearKey DRM scheme.
     *
     * <p>ClearKey is supported on Android devices running Android 5.0 (API Level 21) and up.</p>
     */
    public static final UUID CLEARKEY_UUID = new UUID(0xE2719D58A985B3C9L, 0x781AB030AF78D30EL);

    /**
     * UUID for the Widevine DRM scheme.
     *
     * <p>Widevine is supported on Android devices running Android 4.3 (API level 18) and up.</p>
     */
    public static final UUID WIDEVINE_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    /**
     * UUID for the PlayReady DRM scheme.
     *
     * <p>PlayReady is supported on all AndroidTV devices. Note that most other Android devices do not
     * provide PlayReady support.</p>
     */
    public static final UUID PLAYREADY_UUID = new UUID(0x9A04F07998404286L, 0xAB92E65BE0885F95L);

    /**
     * The type of a message that can be passed to a video {@link Renderer} via
     * {@link ExoPlayer#createMessage(Target)}. The message payload should be the target
     * {@link Surface}, or null.
     */
    public static final int MSG_SET_SURFACE = 1;
    /**
     * A type of a message that can be passed to an audio {@link Renderer} via
     * {@link ExoPlayer#createMessage(Target)}. The message payload should be a {@link Float} with 0
     * being silence and 1 being unity gain.
     */
    public static final int MSG_SET_VOLUME = 2;

    /**
     * A type of a message that can be passed to an audio {@link Renderer} via
     * {@link ExoPlayer#createMessage(Target)}. The message payload should be an
     * {@link com.android.media.player.core.audio.AudioAttributes} instance that will configure the
     * underlying audio track. If not set, the default audio attributes will be used. They are
     * suitable for general media playback.
     */
    public static final int MSG_SET_AUDIO_ATTRIBUTES = 3;

    /**
     * The type of a message that can be passed to a {@link MediaCodec}-based video {@link Renderer}
     * via {@link ExoPlayer#createMessage(Target)}. The message payload should be one of the integer
     * scaling modes in {@link C.VideoScalingMode}.
     */
    public static final int MSG_SET_SCALING_MODE = 4;

    /**
     * A type of a message that can be passed to an audio {@link Renderer} via
     * {@link ExoPlayer#createMessage(Target)}. The messaged payload should be an
     * {@link com.android.media.player.core.audio.AuxEffectInfo} instance representing an auxiliary
     * audio effect for the underlying audio track.
     */
    public static final int MSG_SET_AUX_EFFECT_INFO = 5;

    /**
     * The type of a message that can be passed to a video {@link Renderer} via
     * {@link ExoPlayer#createMessage(Target)}. The message payload should be a
     * {@link VideoFrameMetadataListener} instance, or null.
     */
    public static final int MSG_SET_VIDEO_FRAME_METADATA_LISTENER = 6;

    /**
     * The type of a message that can be passed to a camera motion {@link Renderer} via
     * {@link ExoPlayer#createMessage(Target)}. The message payload should be a
     * {@link CameraMotionListener} instance, or null.
     */
    public static final int MSG_SET_CAMERA_MOTION_LISTENER = 7;

    /**
     * Applications or extensions may define custom {@code MSG_*} constants that can be passed to
     * {@link Renderer}s. These custom constants must be greater than or equal to this value.
     */
    public static final int MSG_CUSTOM_BASE = 10000;

    /**
     * The stereo mode for 360/3D/VR videos.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Format.NO_VALUE,
            STEREO_MODE_MONO,
            STEREO_MODE_TOP_BOTTOM,
            STEREO_MODE_LEFT_RIGHT,
            STEREO_MODE_STEREO_MESH
    })
    public @interface StereoMode {

    }

    /**
     * Indicates Monoscopic stereo layout, used with 360/3D/VR videos.
     */
    public static final int STEREO_MODE_MONO = 0;

    public static final int STEREO_MODE_TOP_BOTTOM = 1;

    public static final int STEREO_MODE_LEFT_RIGHT = 2;

    public static final int STEREO_MODE_STEREO_MESH = 3;

    /**
     * Video color spaces.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Format.NO_VALUE,
            MediaFormat.COLOR_STANDARD_BT709,
            MediaFormat.COLOR_STANDARD_BT601_PAL,
            MediaFormat.COLOR_STANDARD_BT2020
    })
    public @interface ColorSpace {

    }

    /**
     * Video color transfer characteristics.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Format.NO_VALUE,
            MediaFormat.COLOR_TRANSFER_SDR_VIDEO,
            MediaFormat.COLOR_TRANSFER_ST2084,
            MediaFormat.COLOR_TRANSFER_HLG
    })
    public @interface ColorTransfer {

    }

    /**
     * Video color range.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Format.NO_VALUE,
            MediaFormat.COLOR_RANGE_LIMITED,
            MediaFormat.COLOR_RANGE_FULL
    })
    public @interface ColorRange {

    }

    /**
     * Priority for media playback.
     *
     * <p>Larger values indicate higher priorities.</p>
     */
    public static final int PRIORITY_PLAYBACK = 0;

    /**
     * Priority for media downloading.
     *
     * <p>Larger values indicates higher priorities.</p>
     */
    public static final int PRIORITY_DOWNLOAD = PRIORITY_PLAYBACK - 1000;

    /**
     * Network connection type.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            NETWORK_TYPE_UNKNOWN,
            NETWORK_TYPE_OFFLINE,
            NETWORK_TYPE_WIFI,
            NETWORK_TYPE_2G,
            NETWORK_TYPE_3G,
            NETWORK_TYPE_4G,
            NETWORK_TYPE_CELLULAR_UNKNOWN,
            NETWORK_TYPE_ETHERNET,
            NETWORK_TYPE_OTHER
    })
    public @interface NetworkType {

    }

    /**
     * Unknown network type.
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * No network connection.
     */
    public static final int NETWORK_TYPE_OFFLINE = 1;
    /**
     * Network type for a Wifi connection.
     */
    public static final int NETWORK_TYPE_WIFI = 2;
    /**
     * Network type for a 2G cellular connection.
     */
    public static final int NETWORK_TYPE_2G = 3;
    /**
     * Network type for a 3G cellular connection.
     */
    public static final int NETWORK_TYPE_3G = 4;
    /**
     * Network type for a 4G cellular connection.
     */
    public static final int NETWORK_TYPE_4G = 5;
    /**
     * Network type for cellular connection which cannot be mapped to one of
     * {@link #NETWORK_TYPE_2G}, {@link #NETWORK_TYPE_3G}, or {@link #NETWORK_TYPE_4G}.
     */
    public static final int NETWORK_TYPE_CELLULAR_UNKNOWN = 6;
    /**
     * Network type for an Ethernet connection.
     */
    public static final int NETWORK_TYPE_ETHERNET = 7;
    /**
     * Network type for other connections which are not Wifi or cellular (e.g. Ethernet,
     * VPN, Bluetooth).
     */
    public static final int NETWORK_TYPE_OTHER = 8;

    /**
     * Converts a time in microseconds to a corresponding time in milliseconds, preserving
     * {@link #TIME_UNSET} and {@link #TIME_END_OFS_SOURCE}.
     *
     * @param timeUs    The time in microseconds.
     * @return  The corresponding time in milliseconds.
     */
    public static long usToMs(long timeUs) {
        return (timeUs == TIME_UNSET || timeUs == TIME_END_OFS_SOURCE) ? timeUs : (timeUs / 1000);
    }

    /**
     * @param timeMs    The time in milliseconds.
     * @return  The corresponding time in microseconds.
     */
    public static long msToUs(long timeMs) {
        return (timeMs == TIME_UNSET || timeMs == TIME_END_OFS_SOURCE) ? timeMs : (timeMs * 1000);
    }

    /**
     * Returns a newly generated audio session identifier, or {@link AudioManager#ERROR} if
     * an error occurred in which cause audio playback may fail.
     *
     * @see AudioManager#generateAudioSessionId()
     */
    public static int generateAudioSessionIdV21(Context context) {
        return ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE))
                .generateAudioSessionId();
    }
}

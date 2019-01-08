package com.android.media.player.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.media.player.core.drm.DrmInitData;
import com.android.media.player.core.metadata.Metadata;
import com.android.media.player.core.util.MimeTypes;
import com.android.media.player.core.util.Util;
import com.android.media.player.core.video.ColorInfo;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import androidx.annotation.IntDef;

/**
 * Representation of a media format.
 */

// 包括视频，音频，文本。

public final class Format implements Parcelable {

    /**
     * A value for various fields to indicate that the field's value is unknown or not applicable.
     */
    public static final int NO_VALUE = -1;

    /**
     * A value for {@link #subsampleOffsetUs} to indicate that subsample timestamps are relative to
     * the timestamps of their parent samples.
     */
    public static final long OFFSET_SAMPLE_RELATIVE = Long.MAX_VALUE;

    /** An identifier for the format, or null if unknown or not applicable. */
    public final @Nullable String id;

    /** The human readable label, or null if unknown or not applicable. */
    public final @Nullable String label;

    /**
     * The average bandwidth in bits per second, or {@link #NO_VALUE} if unknown or not applicable.
     */
    public final int bitrate;

    /** Codecs of the format as described in RFC 6381, or null if unknown or not applicable. */
    public final @Nullable String codecs;

    /** Metadata, or null if unknown or not applicable. */
    public final @Nullable Metadata metadata;

    // Container specific.

    /** The mime type of the container, or null if unknown or not applicable. */
    public final @Nullable String containerMimeType;

    // Elementary stream specific.

    /**
     * The mime type of the elementary stream (i.e. the individual samples), or null if unknown or not
     * applicable.
     */
    public final @Nullable String sampleMimeType;

    /**
     * The maximum size of a buffer of data (typically one sample), or {@link #NO_VALUE} if unknown or
     * not applicable.
     */
    public final int maxInputSize;

    /**
     * Initialization data that must be provided to the decoder. Will not be null, but may be empty
     * if initialization data is not required.
     */
    public final List<byte[]> initializationData;

    /** DRM initialization data if the stream is protected, or null otherwise. */
    public final @Nullable DrmInitData drmInitData;

    /**
     * For samples that contain subsamples, this is an offset that should be added to subsample
     * timestamps. A value of {@link #OFFSET_SAMPLE_RELATIVE} indicates that subsample timestamps are
     * relative to the timestamps of their parent samples.
     */
    public final long subsampleOffsetUs;

    // Video specific.

    /**
     * The width of the video in pixels, or {@link #NO_VALUE} if unknown or not applicable.
     */
    public final int width;

    /**
     * The height of the video in pixels, or {@link #NO_VALUE} if unknown or not applicable.
     */
    public final int height;

    /**
     * The frame rate in frames per second, or {@link #NO_VALUE} if unknown or not applicable.
     */
    public final float frameRate;

    /**
     * The clockwise rotation that should be applied to the video for it to be rendered in the correct
     * orientation, or 0 if unknown or not applicable. Only 0, 90, 180 and 270 are supported.
     */
    public final int rotationDegrees;

    /** The width to height ratio of pixels in the video, or 1.0 if unknown or not applicable. */
    public final float pixelWidthHeightRatio;

    /**
     * The stereo layout for 360/3D/VR video, or {@link #NO_VALUE} if not applicable. Valid stereo
     * modes are {@link C#STEREO_MODE_MONO}, {@link C#STEREO_MODE_TOP_BOTTOM}, {@link
     * C#STEREO_MODE_LEFT_RIGHT}, {@link C#STEREO_MODE_STEREO_MESH}.
     */
    @C.StereoMode
    public final int stereoMode;

    /** The projection data for 360/VR video, or null if not applicable. */
    public final @Nullable byte[] projectionData;

    /** The color metadata associated with the video, helps with accurate color reproduction. */
    public final @Nullable
    ColorInfo colorInfo;

    // Audio specific

    /**
     * The number of audio channels, or {@link #NO_VALUE} if unknown or not applicable.
     */
    public final int channelCount;

    /**
     * The audio sampling rate in Hz, or {@link #NO_VALUE} if unknown or not applicable.
     */
    public final int sampleRate;

    /**
     * The encoding for PCM audio streams. If {@link #sampleMimeType} is {@link MimeTypes#AUDIO_RAW}
     * then one of {@link C#ENCODING_PCM_8BIT}, {@link C#ENCODING_PCM_16BIT}, {@link
     * C#ENCODING_PCM_24BIT}, {@link C#ENCODING_PCM_32BIT}, {@link C#ENCODING_PCM_FLOAT}, {@link
     * C#ENCODING_PCM_MU_LAW} or {@link C#ENCODING_PCM_A_LAW}. Set to {@link #NO_VALUE} for other
     * media types.
     */
    public final @C.PcmEncoding int pcmEncoding;

    /**
     * The number of frames to trim from the start of the decoded audio stream, or 0 if not
     * applicable.
     */
    public final int encoderDalay;

    /**
     * The number of frames to trim from the end of the decoded audio stream, or 0 if not applicable.
     */
    public final int encoderPadding;

    // Audio and text specific.

    /**
     * Track selection flags.
     */
    @C.SelectionFlags
    public final int selectionFlags;

    /** The language, or null if unknown or not applicable. */
    public final @Nullable String language;

    /**
     * The Accessibility channel, or {@link #NO_VALUE} if not known or applicable.
     */
    public final int accessibilityChannel;

    // Lazily initialized hashcode.
    private int hashCode;


    // 一端写，一端读。

    @SuppressWarnings("ResourceType")
    /* package */ Format(Parcel in) {
        id = in.readString();
        label = in.readString();
        containerMimeType = in.readString();
        sampleMimeType = in.readString();
        codecs = in.readString();
        bitrate = in.readInt();
        maxInputSize = in.readInt();
        width = in.readInt();
        height = in.readInt();
        frameRate = in.readFloat();
        rotationDegrees = in.readInt();
        pixelWidthHeightRatio = in.readFloat();
        boolean hasProjectionData = Util.readBoolean(in);
        projectionData = hasProjectionData ? in.createByteArray() : null;
        stereoMode = in.readInt();
        colorInfo = in.readParcelable(ColorInfo.class.getClassLoader());
        channelCount = in.readInt();
        sampleRate = in.readInt();
        pcmEncoding = in.readInt();
        encoderDalay = in.readInt();
        encoderPadding = in.readInt();
        selectionFlags = in.readInt();
        language = in.readString();
        accessibilityChannel = in.readInt();
        subsampleOffsetUs = in.readLong();
        int initializationDataSize = in.readInt();
        initializationData = new ArrayList<>(initializationDataSize);
        for (int i = 0; i < initializationDataSize; i++) {
            initializationData.add(in.createByteArray());
        }
        drmInitData = in.readParcelable(DrmInitData.class.getClassLoader());
        metadata = in.readParcelable(Metadata.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(label);
    }

    public static final Creator<Format> CREATOR = new Creator<Format>() {
        @Override
        public Format createFromParcel(Parcel in) {
            return new Format(in);
        }

        @Override
        public Format[] newArray(int size) {
            return new Format[size];
        }
    };
}

package com.android.media.player.core;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Nullable;

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

    /** The mime type of the container, or null if unknown or not applicable. */
    public final @Nullable String containerMimeType;

    // Elementary stream specific.



    protected Format(Parcel in) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}

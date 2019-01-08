package com.android.media.player.core.metadata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A collection of metadata entries.
 */

public final class Metadata implements Parcelable {
    protected Metadata(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Metadata> CREATOR = new Creator<Metadata>() {
        @Override
        public Metadata createFromParcel(Parcel in) {
            return new Metadata(in);
        }

        @Override
        public Metadata[] newArray(int size) {
            return new Metadata[size];
        }
    };
}

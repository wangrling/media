package com.google.android.core;

import android.os.Parcel;
import android.os.Parcelable;

public final class Format implements Parcelable {

    /**
     * A value for various fields to indicate that the field's value is unknown or not applicable.
     */
    public static final int NO_VALUE = -1;

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

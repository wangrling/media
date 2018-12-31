package com.google.android.core.source;

import android.os.Parcel;
import android.os.Parcelable;

// 跨进程通信
/** An array of {@link TrackGroup}s exposed by a {@link MediaPeriod}. */
public class TrackGroupArray implements Parcelable {
    protected TrackGroupArray(Parcel in) {
    }

    public static final Creator<TrackGroupArray> CREATOR = new Creator<TrackGroupArray>() {
        @Override
        public TrackGroupArray createFromParcel(Parcel in) {
            return new TrackGroupArray(in);
        }

        @Override
        public TrackGroupArray[] newArray(int size) {
            return new TrackGroupArray[size];
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

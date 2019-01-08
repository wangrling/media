package com.android.media.player.core.source;

import android.os.Parcel;
import android.os.Parcelable;

public class TrackGroupArray implements Parcelable {



    protected TrackGroupArray(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
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
}

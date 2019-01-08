package com.android.media.player.core.video;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Stores color info.
 */

public final class ColorInfo implements Parcelable {

    protected ColorInfo(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ColorInfo> CREATOR = new Creator<ColorInfo>() {
        @Override
        public ColorInfo createFromParcel(Parcel in) {
            return new ColorInfo(in);
        }

        @Override
        public ColorInfo[] newArray(int size) {
            return new ColorInfo[size];
        }
    };
}

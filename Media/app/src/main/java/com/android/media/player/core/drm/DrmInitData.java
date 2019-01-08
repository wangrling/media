package com.android.media.player.core.drm;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public final class DrmInitData implements Comparator<DrmInitData.SchemeData>, Parcelable {

    protected DrmInitData(Parcel in) {
    }

    public static final Creator<DrmInitData> CREATOR = new Creator<DrmInitData>() {
        @Override
        public DrmInitData createFromParcel(Parcel in) {
            return new DrmInitData(in);
        }

        @Override
        public DrmInitData[] newArray(int size) {
            return new DrmInitData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int compare(SchemeData o1, SchemeData o2) {
        return 0;
    }

    /**
     * Scheme initialization data.
     */
    public static final class SchemeData implements Parcelable {

        protected SchemeData(Parcel in) {
        }

        public static final Creator<SchemeData> CREATOR = new Creator<SchemeData>() {
            @Override
            public SchemeData createFromParcel(Parcel in) {
                return new SchemeData(in);
            }

            @Override
            public SchemeData[] newArray(int size) {
                return new SchemeData[size];
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
}

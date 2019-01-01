package com.google.android.camera.stats;

import android.content.Context;

public class UsageStatistics {

    private static UsageStatistics sInstance;

    public static UsageStatistics instance() {
        if (sInstance == null) {
            sInstance = new UsageStatistics();
        }
        return sInstance;
    }

    public void initialize(Context context) {

    }
}

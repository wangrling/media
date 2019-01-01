package com.google.android.camera.app;

import android.content.Context;

import com.google.android.camera.stats.profiler.Profile;
import com.google.android.camera.stats.profiler.Profiler;
import com.google.android.camera.stats.profiler.Profilers;

public class FirstRunDetector {
    private static final long UNKNOWN = -1;

    private final Profile mProfile;

    private static class Singleton {
        private static final  FirstRunDetector INSTANCE = new FirstRunDetector(
                Profilers.instance().guard());
    }


    private FirstRunDetector(Profiler profiler) {
        mProfile = profiler.create("FirstRunDetector getTimeOfFirstRun");
    }

    public static FirstRunDetector instance() {
        return Singleton.INSTANCE;
    }

    public void initializeTimeOfFirstRun(Context context) {

    }
}

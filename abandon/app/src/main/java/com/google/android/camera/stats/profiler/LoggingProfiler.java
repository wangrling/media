package com.google.android.camera.stats.profiler;

public class LoggingProfiler implements Profiler {
    private final Writer mWriter;

    /** Create a new LoggingProfiler */
    public LoggingProfiler(Writer writer) {
        mWriter = writer;
    }

    @Override
    public Profile create(String name) {
        LoggingProfile profile = new LoggingProfile(mWriter, name);
        profile.start();
        return profile;
    }
}

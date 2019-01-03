package com.android.live.camera.stats.profiler;
/**
 * A logging profiler creates profiles that will write all
 * output to the provided writer.
 */
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

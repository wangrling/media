package com.android.live.camera.stats.profiler;

/**
 * A guarding profiler creates new guarded profiles that
 * will only write output messages if the profile time
 * exceeds the threshold.
 */
public class GuardingProfiler implements Profiler {
    private static final int DEFAULT_GUARD_DURATION_MILLIS = 15;
    private final Writer mGuardWriter;
    private final Writer mVerboseWriter;
    private final int mMaxDurationMillis;

    /** Create a new GuardingProfiler */
    public GuardingProfiler(Writer writer, Writer verbose) {
        this(writer, verbose, DEFAULT_GUARD_DURATION_MILLIS);
    }

    /** Create a new GuardingProfiler with a given max duration. */
    public GuardingProfiler(Writer writer, Writer verbose, int maxDurationMillis) {
        mGuardWriter = writer;
        mVerboseWriter = verbose;
        mMaxDurationMillis = maxDurationMillis;
    }

    @Override
    public Profile create(String name) {
        return new GuardingProfile(mGuardWriter, mVerboseWriter, name,
              mMaxDurationMillis);
    }

    /** Start a new profile, but override the maxDuration */
    public Profile create(String name, int maxDurationMillis) {
        return new GuardingProfile(mGuardWriter, mVerboseWriter, name,
              maxDurationMillis);
    }
}

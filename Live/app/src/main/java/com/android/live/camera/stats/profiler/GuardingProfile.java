package com.android.live.camera.stats.profiler;

/**
 * A guarding profile will only write messages to a writer if the
 * time exceeds a given threshold. For mark() calls, it uses the
 * time since last event, and for stop() calls it uses the total
 * elapsed duration of the profile.
 */
public class GuardingProfile extends ProfileBase {
    private final Writer mGuardWriter;
    private final Writer mVerboseWriter;
    private final int mMaxMillis;

    public GuardingProfile(Writer writer, Writer verbose, String name,int maxDuration) {
        super(name);
        mGuardWriter = writer;
        mVerboseWriter = verbose;
        mMaxMillis = maxDuration;
    }

    @Override
    protected void onStart() {
        mVerboseWriter.write(format(0, "GUARD", "START"));
    }

    @Override
    protected void onMark(double totalMillis, double lastMillis, String reason) {
        if (lastMillis > mMaxMillis) {
            mGuardWriter.write(format(totalMillis, "GUARD", lastMillis, reason));
        } else {
            mVerboseWriter.write(format(totalMillis, "GUARD", lastMillis, reason));
        }
    }

    @Override
    protected void onStop(double totalMillis, double lastMillis) {
        if (totalMillis > mMaxMillis) {
            mGuardWriter.write(format(totalMillis, "GUARD", "STOP"));
        } else {
            mVerboseWriter.write(format(totalMillis, "GUARD",  "STOP"));
        }
    }

    @Override
    protected void onStop(double totalMillis, double lastMillis, String reason) {
        onMark(totalMillis, lastMillis, reason);
        onStop(totalMillis, lastMillis);
    }
}

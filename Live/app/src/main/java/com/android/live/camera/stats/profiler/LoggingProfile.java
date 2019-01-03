package com.android.live.camera.stats.profiler;

/**
 * A logging profile writes out all events to the provided
 * writer and uses the standard message formatting.
 */
public class LoggingProfile extends ProfileBase {
    private final Writer mWriter;

    /** Create a new LoggingProfile */
    public LoggingProfile(Writer writer, String name) {
        super(name);

        mWriter = writer;
    }

    @Override
    protected void onStart() {
        mWriter.write(format(0.0, "BEGIN"));
    }

    @Override
    protected void onMark(double totalMillis, double lastMillis, String reason) {
        mWriter.write(format(totalMillis, "MARK", lastMillis, reason));
    }

    @Override
    protected void onStop(double totalMillis, double lastMillis) {
        mWriter.write(format(totalMillis, "END"));
    }

    @Override
    protected void onStop(double totalMillis, double lastMillis, String reason) {
        mWriter.write(format(totalMillis, "END", lastMillis, reason));
    }
}

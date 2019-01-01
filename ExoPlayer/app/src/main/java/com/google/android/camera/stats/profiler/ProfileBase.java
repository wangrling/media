package com.google.android.camera.stats.profiler;

import com.google.android.camera.async.MainThread;

/**
 * Basic profiler that will compute start, end and "time since last event"
 * values and pass them along the subclass. This also provides standard formatting
 * methods for messages to keep them consistent.
 */
public class ProfileBase implements Profile {
    private final String mName;

    private long mStartNanos;
    private long mLastMark;

    /** Create a new profile for a give name. */
    public ProfileBase(String name) {
        mName = name;
    }
    @Override
    public Profile start() {
        mStartNanos = System.nanoTime();
        mLastMark = mStartNanos;

        onStart();

        return this;
    }

    @Override
    public void mark() {
        mLastMark = System.nanoTime();
        // In most cases this will only be used to reset the lastMark time.
    }

    @Override
    public void mark(String reason) {
        long time = System.nanoTime();
        // 总时间和距离上次的时间。
        onMark(getTotalMillis(time), getTimeFromLastMillis(time), reason);
        mLastMark = time;
    }

    @Override
    public void stop() {
        long time = System.nanoTime();
        onStop(getTotalMillis(time), getTimeFromLastMillis(time));
        mLastMark = time;
    }

    @Override
    public void stop(String reason) {
        long time = System.nanoTime();
        onStop(getTotalMillis(time), getTimeFromLastMillis(time), reason);
        mLastMark = time;
    }

    // Called when start() is called.
    protected void onStart() {

    }

    // Called when mark() is called with computed total and time
    // since last event values in milliseconds.
    protected void onMark(double totalMillis, double lastMillis, String reason) {

    }

    // Called when stop() is called with computed total and time
    // since last events values in milliseconds.
    protected void onStop(double totalMillis, double lastMillis) {

    }

    // Format a single message with the total elapsed time and s simple event.
    protected final String format(double totalMillis, String event) {
        return String.format("[%7sms]%s %-6s %s",
                String.format("%.3f", totalMillis),
                MainThread.isMainThread() ? "[ui]" : "",
                event + ":",
                mName);
    }

    // Format a simple message with the total elapsed time, a simple event,
    // and a string at the end.
    protected final String format(double totalMillis, String event, String reason) {
        return String.format("[%7sms]%s %-6s %s - %s",
                String.format("%.3f", totalMillis),
                MainThread.isMainThread() ? "[ui]" : "",
                event + ":",
                mName,
                reason);
    }

    protected final String format(double totalMillis, String event, double lastMillis, String reason) {
        return String.format("[%7sms]%s %-6s %s - [%6sms] %s",
                String.format("%.3f", totalMillis),
                MainThread.isMainThread() ? "[ui]" : "",
                event + ":",
                mName,
                String.format("%.3f", lastMillis),
                reason);
    }

    // Called when stop() is called with computed total and time
    // since last events values in milliseconds. Includes the stop reason.
    protected void onStop(double totalMillis, double lastMillis, String reason) {

    }

    private double getTotalMillis(long timeNanos) {
        // 现在的时间减去开始的时间。
        return nanoToMillis(timeNanos - mStartNanos);
    }

    private double getTimeFromLastMillis(long timeNanos) {
        return nanoToMillis(timeNanos - mLastMark);
    }

    // 1s = 1000ms = 1000,000us = 1000,000,000ns
    private double nanoToMillis(long timeNanos) {
        return (double)(timeNanos) / 1000000.0;
    }
}

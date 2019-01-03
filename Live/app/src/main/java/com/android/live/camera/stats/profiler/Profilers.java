package com.android.live.camera.stats.profiler;

import com.android.live.camera.debug.Log;

/**
 * A set of common, easy to use profilers that fill the most
 * common use cases when profiling parts of an app.
 */
public class Profilers {
    private static final Log.Tag TAG = new Log.Tag("Profiler");

    private static Writer sErrorWriter = new ErrorWriter();
    private static Writer sWarningWriter = new WarningWriter();
    private static Writer sInfoWriter = new InfoWriter();
    private static Writer sDebugWriter = new DebugWriter();
    private static Writer sVerboseWriter = new VerboseWriter();

    private static class Singleton {
        private static final Profilers INSTANCE = new Profilers(
              new LoggingProfiler(sErrorWriter),
              new LoggingProfiler(sWarningWriter),
              new LoggingProfiler(sInfoWriter),
              new LoggingProfiler(sDebugWriter),
              new LoggingProfiler(sVerboseWriter),
              new GuardingProfiler(sInfoWriter, sVerboseWriter));
    }

    /** get a single shared Profilers instance */
    public static Profilers instance() {
        return Singleton.INSTANCE;
    }

    private final LoggingProfiler mErrorProfiler;
    private final LoggingProfiler mWarningProfiler;
    private final LoggingProfiler mInfoProfiler;
    private final LoggingProfiler mDebugProfiler;
    private final LoggingProfiler mVerboseProfiler;
    private final GuardingProfiler mGuardingProfiler;

    private Profilers(LoggingProfiler errorProfiler,
          LoggingProfiler warningProfiler,
          LoggingProfiler infoProfiler,
          LoggingProfiler debugProfiler,
          LoggingProfiler verboseProfiler,
          GuardingProfiler guardingProfiler) {
        mErrorProfiler = errorProfiler;
        mWarningProfiler = warningProfiler;
        mInfoProfiler = infoProfiler;
        mDebugProfiler = debugProfiler;
        mVerboseProfiler = verboseProfiler;
        mGuardingProfiler = guardingProfiler;
    }

    public LoggingProfiler e() {
        return mErrorProfiler;
    }

    public Profile e(String name) {
        return e().create(name).start();
    }

    public LoggingProfiler w()  {
        return mWarningProfiler;
    }

    public Profile w(String name) {
        return w().create(name).start();
    }

    public LoggingProfiler i() {
        return mInfoProfiler;
    }

    public Profile i(String name) {
        return i().create(name).start();
    }

    public LoggingProfiler d() {
        return mDebugProfiler;
    }

    public Profile d(String name) {
        return d().create(name).start();
    }

    public LoggingProfiler v()  {
        return mVerboseProfiler;
    }

    public Profile v(String name) {
        return v().create(name).start();
    }

    public GuardingProfiler guard() {
        return mGuardingProfiler;
    }

    public Profile guard(String name) {
        return guard().create(name).start();
    }

    public Profile guard(String name, int durationMillis) {
        return guard().create(name, durationMillis).start();
    }

    private static class DebugWriter implements Writer {
        @Override
        public void write(String message) {
            Log.d(TAG, message);
        }
    }

    private static class ErrorWriter implements Writer {
        @Override
        public void write(String message) {
            Log.e(TAG, message);
        }
    }

    private static class InfoWriter implements Writer {
        @Override
        public void write(String message) {
            Log.i(TAG, message);
        }
    }

    private static class VerboseWriter implements Writer {
        @Override
        public void write(String message) {
            Log.v(TAG, message);
        }
    }

    private static class WarningWriter implements Writer {
        @Override
        public void write(String message) {
            Log.w(TAG, message);
        }
    }
}

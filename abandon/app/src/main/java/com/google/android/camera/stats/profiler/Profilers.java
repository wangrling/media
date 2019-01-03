package com.google.android.camera.stats.profiler;

import com.google.android.camera.debug.Log;

/**
 * A set of common, easy to profilers that fill the most common use cases
 * when profiling parts of an app.
 * 管理各种Profiler
 * 流程Profilers -> Profiler
 */
public class Profilers {
    private static final Log.Tag TAG = new Log.Tag("Profile");

    private final GuardingProfiler mGuardingProfiler;

    private static Writer sErrorWriter = new ErrorWriter();
    private static Writer sWarningWriter = new WarningWriter();
    private static Writer sInfoWriter = new InfoWriter();
    private static Writer sDebugWriter = new DebugWriter();
    private static Writer sVerboseWriter = new VerboseWriter();

    private final LoggingProfiler mErrorProfiler;
    private final LoggingProfiler mWariningProfiler;
    private final LoggingProfiler mInfoProfiler;
    private final LoggingProfiler mDebugProfiler;
    private final LoggingProfiler mVerboseProfiler;

    public Profilers(LoggingProfiler errorProfiler, LoggingProfiler warningProfiler,
                     LoggingProfiler infoProfiler, LoggingProfiler debugProfiler,
                     LoggingProfiler verboseProfiler, GuardingProfiler guardingProfiler) {
            mErrorProfiler = errorProfiler;
            mWariningProfiler = warningProfiler;
            mInfoProfiler = infoProfiler;
            mDebugProfiler = debugProfiler;
            mVerboseProfiler = verboseProfiler;
            mGuardingProfiler = guardingProfiler;
    }

    private static class Singleton {
        private static final Profilers INSTANCE = new Profilers(
                new LoggingProfiler(sErrorWriter),
                new LoggingProfiler(sWarningWriter),
                new LoggingProfiler(sInfoWriter),
            new LoggingProfiler(sDebugWriter),
            new LoggingProfiler(sVerboseWriter),
            new GuardingProfiler(sInfoWriter, sVerboseWriter));
    }

    public static Profilers instance() {
        return Singleton.INSTANCE;
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

    // 将消息输出到log上
    private static class DebugWriter implements Writer {
        @Override
        public void write(String message) {
            Log.d(TAG, message);
        }
    }

    private static class WarningWriter implements Writer {
        @Override
        public void write(String message) {
            Log.w(TAG, message);
        }
    }

    private static class VerboseWriter implements Writer {
        @Override
        public void write(String message) {
            Log.v(TAG, message);
        }
    }

    private static class InfoWriter implements Writer {
        @Override
        public void write(String message) {
            Log.i(TAG, message);
        }
    }

    private static class ErrorWriter implements Writer {
        @Override
        public void write(String message) {
            Log.e(TAG, message);
        }
    }
}

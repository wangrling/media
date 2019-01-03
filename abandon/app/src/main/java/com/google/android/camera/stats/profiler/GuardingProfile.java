package com.google.android.camera.stats.profiler;

public class GuardingProfile extends ProfileBase {

    private final Writer mGuardWriter;
    private final Writer mVerboseWriter;
    private final int mMaxMillis;

    public GuardingProfile(Writer writer, Writer verbose, String name, int maxDuration) {
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
        // 打印不同的等级，某阶段的时间小于设定的总时间。
        // 应该分开设计，总时间和阶段时间。
        if (lastMillis > mMaxMillis) {
            mGuardWriter.write(format(totalMillis, "GUARD", lastMillis, reason));
        } else {
            mVerboseWriter.write(format(totalMillis, "GUARD", lastMillis, reason));
        }
    }

    @Override
    protected void onStop(double totalMillis, double lastMillis) {
        // 实际运行的总时间小于设定的总时间。
        if (totalMillis > mMaxMillis) {
            mGuardWriter.write(format(totalMillis, "GUARD", "STOP"));
        } else {
            mVerboseWriter.write(format(totalMillis, "GUARD", "STOP"));
        }
    }

    @Override
    protected void onStop(double totalMillis, double lastMillis, String reason) {
        onMark(totalMillis, lastMillis, reason);
        onStop(totalMillis, lastMillis);
    }
}

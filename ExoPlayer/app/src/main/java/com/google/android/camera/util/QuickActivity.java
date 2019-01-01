package com.google.android.camera.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.Nullable;

public class QuickActivity extends Activity {

    /** When application execution started in SystemClock.elapsedRealTimeNanos(). */
    protected long mExecutionStartNanoTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mExecutionStartNanoTime = SystemClock.elapsedRealtimeNanos();


        super.onCreate(savedInstanceState);
    }

    private void logLifecycle(String methodName, boolean start) {
        String prefix = start ? "START" : "END";
    }
}

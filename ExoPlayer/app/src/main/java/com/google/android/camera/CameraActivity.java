package com.google.android.camera;

import android.os.Bundle;

import com.google.android.camera.app.FirstRunDialog;
import com.google.android.camera.stats.profiler.Profile;
import com.google.android.camera.stats.profiler.Profiler;
import com.google.android.camera.stats.profiler.Profilers;
import com.google.android.camera.util.QuickActivity;

public class CameraActivity extends QuickActivity {

    private final Profiler mProfiler = Profilers.instance().guard();

    /** First run dialog */
    private FirstRunDialog mFirstRunDialog;

    @Override
    protected void onCreateTasks(Bundle savedInstanceState) {
        Profile profile = mProfiler.create("CameraActivity.onCreateTasks").start();

    }
}

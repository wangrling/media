package com.android.live.camera;

import android.os.Bundle;

import com.android.live.R;
import com.android.live.camera.ui.MockView;
import com.android.live.camera.util.QuickActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

public class MockActivity extends QuickActivity {

    private ActionBar mActionBar;

    @Override
    protected void onCreateTasks(@Nullable Bundle savedInstanceState) {

        // setContentView(R.layout.activity_camera);
        setContentView(new MockView(this));
        mActionBar = getSupportActionBar();
    }
}

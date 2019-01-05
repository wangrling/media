package com.android.live.camera.app;

import android.view.ViewConfiguration;

import com.android.live.camera.debug.Log;
import com.android.live.camera.ui.CameraActivityLayout;

public class CameraAppUI {

    private final static Log.Tag TAG = new Log.Tag("CameraAppUI");

    // private final AppController mController;

    private final int mSlop;
    private final boolean mIsCaptureIntent;
    private final CameraActivityLayout mAppRootView;

    public CameraAppUI(AppController controller, CameraActivityLayout appRootView,
                       boolean isCaptureIntent) {
        mSlop = ViewConfiguration.get(controller.getAndroidContext()).getScaledTouchSlop();
        // mController = controller;
        mIsCaptureIntent = isCaptureIntent;

        mAppRootView = appRootView;


    }
}

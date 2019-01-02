package com.google.android.camera.app;

import android.content.Context;

/**
 * The dialog to show when users open the app for the first time.
 * 和FirstRunDetector配套使用。
 */
public class FirstRunDialog {

    // CameraActivity构建监听实例。
    public interface FirstRunDialogListener {
        public void onFirstRunStateReady();
        public void onFirstRunDialogCancelled();
        public void onCameraAccessException();
    }

    // Constructs a first run dialog.
    public FirstRunDialog(
            AppController appController,
            Context activityContext,
    )
}

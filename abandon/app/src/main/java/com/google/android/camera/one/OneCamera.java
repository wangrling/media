package com.google.android.camera.one;

import androidx.annotation.NonNull;

public interface OneCamera {


    public static interface OpenCallback {

        /**
         * Called when the camera was opened successfully.
         *
         * @param camera    The camera instance that was successfully opened.
         */
        public void onCameraOpened(@NonNull OneCamera camera);

        /**
         * Called if opening the camera failed.
         */
        void onFailure();

        /**
         * Called if the camera is closed or disconnected while
         * attempting to open.
         */
        void onCameraClosed();
    }
}

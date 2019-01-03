package com.google.android.camera.ex;

/**
 * The device info for all attached cameras.
 */
public class CameraDeviceInfo {



    public interface Characteristics {

        /**
         * @return  Whether the camera faces the back of the device.
         */
        boolean isFacingBack();

        /**
         * @return  Whether the camera faces the device's screen.
         */
        boolean isFacingFront();

        /**
         * @return  The camera image orientation, or the clockwise rotation angle
         * that must be applied to display it in tis natural orientation (in degrees,
         * and always a multiple of 90).
         */
        int getSensorOrientation();

        /**
         * @return  Whether the shutter sound can be disabled.
         */
        boolean canDisableShutterSound();
    }
}

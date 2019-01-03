package com.google.android.camera.app;

import android.content.res.Configuration;

/**
 * An interface which defines the orientation manager.
 * 物理设备的方位旋转多少度，只计算四个方位。
 * 设备方位没有办法通过软件控制是否旋转，但是显示方位可以控制。
 */
public interface OrientationManager {

    public static enum DeviceNaturalOrientation {
        PORTRAIT(Configuration.ORIENTATION_PORTRAIT),
        LANDSCAPE(Configuration.ORIENTATION_LANDSCAPE);

        private final int mOrientation;
        private DeviceNaturalOrientation(int orientation) {
            mOrientation = orientation;
        }
    }

    public static enum DeviceOrientation {
        CLOCKWISE_0(0),
        CLOCKWISE_90(90),
        CLOCKWISE_180(180),
        CLOCKWISE_270(270);

        private final int mDegrees;

        DeviceOrientation(int  degrees) {
            mDegrees = degrees;
        }

        /**
         * Returns the degree in clockwise.
         */
        public int getDegrees() {
            return mDegrees;
        }

        public static DeviceOrientation from(int degrees) {
            switch (degrees) {
                case (-1):  // UNKNOWN Orientation
                    // Explicitly default to CLOCKWISE_0, When Orientation is UNKNOWN.
                    return CLOCKWISE_0;
                case 0:
                    return CLOCKWISE_0;
                case 90:
                    return CLOCKWISE_90;
                case 180:
                    return CLOCKWISE_180;
                case 270:
                    return CLOCKWISE_270;
                default:
                    // 归化到0-360
                    int normalizedDegrees = (Math.abs(degrees / 360) * 360 + 360 + degrees) % 360;
                    if (normalizedDegrees > 315 || normalizedDegrees <= 45) {
                        return CLOCKWISE_0;
                    } else if (normalizedDegrees > 45 && normalizedDegrees <= 135) {
                        return CLOCKWISE_90;
                    } else if (normalizedDegrees > 135 && normalizedDegrees <= 225) {
                        return CLOCKWISE_180;
                    } else {
                        return CLOCKWISE_270;
                    }
            }
        }
    }

    public interface OnOrientationChangeListener {
        /**
         * Called when the orientation changes.
         *
         * @param orientationManager The orientation manager detects the change.
         * @param orientation   The new rounded orientation
         */
        public void onOrientationChanged(OrientationManager orientationManager,
                                         DeviceOrientation orientation);
    }

    public void addOnOrientationChangeListener(OnOrientationChangeListener listener);

    void removeOnOrientationChangeListener(OnOrientationChangeListener listener);

    // Returns the device natural orientation.
    public DeviceNaturalOrientation getDeviceNaturalOrientation();

    // Return the current rounded device orientation.
    public DeviceOrientation getDeviceOrientation();

    // Return the current display rotation.
    public DeviceOrientation getDisplayRotation();

    /**
     * @return  Whether the device is in landscape based on the natural orientation
     * and rotation from natural orientation.
     */
    public boolean isInLandscape();

    /**
     * @return  Whether the device is in portrait based on the natural orientation
     * and rotation from natural orientation.
     */
    public boolean isInPortrait();

    /**
     * Lock the framework orientation to the current device orientation rotates. No effect
     * if the system setting of auto-rotation is off.
     */
    void lockOrientation();

    /**
     * Unlock the framework orientation, so it can change when the device rotates. No
     * effect if the system setting of auto-rotation is off.
     */
    void unlockOrientation();

    /**
     * Return whether the orientation is locked by the app or the system.
     */
    boolean isOrientationLocked();
}

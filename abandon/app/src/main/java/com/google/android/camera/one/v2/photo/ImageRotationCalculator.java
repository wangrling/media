package com.google.android.camera.one.v2.photo;

import com.google.android.camera.app.OrientationManager;
import com.google.common.base.Supplier;

/**
 * Based on the current device orientation, calculates the JPEG rotation that needs to be applied
 * to render the resulting JPEG correctly.
 */
public interface ImageRotationCalculator {

    /**
     * Calculates the correct JPEG orientation base on the given sampled device orientation,
     * and the sensor orientation.
     *
     * @return  The JPEG rotation that needs to be applied to the final image.
     */
    public OrientationManager.DeviceOrientation toImageRotation();

    public Supplier<Integer> getSupplier();
}

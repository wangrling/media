package com.google.android.camera.device;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Identifier for Camera1 and Camera2 camera devices.
 */

public final class CameraId {

    private final Integer mLegacyCameraId;
    private final String mCameraId;

    private CameraId(@NonNull String cameraId, @Nullable Integer legacyCameraId) {
        mCameraId = cameraId;
        mLegacyCameraId = legacyCameraId;
    }
}

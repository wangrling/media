package com.android.live.camera.device;

import com.android.live.camera.debug.Log;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.GuardedBy;

import androidx.annotation.VisibleForTesting;

/**
 * Shared object for tracking the active camera device across multiple
 * implementations.
 */
@ParametersAreNonnullByDefault
public class ActiveCameraDeviceTracker {

    private static final Log.Tag TAG = new Log.Tag("ActvCamDevTrckr");

    private static class Singleton {
        public static final ActiveCameraDeviceTracker INSTANCE = new ActiveCameraDeviceTracker();
    }

    public static ActiveCameraDeviceTracker instance() {
        return Singleton.INSTANCE;
    }

    private final Object mLock;

    @GuardedBy("mLock")
    private CameraId mActiveCamera;

    @GuardedBy("mLock")
    private CameraId mPreviousCamera;

    @VisibleForTesting
    ActiveCameraDeviceTracker() {
        mLock = new Object();
    }

    public CameraId getActiveCamera() {
        synchronized (mLock) {
            return mActiveCamera;
        }
    }

    public CameraId getActiveOrPreviousCamera() {
        synchronized (mLock) {
            if (mActiveCamera != null) {

                return mActiveCamera;
            }
            Log.v(TAG, "Returning previously active camera: " + mPreviousCamera);
            return mPreviousCamera;
        }
    }

    public void onCameraOpening(CameraId key) {
        synchronized (mLock) {
            if (mActiveCamera != null && !mActiveCamera.equals(key)) {
                mPreviousCamera = mActiveCamera;
            }

            Log.v(TAG, "Tracking active camera: " + mActiveCamera);
            mActiveCamera = key;
        }
    }

    public void onCameraClosed(CameraId key) {
        synchronized (mLock) {
            if (mActiveCamera != null && mActiveCamera.equals(key)) {
                mPreviousCamera = mActiveCamera;
                mActiveCamera = null;
            }
        }
    }
}

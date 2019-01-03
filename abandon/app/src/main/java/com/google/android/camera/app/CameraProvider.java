package com.google.android.camera.app;

import com.google.android.camera.ex.CameraDeviceInfo;

/**
 * An interface which defines the camera provider.
 * 物理设备提供
 */
public interface CameraProvider {

    /**
     * Request the camera device. If the camera device of the same ID is
     * already requested, then no-op here.
     *
     * @param id    The ID of the requested camera device.
     */
    public void requestCamera(int id);

    /**
     * Request the camera device. If the camera device of the same ID is
     * already requested, then no-op here.
     * @param id    The ID of the requested camera device.
     * @param useNewApi Whether to the new API if this platform provides it.
     */
    void requestCamera(int id, boolean useNewApi);

    public boolean waitingForCamera();

    // Release the camera device.
    public void releaseCamera(int id);

    /**
     * Gets the {@link CameraDeviceInfo.Characteristics} of the given camera.
     * @return  The static characteristics of that camera.
     */
    public CameraDeviceInfo.Characteristics getCharacteristics(int cameraId);

    /**
     * @return  The current camera id.
     */
    public int getCurrentCameraId();

    /**
     * @return  The total number of cameras available on the device.
     */
    int getNumberOfCameras();

    /**
     * @return  The lowest ID of the back camera or -1 if not available.
     */
    public int getFirstBackCameraId();

    /**
     * @return  The lowest ID of the front camera or -1 if not available.
     */
    public int getFirstFrontCameraId();

    // Whether the camera is facing front.
    boolean isFrontFacingCamera(int id);

    /**
     * @return  Whether the camera is facing back.
     */
    public boolean isBackFacingCamera(int id);
}

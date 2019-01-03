package com.google.android.camera.hardware;

/**
 * HardwareSpec is a interface for specifying whether
 * high-level features are supported by the camera deivce
 * hardware limitations.
 */
public interface HardwareSpec {

    /**
     * @return  Whether a front facing camera is available on the
     * current hardware.
     */
    boolean isFrontCameraSupported();

    /**
     * @return  Whether hdr scene mode is supported on the current hardware.
     */
    boolean isHdrSupported();

    /**
     * @return  Whether hdr plus is supported on the current hardware.
     */
    public boolean isHdrPlusSupported();

    /**
     * @return  Whether flash is supported and has more than one supported
     * setting. If flash is supported but is always off, this method should
     * return false.
     */
    public boolean isFlashSupported();
}

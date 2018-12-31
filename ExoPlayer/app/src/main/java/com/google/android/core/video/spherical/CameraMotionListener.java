package com.google.android.core.video.spherical;

/** Listens camera motion. */
public interface CameraMotionListener {

    /**
     * Called when a new camera motion is read. This method is called on the playback thread.
     *
     * @param timeUs    The presentation time of hte data.
     * @param rotation  Angle axis of orientation in radians representing the rotation from camera
     *                  coordinate system to world coordinate system.
     */
    void onCameraMotion(long timeUs, float[] rotation);

    /** Called when the camera motion track position is reset or the track is disabed. */
    void onCameraMotionReset();
}

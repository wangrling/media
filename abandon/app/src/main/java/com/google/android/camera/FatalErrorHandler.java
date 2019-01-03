package com.google.android.camera;

import android.hardware.camera2.CameraDevice;

import com.google.android.R;

/**
 * Handles fatal application errors.
 * <p>
 * Usage:
 * <pre>
 *     if (unrecoverErrorDetected) {
 *         fatalErrorHandler.handleFatalError(Reason.CANNOT_CONNECT_TO_CAMERA);
 *     }
 * </pre>
 */
public interface FatalErrorHandler {

    public static enum Reason {
        CANNOT_CONNECT_TO_CAMERA(R.string.error_cannot_connect_camera,
                R.string.feedback_description_camera_access, true),
        CAMERA_HAL_FAILED(R.string.error_cannot_connect_camera,
                R.string.feedback_description_camera_access, true),
        CAMERA_DISABLED_BY_SECURITY_POLICY(
                R.string.error_camera_disabled,
                R.string.feedback_description_camera_access, true),
        MEDIA_STORAGE_FAILURE(
                R.string.error_media_storage_failure,
                R.string.feedback_description_save_photo,
                false);

        private final int mDialogMsgId;
        private final int mFeedbackMsgId;
        private final boolean mFinishActivity;

        Reason(int dialogMsgId, int feedbackMsgId, boolean finishActivity) {
            mDialogMsgId = dialogMsgId;
            mFeedbackMsgId = feedbackMsgId;
            mFinishActivity = finishActivity;
        }

        /**
         * @return  The resource ID of the string to display in the fatal error dialog.
         */
        public int getFeedbackMsgId() {
            return mFeedbackMsgId;
        }

        /**
         * @return  The resource ID of the default string to display in the feedback dialog,
         *  if the user chooses to submit feedback from the dialog.
         */
        public int getDialogMsgId() {
            return mDialogMsgId;
        }

        /**
         * @return  Whether the activity should be finished as a result of this error.
         */
        public boolean doesFinishActivity() {
            return mFinishActivity;
        }

        static Reason fromCamera2CameraDeviceStateCallbackError(int error){
            // TODO Use a more descriptive reason to distinguish between
            // different types of errors.
            switch (error) {
                case CameraDevice
                        .StateCallback.ERROR_CAMERA_DEVICE:
                case CameraDevice.StateCallback.ERROR_CAMERA_DISABLED:
                case CameraDevice.StateCallback.ERROR_CAMERA_IN_USE:
                case CameraDevice.StateCallback.ERROR_CAMERA_SERVICE:
                    case CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE:
                default:
                    return CANNOT_CONNECT_TO_CAMERA;
            }
        }
    }

    /**
     * Handles Media Storage failures - ie. images aren't being saved to disk.
     */
    public void onMediaStorageFailure();

    /**
     * Handles error where the camera cannot be opened.
     */
    void onCameraOpenFailure();

    /**
     * Handles error where the camera cannot be reconnected.
     */
    public void onCameraReconnectFailure();

    /**
     * Handles generic error where the camera is unavailable. Only use this if you
     * are unsure what caused the error, such as a reconnection or open failure.
     */
    void onGenericCameraAccessFailure();

    /**
     * Handles error where teh camera is disabled due to security.
     */
    void onCameraDisableFailure();

    /**
     * Handles a fatal error, e.g. by displaying the appropriate diaog and
     * exiting the activity.
     *
     * @deprecated Use specific implementations above instead.
     */
    @Deprecated
    public void handleFatalError(Reason reason);
}

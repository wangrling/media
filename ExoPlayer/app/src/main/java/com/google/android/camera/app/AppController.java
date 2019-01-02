package com.google.android.camera.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.location.LocationManager;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.camera.module.ModuleController;
import com.google.android.camera.one.config.OneCameraFeatureConfig;

/**
 * The controller at app level.
 */
public interface AppController {

    /**
     * An interface which defines the shutter (快门) events listener.
     */
    public interface ShutterEventsListener {

        // Called when the shutter state is changed to pressed.
        void onShutterPressed();

        // Called when the shutter is changed to released.
        void onShutterReleased();

        // Called when the shutter is clicked.
        void onShutterClicked();

        // Called when the shutter is long pressed.
        void onShutterLongPressed();
    }

    /**
     * @return  The {@link Context} being used.
     */
    public Context getAndroidContext();

    /**
     * @return  The camera feature configuration for the device.
     */
    public OneCameraFeatureConfig getCameraFeatureConfig();

    /**
     * Creates a new dialog which can be shown in the app.
     *
     * @return  {@link Dialog} of the app.
     */
    public Dialog createDialog();

    /**
     * @return  A String scope uniquely identifying the current module.
     */
    public String getModuleScope();

    /**
     * @return  A String scope uniquely identifying the current camera id.
     */
    public String getCameraScope();

    /**
     * Starts an activity.
     *
     * @param intent    Used to start the activity.
     */
    public void launchActivityByIntent(Intent intent);

    /**
     * See {@link Activity#openContextMenu(View)}.
     */
    public void openContextMenu(View view);

    /**
     * See {@link Activity#registerForContextMenu(View)}.
     */
    public void registerForContextMenu(View view);

    /**
     * @return  Whether the app is currently paused.
     */
    public boolean isPaused();

    /**
     * @return  The current module controller.
     */
    public ModuleController getCurrentModuleController();

    /**
     * @return  The currently active module index.
     */
    public int getCurrentModuleIndex();

    /**
     * @return  The module ID for a specific mode.
     */
    public int getModuleId(int modeIndex);

    /**
     * Gets the mode that can be switched to from the give mode id through
     * quick switch.
     *
     * @param currentModuleIndex   Index of the current mode.
     * @return  Mode id to quick switch to if index is valid, otherwise returns
     * the given mode id itself.
     */
    public int getQuickSwitchToModuleId(int currentModuleIndex);

    /**
     * Based on a mode switcher index, choose the correct module index.
     * @param modeIndex mode switcher index.
     * @return  Module index.
     */
    public int getPreferredChildModeIndex(int modeIndex);

    /**
     * The get called when mode is changed.
     *
     * @param moduleIndex   Index of the new module to switch to.
     */
    public void onModeSelected(int moduleIndex);

    /**
     * This get called when settings is selected and settings dialog needs to open.
     */
    public void onSettingsSelected();

    /************************* UI / Camera preview **************************/

    /**
     * Freeze what is currently shown on screen until the next preview frame comes
     * in. This can be used for camera switch to hide the UI changes underneatch
     * until preview is ready.
     */
    public void freezeScreenUntilPreviewReady();

    /**
     * @return  The {@link SurfaceTexture} used by the preview UI.
     */
    public SurfaceTexture gtePreviewBuffer();

    /**
     * Gets called from module when preview is ready to start.
     */
    public void onPreviewReadyToStart();

    /**
     * Gets called from module when preview is started.
     */
    public void onPreviewStarted();

    /**
     * Adds listener to receive callbacks when preview area changes.
     */
    public void addPreviewAreaSizeChangeListener(
            PreviewStatusListener.PreviewAreaChangedListener listener);

    /**
     * Sets up one shot preview callback in order to notify UI when the net preview frame
     * comes in.
     */
    public void setupOneShotPreviewListener();

    /**
     * Gets called from module when preview aspect ratio has changed.
     *
     * @param aspectRatio   Aspect ratio of preview stream.
     */
    public void updatePreviewAspectRatio(float aspectRatio);

    /**
     * Gets called from module when the module needs to change the transform matrix of the preview
     * TextureView. It does not modify the matrix before  applying it.
     *
     * @param matrix    Transform matrix to be set on preview TextureView.
     * @param aspectRatio   The desired aspect ratio of the preview.
     */
    public void updatePreviewTransformFullscreen(Matrix matrix, float aspectRatio);

    /**
     * Call this to find the full rect available for a full screen preview.
     *
     * @return  the rect of the full screen minus any decor.
     */
    public RectF getFullscreenRect();

    /**
     * Gets called from module when the module needs to change the transform matrix of the preview
     * TextureView. It is encouraged to use {@link #updatePreviewAspectRatio(float)} over this
     * function, unless the module needs to rotate the surface texture using transform matrix.
     *
     * @param matrix    Transform matrix to be set on preview TextureView.
     */
    public void updatePreviewTransform(Matrix matrix);

    /**
     * Sets the preview status listener, which will get notified when TextureView surface has changed.
     *
     * @param previewStatusListener The listener to get callbacks.
     */
    public void setPreviewStatusListener(PreviewStatusListener previewStatusListener);

    /**
     * @return  The {@link FrameLayout} as the root of the module.
     */
    public FrameLayout getModuleLayoutRoot();

    /**
     * Locks the system orientation.
     */
    public void lockOrientation();

    /**
     * Unlocks the system orientation.
     */
    public void unlockOrientation();


    /************************* Shutter button **********************************/

    /**
     * Sets the shutter events listener.
     *
     * @param listener  The listener.
     */
    public void setShutterEventsListener(ShutterEventsListener listener);

    /**
     * Checks whether the shutter is enabled.
     */
    public boolean isShutterEnabled();

    /************************** Capture animation ********************************/

    /**
     * Start flash animation with optional shorter flash.
     *
     * @param shortFlash    True for shorter flash (faster cameras).
     */
    public void startFlashAnimation(boolean shortFlash);

    /**
     * Starts normal pre-capture animation.
     */
    public void startPreCaptureAnimation();

    /**
     * Cancel the pre-capture animation.
     */
    public void cancelPreCaptureAnimation();

    /**
     * Starts the post-capture animation with the current preview image.
     */
    public void startPostCaptureAnimation();

    /**
     * Start the post-capture animation with the given thumbnail.
     *
     * @param thumbnail The thumbnail for the animation.
     */
    public void startPostCaptureAnimation(Bitmap thumbnail);

    /**
     * Cancels the post-capture animation.
     */
    public void cancelPostCaptureAnimation();

    /*************************** Media saving ******************************/

    /**
     * Notifies the app of the newly captured media.
     */
    public void notifyNewMedia(Uri uri);

    /***************************** App-level resources **************************/

    /**
     * Keeps the screen turned on.
     *
     * @param enabled   Whether to keep the screen on.
     */
    public void enableKeepScreenOn(boolean enabled);

    /**
     * @return  The {@link CameraProvider}.
     */
    public CameraProvider getCameraProvider();

    /**
     * @return  The new camera API manager.
     */
    public OneCameraOpener getCameraOpener();

    /**
     * Returns the {@link OrientationManagerImpl}
     *
     * @return  {@code null} if not available yet.
     */
    public OrientationManager getOrientationManager();

    /**
     * Returns the {@link LocationManager}.
     *
     * @return {@code null} if not available yet.
     */
    public LocationManager getLocationManager();

    /**
     * @return  The {@link SettingsManager}
     */
    public SettingsManager getSettingsManager();

    /**
     * Returns the {@linnk ResolutionSetting}.
     *
     * @return  the current resolution setting.
     */
    public ResolutionSetting getResolutionSetting();

    /**
     * @return Common services and functionality to be shared.
     */
    public CameraServices getServices();

    /**
     * @return  The error handler to invoke for errors.
     */
    public FatalErrorHandler getFatalErrorHandler();

    /**
     * @return  {@code null} if not available yet.
     */
    public CameraAppUI getCameraAppUI();

    /**
     * @return  {@code null} if not available yet.
     */
    public ModuleManager getModuleManager();

    public ButtonManager getButtonManager();

    /**
     * @return  A sound player that can be used to play custom sounds.
     */
    public SoundPlayer getSoundPlayer();

    /**
     * Shows the given tutorial overlay.
     */
    public void showTutorial(AbstractTutorialOverlay tutorial);

    /**
     * Finishes the activity since the intent is completed successfully.
     *
     * @param resultIntent  The intent that carries the result.
     */
    public void finishActivityWithIntentCompleted(Intent resultIntent);

    /**
     * Finish the activity since the intent got canceled.
     */
    public void finishActivityWithIntentCanceled();
}

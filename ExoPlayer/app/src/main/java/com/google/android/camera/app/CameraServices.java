package com.google.android.camera.app;

import com.google.android.camera.settings.SettingsManager;

/**
 * Functionality available to all modules and services.
 */
public interface CameraServices {

    public SettingsManager getSettingsManager();
}

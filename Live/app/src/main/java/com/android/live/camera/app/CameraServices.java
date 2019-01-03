package com.android.live.camera.app;

import com.android.live.camera.settings.SettingsManager;

/**
 * 不是后台运行的服务。
 * Functionality available to all modules and services.
 */
public interface CameraServices {


    /**
     * @return  The setting manager which allows get/set of all app settings.
     */
    public SettingsManager getSettingsManager();
}

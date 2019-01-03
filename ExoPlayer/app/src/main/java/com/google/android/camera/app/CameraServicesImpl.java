package com.google.android.camera.app;

import android.content.Context;

import com.google.android.camera.settings.SettingsManager;
import com.google.android.camera.util.AndroidContext;

/**
 * Functionality available to all modules and services.
 */

public class CameraServicesImpl implements CameraServices {

    private final SettingsManager mSettingManager;

    /**
     * Fast, thread safe singleton initialization.
     */
    private static class Singleton {
        private static final CameraServicesImpl INSTANCE = new CameraServicesImpl(
                AndroidContext.instance().get());
    }

    private CameraServicesImpl(Context context) {
        mSettingManager = new SettingsManager(context);
    }

    /**
     * @return a single instance of of the global camera services.
     */
    public static CameraServicesImpl instance() {
        return Singleton.INSTANCE;
    }

    @Override
    public SettingsManager getSettingsManager() {
        return null;
    }
}

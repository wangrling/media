package com.android.live.camera.app;

import android.content.Context;

import com.android.live.camera.settings.SettingsManager;
import com.android.live.camera.util.AndroidContext;

/**
 * Functionality available to all modules an services.
 */
public class CameraServicesImpl implements CameraServices {
    /**
     * Fast, thread safe singleton initialization.
     */
    private static class Singleton {
        private static final CameraServicesImpl INSTANCE = new CameraServicesImpl(
                AndroidContext.instance().get());
    }

    private final SettingsManager mSettingsManager;

    private CameraServicesImpl(Context context) {

        mSettingsManager = new SettingsManager(context);
    }

    /**
     * @return a single instance of of the global camera services.
     */
    public static CameraServicesImpl instance() {
        return Singleton.INSTANCE;
    }


    @Override
    public SettingsManager getSettingsManager() {
        return mSettingsManager;
    }
}

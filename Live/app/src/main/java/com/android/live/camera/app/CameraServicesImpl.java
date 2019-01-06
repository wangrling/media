package com.android.live.camera.app;

import android.content.Context;

import com.android.live.camera.MediaSaverImpl;
import com.android.live.camera.remote.RemoteShutterListener;
import com.android.live.camera.session.CaptureSessionManager;
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

    /**
     * @return a single instance of of the global camera services.
     */
    public static CameraServicesImpl instance() {
        return Singleton.INSTANCE;
    }

    private final MediaSaver mMediaSaver;
    private final MemoryManagerImpl mMemoryManager;
    private final SettingsManager mSettingsManager;


    private CameraServicesImpl(Context context) {
        mMediaSaver = new MediaSaverImpl(context.getContentResolver());

        mMemoryManager = MemoryManagerImpl.create(context, mMediaSaver);
        mSettingsManager = new SettingsManager(context);
    }


    @Override
    public CaptureSessionManager getCaptureSessionManager() {
        return null;
    }

    @Override
    public MemoryManager getMemoryManager() {
        return mMemoryManager;
    }

    @Override
    public MotionManager getMotionManager() {
        return null;
    }

    @Override
    public MediaSaver getMediaSaver() {
        return null;
    }

    @Override
    public RemoteShutterListener getRemoteShutterListener() {
        return null;
    }

    @Override
    public SettingsManager getSettingsManager() {
        return mSettingsManager;
    }
}

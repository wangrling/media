package com.google.android.camera.app;

import android.app.Activity;
import android.os.Handler;

import com.google.android.camera.CameraActivity;
import com.google.android.camera.debug.Log;

/**
 * The implementation of {@link OrientationManager} by {@link android.view.OrientationEventListener}.
 */
public class OrientationManagerImpl implements OrientationManager {

    private static final Log.Tag TAG = new Log.Tag("OrientMgrImpl");
    

    public OrientationManagerImpl(Activity activity, Handler handler) {
    }
}

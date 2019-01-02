package com.google.android.camera.app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.OrientationEventListener;

import com.google.android.camera.CameraActivity;
import com.google.android.camera.debug.Log;

/**
 * The implementation of {@link OrientationManager} by {@link android.view.OrientationEventListener}.
 */
public class OrientationManagerImpl implements OrientationManager {

    private static final Log.Tag TAG = new Log.Tag("OrientMgrImpl");

    // DeviceOrientation hysteresis amount used in rounding, in degrees.
    private static final int ORIENTATION_HYSTERESIS = 5;

    private final Activity mActivity;

    // The handler used to invoke listener callback.
    private final Handler mHandler;

    private final MyOrientationEventListener mOrientationListener;

    public OrientationManagerImpl(Activity activity, Handler handler) {
        mActivity = activity;
        mHandler = handler;
        mOrientationListener =
    }

    @Override
    public void addOnOrientationChangeListener(OnOrientationChangeListener listener) {

    }

    @Override
    public void removeOnOrientationChangeListener(OnOrientationChangeListener listener) {

    }

    @Override
    public DeviceNaturalOrientation getDeviceNaturalOrientation() {
        return null;
    }

    @Override
    public DeviceOrientation getDeviceOrientation() {
        return null;
    }

    @Override
    public DeviceOrientation getDisplayRotation() {
        return null;
    }

    @Override
    public boolean isInLandscape() {
        return false;
    }

    @Override
    public boolean isInPortrait() {
        return false;
    }

    @Override
    public void lockOrientation() {

    }

    @Override
    public void unlockOrientation() {

    }

    @Override
    public boolean isOrientationLocked() {
        return false;
    }

    // This listens to the device orientation, so we can update the compensation.
    private class MyOrientationEventListener extends OrientationEventListener {

        public MyOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {

        }
    }
}

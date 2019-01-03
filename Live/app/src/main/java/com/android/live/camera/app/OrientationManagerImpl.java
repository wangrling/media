package com.android.live.camera.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;

import com.android.live.camera.debug.Log;
import com.android.live.camera.util.AndroidServices;

public class OrientationManagerImpl implements OrientationManager {

    private static final Log.Tag TAG = new Log.Tag("OrientMgrImpl");

    private final Activity mActivity;

    private final MyOrientationEventListener mOrientationListener;

    // The handler used to invoke listener callback.
    private final Handler mHandler;

    private final boolean mIsDefaultToPortrait;

    public OrientationManagerImpl(Activity activity, Handler handler) {
        mActivity = activity;
        mOrientationListener = new MyOrientationEventListener(activity);
        mHandler = handler;
        mIsDefaultToPortrait = isDefaultToPortrait(activity);
    }

    /**
     * Calculate the default orientation of the device based on the width and
     * height of the display when rotation = 0 (i.e. natrual width and height)
     *
     * @param context   Current context.
     * @return  whether the default orientation of the device is portrait.
     */
    private boolean isDefaultToPortrait(Context context) {
        Display currentDisplay = AndroidServices.instance().provideWindowManager()
                .getDefaultDisplay();
        Point displaySize = new Point();

        // 计算Display的宽和高
        currentDisplay.getSize(displaySize);
        // 获取Display旋转的角度
        int orientation = currentDisplay.getRotation();
        /**
         * 手机水平放置的时候，Activity没有旋转。
         * CAM_OrientMgrImpl: display orientation: 0
         * 说明display的坐标系是相对于设备。
         */
        Log.v(TAG, "display orientation: " + orientation);
        int naturalWidth, naturalHeight;
        if (orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180) {
            naturalWidth = displaySize.x;
            naturalHeight = displaySize.y;
        } else {
            naturalWidth = displaySize.y;
            naturalHeight = displaySize.x;
        }
        // 如果是手机默认是竖直，如果是平板默认是水平。
        return naturalWidth < naturalHeight;
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

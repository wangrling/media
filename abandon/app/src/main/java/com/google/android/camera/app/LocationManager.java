package com.google.android.camera.app;

import android.content.Context;
import android.location.Location;

import com.google.android.camera.debug.Log;

/**
 * A class to select the best available location provider (fused location
 * provider, or network/gps if the fused location provider is unavailable)
 * and provide a common location interface.
 * 对{@link android.location.LocationManager}完成封装。
 */
public class LocationManager {

    private static final Log.Tag TAG = new Log.Tag("LocationManager");

    LocationProvider mLocationProvider;
    // 记录位置
    private boolean mRecordLocation;

    public LocationManager(Context context) {
        Log.d(TAG, "Using legacy location provider.");
        LegacyLocationProvider llp = new LegacyLocationProvider(context);
        mLocationProvider = llp;
    }

    /**
     * Start/stop location recording.
     */
    public void recordLocation(boolean recordLocation) {
        mRecordLocation = recordLocation;
        mLocationProvider.recordLocation(mRecordLocation);
    }

    /**
     * @return The current location from the location provider or null, if
     * location could not be determined or is switched off.
     */
    public Location getCurrentLocation() {
        return mLocationProvider.getCurrentLocation();
    }

    /**
     * Disconnects the location provider.
     */
    public void disconnect() {
        mLocationProvider.disconnect();;
    }
}

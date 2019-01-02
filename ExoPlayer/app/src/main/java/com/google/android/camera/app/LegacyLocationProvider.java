package com.google.android.camera.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.camera.debug.Log;

/**
 * A class that handles legacy (network, gps) location providers, in the event
 * the fused (融合定位) location provider from Google Play Services is unavailable.
 */

public class LegacyLocationProvider implements LocationProvider {

    private static final Log.Tag TAG = new Log.Tag("LcyLocProvider");

    private Context mContext;
    private android.location.LocationManager mLocationManager;

    private boolean mRecordLocation;

    public LegacyLocationProvider(Context context) {
        mContext = context;
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
        new LocationListener(android.location.LocationManager.GPS_PROVIDER),
            new LocationListener(android.location.LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public Location getCurrentLocation() {
        if (!mRecordLocation) {
            return null;
        }

        // go in base to worse order.
        for (int i = 0; i < mLocationListeners.length; i++) {
            Location l = mLocationListeners[i].current();
            if (l != null) {
                return l;
            }
        }
        Log.d(TAG, "no location received yet.");
        return null;
    }

    // 设置是否允许记录位置。
    @Override
    public void recordLocation(boolean recordLocation) {
        if (mRecordLocation != recordLocation) {
            mRecordLocation = recordLocation;
            if (recordLocation) {
                startReceivingLocationUpdates();
            } else {
                stopReceivingLocationUpdates();
            }
        }
    }

    private void startReceivingLocationUpdates() {
        Log.v(TAG, "starting location updates");
        if (mLocationManager != null) {
            try {
                mLocationManager.requestLocationUpdates(
                        android.location.LocationManager.NETWORK_PROVIDER, 1000, 0F,
                        mLocationListeners[1]);
            } catch (SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "provider does not exist " + ex.getMessage());
            }

            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 1000, 0F, mLocationListeners[0]);
            } catch (SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "provider does not exist " + ex.getMessage());
            }
            Log.d(TAG, "startReceivingLocationUpdates");
        }
    }

    private void stopReceivingLocationUpdates() {
        Log.v(TAG, "stopping location updates");
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception e) {
                    Log.i(TAG, "fail to remove location listener, ignore", e);
                }
            }
            Log.d(TAG, "stopReceivingLocationUpdates");
        }
    }

    @Override
    public void disconnect() {
        Log.d(TAG, "disconnect");
        // The onPause() call to stopReceivingLocationUpdates is sufficient to unregister the
        // Network/GPS listener.
    }

    private class LocationListener implements android.location.LocationListener {

        Location mLastLocation;
        boolean mValid = false;
        String mProvider;

        public LocationListener(String provider) {
            mProvider = provider;
            mLastLocation = new Location(mProvider);
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location.getLatitude() == 0.0 &&
                    location.getLongitude() == 0.0) {
                // Hack to filter out (0.0, 0.0) locations.
                return ;
            }
            if (!mValid) {
                Log.d(TAG, "Got first location");
            }
            mLastLocation.set(location);
            mValid = true;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case android.location.LocationProvider.OUT_OF_SERVICE:
                case android.location.LocationProvider.TEMPORARILY_UNAVAILABLE: {
                    mValid = false;
                    break;
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            // nothing
        }

        @Override
        public void onProviderDisabled(String provider) {
            mValid = false;
        }

        public Location current() {
            return mValid ? mLastLocation : null;
        }
    }
}

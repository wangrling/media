package com.google.android.camera.app;

import android.location.Location;

/**
 * A generic interface for a location provider {Fused, GPS, NetWork}.
 */
public interface LocationProvider {

    /**
     * Report when connection fails so another location provider may be used.
     */
    public interface  OnConnectionFailedListener {

        /**
         * Report connection failure.
         */
        void onConnectionFailed();
    }

    /**
     * Get the current location.
     */
    public Location getCurrentLocation();

    /**
     * Trun on/off recording of location.
     */
    public void recordLocation(boolean recordLocation);

    /**
     * Disconnect the location provider after use. The location providre can no longer acquire
     * locations after this has been called.
     */
    public void disconnect();
}

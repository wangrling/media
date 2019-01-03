package com.android.live.camera.app;

import android.location.Location;

/**
 * A generic interface for a location provider {Fused, GPS, Network}.
 */
public interface LocationProvider {

    /**
     * Report when connection fails so another location provider may be used.
     */
    public interface OnConnectionFailedListener {
        /**
         * Report connection failure.
         */
        public void onConnectionFailed();
    }

    /**
     * Get the current location.
     */
    public Location getCurrentLocation();

    /**
     * Turn on/off recording of location.
     *
     * @param recordLocation Whether or not to record location.
     */
    public void recordLocation(boolean recordLocation);

    /**
     * Disconnect the location provider after use. The location provider can no longer acquire
     * locations after this has been called.
     */
    public void disconnect();
}
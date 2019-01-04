package com.android.live.concurrency.composeobject;

import android.graphics.Point;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * All access to state is managed by ConcurrentHashMap, and all the keys and values
 * of the Map are immutable.
 *
 * If we had use the original MutablePoint class instead of Point, we would be
 * breaking encapsulation by letting {@link #getLocations()} publish a reference to
 * mutable state that is not thread-safe.
 */

public class DelegatingVehicleTracker {
    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }

    public Point getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (locations.replace(id, new Point(x, y)) == null) {
            throw new IllegalArgumentException(
                    "invalid vehicle name: " + id);
        }
    }
}

package com.android.live.concurrency.composeobject;

import com.android.live.concurrency.threadsafety.StatelessFactorizer;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe class SafePoint {
    @GuardedBy("this")
    private int x, y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(SafePoint p) {
        this(p.get());
    }

    public SafePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public synchronized int[] get() {
        return new int[] {
                x, y
        };
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

@ThreadSafe
public class PublishingVehicleTracker {
    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;

    public PublishingVehicleTracker(
            Map<String, SafePoint> locations) {
        this.locations =
                new ConcurrentHashMap<>(locations);
        this.unmodifiableMap =
                Collections.unmodifiableMap(this.locations);
    }

    public Map<String, SafePoint> getLoctations() {
        return unmodifiableMap;
    }

    public SafePoint getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (!locations.containsKey(id))
            throw new IllegalArgumentException(
                    "invalid vehicle name: " + id);
        locations.get(id).set(x, y);
    }
}

package com.android.live.concurrency.sharingobjects;

import android.util.EventLog;

/**
 * Using a factory method to prevent the this reference from escaping
 * during construction.
 *
 *
 */

interface EventListener {
    void onEvent(EventLog.Event e);
}

interface EventSource {
    void registerListener(EventListener listener);
}

public class SafeListener {

    private final EventListener listener;

    private SafeListener() {
        listener = new EventListener() {
            @Override
            public void onEvent(EventLog.Event e) {
                // doSomething(e);
            }
        };
    }

    public static SafeListener newInstance(EventSource source) {
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);

        return safe;
    }
}

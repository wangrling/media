package com.android.live.concurrency.composeobject;

import android.widget.TabWidget;

import javax.annotation.concurrent.GuardedBy;

/**
 * The Java monitor pattern is merely a convention; any lock object could be used
 * to guard an object's state so long as it is used consistently.
 */

public class PrivateLock {

    private final Object myLock = new Object();

    @GuardedBy("myLock")
    TabWidget widget;

    void someMethod() {
        synchronized (myLock) {
            // Access or modify the state of widget.
        }
    }
}

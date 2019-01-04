package com.android.live.concurrency.sharingobjects;

import java.util.EventListener;

/**
 * A final mechanism by which an object or its internal state can be published is to
 * publish an inner class instance.
 *
 * Not not allow the this reference to escape during construction.
 */

public class ThisEscape {
/*
    public ThisEscape(EventSource source) {
        source.registerListener(
            new EventListener() {
                public void onEvent(Event e) {
                    doSomething(e);
                }
            });
    }
    */
}

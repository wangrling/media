package com.android.live.concurrency.threadsafety;

/**
 * When a thread requests a lock that is already held by another thread, the requesting
 * thread blocks. But because intrinsic locks are reentrant, if a thread tries to acquire
 * a lock that it already holds, the request succeeds.
 */

public class LoggingWidget extends Widget {
    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething");
    }
}

class Widget {
    public synchronized void doSomething() {

    }
}

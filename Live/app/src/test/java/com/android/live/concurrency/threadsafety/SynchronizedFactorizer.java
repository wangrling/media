package com.android.live.concurrency.threadsafety;

import java.math.BigInteger;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

/**
 * A synchronized block has two parts: a reference to an object that will serve as the
 * lock, and a block of code to be guarded by that lock.
 *
 * Every Java object can implicitly act as a lock for purposes of synchronization;
 * these build-in locks are called intrinsic locks or monitor locks.
 *
 * Intrinsic locks in Java act as mutexes (or mutual exclusion locks), which means that
 * at most noe thread may own the lock.
 *
 * For each mutable state variable that may be accessed by more than one thread, all accesses
 * to that variable must be performed with the same lock held. In this case, we sya that the
 * variable is guarded by that lock.
 *
 * For every invariant that involves more than one variable, all the variables involved in that
 * invariant must bee guarded by the same lock.
 *
 * The way we used synchronization in {@link SynchronizedFactorizer} makes it perform badly.
 * Because service is synchronized, only one thread may execute it at once.
 *
 * The number of simultaneous invocations is limited not by the availability of processing
 * resources, but by the structure of the application itself.
 */

@ThreadSafe
public class SynchronizedFactorizer extends Servlet {

    @GuardedBy("this")  private BigInteger lastNumber;
    @GuardedBy("this")  private BigInteger lastFactor;

    @Override
    synchronized void  service(Object req, Object resp) {
        if (lastNumber.equals(lastFactor)) {
            encodeIntoResponse(req);
        } else {
            lastNumber = null;
            lastFactor = null;
            encodeIntoResponse(resp);
        }
    }
}

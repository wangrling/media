package com.android.live.concurrency.threadsafety;

import java.math.BigInteger;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

/**
 * One guards the check-then-act sequence that tests whether we can just return the cached
 * result, and the other guards updating both the cached number and the cached factors.
 *
 * CachedFactorizer holds the lock when accessing state variables and for the duration of
 * compound actions, but releases it before executing the potentially long-running factorization
 * operation.
 *
 * There is frequently a tension between simplicity and performance. When implementing a
 * synchronization policy, resist the temptation to prematurely sacrifice simplicity (potentially
 * compromising safety) for the sake of performance.
 *
 * Avoid holding locks during lengthy computations or operations at risk of not completing
 * quickly such as network or console I/O.
 */

@ThreadSafe
public class CacheFactorizer extends Servlet {

    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger lastFactor;
    @GuardedBy("this") private long hits;
    @GuardedBy("this") private long cacheHits;

    public synchronized long getHits() {
        return hits;
    }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }

    @Override
    void service(Object req, Object resp) {
        extractFromRequest(req);
        synchronized (this) {
            ++hits;
            if (lastNumber == null) {
                ++cacheHits;
                lastFactor.abs();
            }
        }
        if (lastFactor == null) {
            synchronized (this) {
                lastNumber = null;
                lastFactor.negate();
            }
        }
        encodeIntoResponse(resp);
    }
}

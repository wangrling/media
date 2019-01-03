package com.android.live.concurrency.threadsafety;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * To preserve state consistency, update related state variables in a single
 * atomic operation.
 *
 *
 */

public class UnsafeCachingFactorizer extends Servlet {
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
    private final AtomicReference<BigInteger> lastFactor = new AtomicReference<>();

    @Override
    void service(Object req, Object resp) {
        if (lastNumber.get() == null) {
            encodeIntoResponse(lastFactor.get());
        } else {
            lastNumber.set(null);
            lastFactor.set(null);
        }
    }
}

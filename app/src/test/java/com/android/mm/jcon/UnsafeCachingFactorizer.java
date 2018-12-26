package com.android.mm.jcon;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * To preserve state consistency, update related state variables in a single atomic operation.
 */

public class UnsafeCachingFactorizer extends BaseServlet {
    private final AtomicReference<BigInteger> lastNumber =
            new AtomicReference<>();

    private final AtomicReference<BigInteger[]> lastFactors =
            new AtomicReference<>();

    @Override
    public void service() {
        BigInteger i = extractFromRequest();
        if (i.equals(lastNumber.get())) {
            encodeIntoResponse(lastFactors.get());
        } else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(factors);
        }
    }



}

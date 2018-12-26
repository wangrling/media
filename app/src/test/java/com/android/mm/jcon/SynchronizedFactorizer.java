package com.android.mm.jcon;

import org.junit.runner.notification.RunListener;

import java.math.BigInteger;

import androidx.annotation.GuardedBy;

@RunListener.ThreadSafe
public class SynchronizedFactorizer extends BaseServlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;

    @Override
    public synchronized void service() {
        BigInteger i = extractFromRequest();

        if (i.equals(lastNumber)) {
            encodeIntoResponse(lastFactors);
        } else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(factors);
        }
    }
}

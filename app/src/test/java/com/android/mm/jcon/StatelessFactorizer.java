package com.android.mm.jcon;

import java.io.IOException;
import java.math.BigInteger;

// Stateless objects are always thread-safe.

public class StatelessFactorizer extends BaseServlet {

    @Override
    public void service() {
        BigInteger i = extractFromRequest();
        BigInteger[] factors = factor(i);
        encodeIntoResponse(factors);
    }
}

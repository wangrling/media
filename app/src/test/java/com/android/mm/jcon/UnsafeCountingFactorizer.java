package com.android.mm.jcon;

import javax.annotation.concurrent.NotThreadSafe;
/**
 * While the increment operation, ++count, may look like a single
 * action because of its compact syntax, it is not atomic, which
 * means that it does not execute as a single, indivisible operation.
 */

@NotThreadSafe
public class UnsafeCountingFactorizer extends BaseServlet {

    private long count = 0;

    @Override
    public void service() {
        ++count;
    }
}

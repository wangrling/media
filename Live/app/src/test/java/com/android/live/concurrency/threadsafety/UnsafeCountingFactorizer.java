package com.android.live.concurrency.threadsafety;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Race conditions that make its results unreliable.
 * A race condition occurs when the correctness of a computation depends on the relative
 * timing or interleaving of multiple threads by the runtime; in other words, when getting
 * the right answer relies on luck timing.
 */
@NotThreadSafe
public class UnsafeCountingFactorizer extends Servlet {

    private long count = 0;

    public long getCount() {
        return count;
    }

    @Override
    void service(Object req, Object resp) {
        extractFromRequest(req);
        ++count;
        encodeIntoResponse(resp);
    }
}

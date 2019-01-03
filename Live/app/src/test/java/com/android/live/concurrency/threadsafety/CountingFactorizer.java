package com.android.live.concurrency.threadsafety;


import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Operations A and B are atomic with respect to each other if, from the perspective
 * of a thread executing A, when another thread executes B, either all of B has executed
 * or none of it has. An atomic operation is one that is atomic with respect to all
 * operations, including itself, that operate on the same state.
 *
 * Where practical, use existing thread-safe objects, like {@link AtomicLong}, to manage your
 * class's state. It is simpler to reason about te possible states and state transitions for
 * existing thread-safe objects than it is for arbitrary state varibles, and this make it
 * easier to maintain and verify thread safety.
 */

@ThreadSafe
public class CountingFactorizer extends Servlet {

    private final AtomicLong count = new AtomicLong(0);

    public long getCount() {
        return count.get();
    }


    @Override
    void service(Object req, Object resp) {
        extractFromRequest(req);
        count.incrementAndGet();
        encodeIntoResponse(resp);
    }
}

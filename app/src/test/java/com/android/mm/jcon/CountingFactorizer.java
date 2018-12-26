package com.android.mm.jcon;
import java.util.concurrent.atomic.AtomicLong;
/**
 * Operations A and B are atomic with respect to each other if, from the
 * perspective of a thread executing A, when another thread executes B,
 * either all of B has executed or none of it has.
 */

public class CountingFactorizer extends BaseServlet {

    private final AtomicLong count = new AtomicLong(0);

    @Override
    public void service() {
        count.incrementAndGet();
    }
}

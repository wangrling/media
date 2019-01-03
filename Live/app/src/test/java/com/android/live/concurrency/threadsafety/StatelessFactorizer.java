package com.android.live.concurrency.threadsafety;

/**
 * Stateless objects are always thread-safe.
 * 没有变量的对象总是线程安全的。
 */

public class StatelessFactorizer extends Servlet {

    @Override
    public void service(Object req, Object resp) {
        extractFromRequest(req);
        encodeIntoResponse(resp);
    }
}

package com.android.live.concurrency.buildblock;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.annotation.concurrent.GuardedBy;

public class Caching {

}

/**
 * Describe a function with input of type A and result of type V.
 */
interface Computeable<A, V> {
    V compute(A arg) throws InterruptedException;
}

/**
 * ExpensiveFunction , which implements Computable , takes a long time to compute its result;
 */
class ExpensiveFunction implements Computeable<String, BigInteger> {

    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        // after deep though...
        return new BigInteger(arg);
    }
}

// create a Computable wrapper that remembers the results of previous computations and encapsulates
//the caching process.


/**
 * HashMap is not thread-safe, so to ensure that two threads do not access the
 * HashMap at the same time, Memoizer1 takes the conservative approach of synchronizing
 * the entire compute method.
 * This ensures thread safety but has an obvious scalability problem: only one
 * thread at a time can execute compute at all.
 */
class Memoizer1<A, V> implements Computeable<A, V> {

    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<A, V>();
    private final Computeable<A, V> c;

    public Memoizer1(Computeable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}

/**
 * Memoizer2 certainly has better concurrent behavior than Memoizer1 : multiple
 * threads can actually use it concurrently. But it still has some defects as a cache—
 * there is a window of vulnerability in which two threads calling compute at the
 * same time could end up computing the same value.
 *
 * thread X is currently computing f ( 27 ) ”, so that if
 * another thread arrives looking for f ( 27 ) , it knows that the most efficient way to
 * find it is to head over to Thread X’s house, hang out there until X is finished, and
 * then ask “Hey, what did you get for f ( 27 ) ?
 *
 * 保证两个线程操作Map同步，但是不能保证连续操作同步。
 */
class Memoizer2<A, V> implements Computeable<A, V> {

    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computeable<A, V> c;

    public Memoizer2(Computeable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}

/**
 * 把Callable放到FutureTask中运行。
 */
class Memoizer3<A, V> implements Computeable<A, V> {
    private final Map<A, Future<V>> cache =
            new ConcurrentHashMap<>();

    private final Computeable<A, V> c;

    public Memoizer3(Computeable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> eval = new Callable<V>() {
                @Override
                public V call() throws InterruptedException {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<>(eval);
            f = ft;
            cache.put(arg, ft);
            ft.run();
        }

        try {
            return f.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}

class Memoizer<A, V> implements Computeable<A, V> {

    private final Map<A, Future<V>> cache =
            new ConcurrentHashMap<>();

    private final Computeable<A, V> c;

    public Memoizer(Computeable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                // 定义一个Callable变量，返回类型是V
                Callable<V> eval = new Callable<V>() {

                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.android.live.concurrency.buildblock;

import java.util.Vector;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class SafeList {

    public static Object getLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static void deleteLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }

    Vector vector;



    public void iteratorList() {
        synchronized (vector) {
            for (int i = 0; i < vector.size(); i++) {
                // doSomething(vector.get(i));
            }
        }
    }
}

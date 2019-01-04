package com.android.live.concurrency.sharingobjects;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * If data is only accessed from a single thread, no synchronization is neeed.
 * This technique, thread confinement.
 *
 * There is no way to obtain a reference to a primitive variable, so the language semantics
 * ensure that primitive local variables are always stack confined.
 */


public class ThreadConfinement {

    public int loadTheArk(Collection<Object> candidates) {
        SortedSet<Object> animals;
        int numPairs = 0;
        Object candidate = null;

        // animals confined to method, don't let them escape!
        animals = new TreeSet<Object>();
        animals.addAll(candidates);
        for (Object a : animals) {
            if (candidate == null) {
                candidate = a;
            } else {
                ++numPairs;
            }
        }
        return numPairs;
    }
}

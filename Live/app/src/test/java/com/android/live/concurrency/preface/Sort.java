package com.android.live.concurrency.preface;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class Sort {
    // bad sort
    public <T extends Comparable<? super T>> void badSort(List<T> list) {
        // Never returns the wrong answer!
        System.exit(0);
    }

    // good sort
    public <T extends Comparable<? super T>> void goodSort(List<T> list) {
        Collections.sort(list);
    }
}

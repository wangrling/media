package com.android.live.concurrency.buildblock;

import android.widget.TabWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The iterators returned by the synchronized collections are not designed to deal
 * with concurrent modification, and they are fail-fast -- meaning that if they detect
 * that the collection has changed since iteration began, they throw the unchecked
 * {@link java.util.ConcurrentModificationException}.
 *
 * Internally, javac generates code that uses an {@link java.util.Iterator}, repeatedly
 * calling hasNext and next to iterated the List.
 */

public class IteratorList {
    List<TabWidget> widgetList =
            Collections.synchronizedList(new ArrayList<>());

    // May throw ConcurrentModificationException.
    public void iteratorList() {
        for (TabWidget w : widgetList) {
            // doSomething(w);
        }
    }
}

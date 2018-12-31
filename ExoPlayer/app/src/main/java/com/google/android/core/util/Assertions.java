package com.google.android.core.util;

/**
 * Provides methods for asserting the truth of expressions and properties.
 */
public final class Assertions {

    private Assertions() {

    }

    public static int checkIndex(int index, int start, int limit) {
        if (index < start || index > limit) {
            throw new IndexOutOfBoundsException();
        }
        return index;
    }
}

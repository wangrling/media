package com.android.media.player.core.util;

import com.android.media.player.core.ExoPLayerLibraryInfo;

/**
 * Provides methods for asserting the truth of expressions and  properties.
 */
public class Assertions {

    private Assertions() {}

    /**
     * Throws {@link IllegalArgumentException} if {@code expression} evaluates to false.
     *
     * @param expression    The expression to evaluate.
     * @throws IllegalArgumentException If {@code expression} is false.
     */
    public static void checkArgument(boolean expression) {
        if (ExoPLayerLibraryInfo.ASSERTIONS_ENABLED && !expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Throws {@link IllegalArgumentException} if {@code expression} evaluates to false
     *
     * @param expression    The expression to evaluate.
     * @param errorMessage  The exception message if an exception is thrown. The message is converted
     *                      to a {@link String} using {@link String#valueOf(Object)}.
     * @throws IllegalArgumentException If {@code expression} is false.
     */
    public static void checkArgument(boolean expression, Object errorMessage) {
        if (ExoPLayerLibraryInfo.ASSERTIONS_ENABLED && !expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    /**
     * Throws {@link IndexOutOfBoundsException} if {@code index} fall outside the specified bounds.
     *
     * @param index The index to test.
     * @param start The start of the allowed range (inclusive).
     * @param limit The end of the allowed range (exclusive).
     * @return  The {@code index} that was validated.
     * @throws IndexOutOfBoundsException If {@code index} falls outside specified bounds.
     */
    public static int checkIndex(int index, int start, int limit) {
        if (index < start || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        return index;
    }

}

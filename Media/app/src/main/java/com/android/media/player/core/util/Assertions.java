package com.android.media.player.core.util;

import android.os.Looper;
import android.text.TextUtils;

import com.android.media.player.core.ExoPLayerLibraryInfo;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.w3c.dom.Text;

import androidx.annotation.Nullable;

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
            // valueOf返回Object.toString()值。
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

    /**
     * Throws {@link IllegalStateException} if {@code expression} evaluates to false.
     *
     * @param expression    The expression to evaluate.
     * @throws IllegalStateException    If {@code expression} is false.
     */
    public static void checkState(boolean expression) {
        if (ExoPLayerLibraryInfo.ASSERTIONS_ENABLED && !expression) {
            throw new IllegalStateException();
        }
    }

    /**
     * Throws {@link IllegalStateException} if {@code expression} evaluates to false.
     *
     * @param expression    The expression to evaluate.
     * @param errorMessage  The exception message if an exception is thrown. The message is
     *                      converted to a {@link String} using {@link String#valueOf(Object)}.
     */
    public static void checkState(boolean expression, Object errorMessage) {
        if (ExoPLayerLibraryInfo.ASSERTIONS_ENABLED && !expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    /**
     * Throws {@link NullPointerException} if {@code reference} is null.
     *
     * @param reference The reference.
     * @param <T>   The type of the reference.
     * @return  The non-null reference that was validated.
     * @throws NullPointerException if {@code reference} is null.
     */
    @SuppressWarnings({"contracts.postcondition.not.satisfied", "return.type.incompatible"})
    @EnsuresNonNull({"#1"})
    public static <T> T checkNotNull(@Nullable T reference) {
        if (ExoPLayerLibraryInfo.ASSERTIONS_ENABLED && reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Throws {@link NullPointerException} if {@code reference} is null.
     *
     * @param reference The reference.
     * @param errorMessage  The exception message to use if the check fails. The message is
     *                      converted to a string using {@link String#valueOf(Object)}.
     * @param <T>
     * @return
     */
    @SuppressWarnings({"contracts.postcondition.not.satisfied", "return.type.incompatible"})
    @EnsuresNonNull("#1")
    public static <T> T checkNotNull(@Nullable T reference, Object errorMessage) {
        if (ExoPLayerLibraryInfo.ASSERTIONS_ENABLED && reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * Throws {@link IllegalArgumentException} if {@code string} is null or zero length.
     *
     * @param string    The string to check.
     * @return  The non-null, non-empty string that was validated.
     * @throws IllegalArgumentException If {@code string} is null or 0-length.
     */
    @SuppressWarnings({"contracts.postcondition.not.satisfied", "return.type.incompatible"})
    @EnsuresNonNull("#1")
    public static String checkNotEmpty(@Nullable String string) {
        if (ExoPLayerLibraryInfo.ASSERTIONS_ENABLED && TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException();
        }
        return string;
    }

    /**
     * Throws {@link IllegalArgumentException} if {@code string} is null or zero length.
     *
     * @param string    The string to check.
     * @param errorMessage  The exception message to use if the check fails. The message is
     *                      converted to a string using {@link String#valueOf(Object)}.
     * @return  The non-null, non-empty string that was validated.
     * @throws IllegalArgumentException If {@code string} is null or 0-length.
     */
    @SuppressWarnings({"contracts.postcondition.not.satisfied", "return.type.incompatible"})
    @EnsuresNonNull("#1")
    public static String checkNotEmpty(@Nullable String string, Object errorMessage) {
        if (ExoPLayerLibraryInfo.ASSERTIONS_ENABLED && TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
        return string;
    }

    /**
     * Throw {@link IllegalStateException} if the calling thread is not hte application's main
     * thread.
     *
     * @throws IllegalStateException If the calling thread is not the application's main thread.
     */
    // UI activity有main looper
    public static void checkMainThread() {
        if (ExoPLayerLibraryInfo.ASSERTIONS_ENABLED && Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Not in applications main thread");
        }
    }
}

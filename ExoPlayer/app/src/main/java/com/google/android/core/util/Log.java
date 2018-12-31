package com.google.android.core.util;

import android.text.TextUtils;

import androidx.annotation.Nullable;

/**
 * Wrapper around {@link android.util.Log} which allows to set the log level.
 */
public class Log {

    // 定义一个全局的TAG
    private static final String TAG = "ExoPlayer";

    /**
     * Log level for ExoPlayer logcat logging. One of {@link #LOG_LEVEL_ALL}, {@link #LOG_LEVEL_INFO},
     * {@link #LOG_LEVEL_WARNING}, {@link #LOG_ELVEL_ERROR} or {@link #LOG_LEVEL_OFF}.
     */


    /** Log level to log all messages. */
    public static final int LOG_LEVEL_ALL = 0;
    /** Log level to only log informative, warning and error messages. */
    public static final int LOG_LEVEL_INFO = 1;


    private static int logLevel = LOG_LEVEL_ALL;

    private static boolean logStackTraces = true;

    private Log() {}

    // DEBUG等级最低

    // 仅DEBUG使用全局TAG
    public static void d(String message) {
        if (logLevel == LOG_LEVEL_ALL) {
            android.util.Log.d(TAG, message);
        }
    }

    /** See android.util.Log#d(String, String message) */
    public static void d(String tag, String message) {
        if (logLevel == LOG_LEVEL_ALL) {
            android.util.Log.d(tag, message);
        }
    }

    /** @see android.util.Log#d(String, String, Throwable) */
    public static void d(String tag, String message, @Nullable Throwable throwable) {
        if (!logStackTraces) {
            // 把throwable附加到message上。
            d(tag, appendThrowableMessage(message, throwable));
        }
        if (logLevel == LOG_LEVEL_ALL) {
            android.util.Log.d(tag, message, throwable);
        }
    }



    private static String appendThrowableMessage(String message, @Nullable Throwable throwable) {
        if (throwable == null) {
            return message;
        }

        String throwableMessage = throwable.getMessage();

        return TextUtils.isEmpty(throwableMessage) ? message : message + " - " + throwableMessage;
    }
}

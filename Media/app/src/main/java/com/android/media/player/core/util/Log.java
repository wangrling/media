package com.android.media.player.core.util;

import android.text.TextUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

/** Wrapper around {@link android.util.Log} which allows to set the log level. */
public class Log {

    /**
     * Log level for ExoPlayer logcat logging. One of {@link #LOG_LEVEL_ALL}, {@link #LOG_LEVEL_INFO},
     * {@link #LOG_LEVEL_WARNING}, {@link #LOG_LEVEL_ERROR} or {@link #LOG_LEVEL_OFF}.
     */
    // Indicates that annotations with a type are to be documented by javadoc and similar tools
    // by default.
    @Documented
    // 定义该Annotation被保留的时间长短，SOURCE表示源文件中有效。
    @Retention(RetentionPolicy.SOURCE)
    // 定义int型的枚举
    @IntDef({LOG_LEVEL_ALL, LOG_LEVEL_INFO, LOG_LEVEL_WARNING, LOG_LEVEL_ERROR, LOG_LEVEL_OFF})
    @interface LogLevel {}
    /** Log level to log all messages. */
    public static final int LOG_LEVEL_ALL = 0;
    /** Log level to only informative, warning and error message. */
    public static final int LOG_LEVEL_INFO = 1;
    /** Log level to only warning and error messages. */
    public static final int LOG_LEVEL_WARNING = 2;
    /** Log level to only log error messages. */
    public static final int LOG_LEVEL_ERROR = 3;
    /** Log level to disable all logging. */
    public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;

    private static int logLevel = LOG_LEVEL_ALL;
    private static boolean logStackTraces = true;

    private Log() {}

    // 定义logLevel和logStackTraces的get/set函数。

    /**
     * @return  Current {@link LogLevel} for ExoPlayer logcat logging.
     */
    public static @LogLevel int getLogLevel() {
        return logLevel;
    }

    /**
     * @return  Whether stack traces of {@link Throwable}s will be logged to logcat.
     */
    public static boolean getLogStackTraces() {
        return logStackTraces;
    }

    /**
     * Sets the {@link LogLevel} for ExoPlayer logcat logging.
     *
     * @param logLevel  The new {@link LogLevel}.
     */
    public static void setLogLevel(@LogLevel int logLevel) {
        // 静态数据引用
        Log.logLevel = logLevel;
    }

    /**
     * Sets whether stack traces of {@link Throwable}s will be logged to logcat.
     *
     * @param logStackTraces    Whether stack traces will be logged.
     */
    public static void setLogStackTraces(boolean logStackTraces) {
        Log.logStackTraces = logStackTraces;
    }

    // 2x5个log输出函数

    /** @see android.util.Log#d(String, String) */
    public static void d(String tag, String message) {
        if (logLevel == LOG_LEVEL_ALL) {
            android.util.Log.d(tag, message);
        }
    }

    /** @see android.util.Log#d(String, String, Throwable) */
    public static void d(String tag, String message, @Nullable Throwable throwable) {
        if (logLevel == LOG_LEVEL_ALL) {
            if (logStackTraces) {
                android.util.Log.d(tag, message, throwable);
            } else {
                d(tag, appendThrowableMessage(message, throwable));
            }
        }
    }

    /** @see android.util.Log#i(String, String) */
    public static void i(String tag, String message) {
        if (logLevel <= LOG_LEVEL_INFO) {
            // log等级越低，打印的log越多。
            android.util.Log.i(tag, message);
        }
    }

    /**@see android.util.Log#i(String, String, Throwable) */
    public static void i(String tag, String message, @Nullable Throwable throwable) {
        if (logLevel <= LOG_LEVEL_INFO) {
            if (logStackTraces) {
                android.util.Log.i(tag, message, throwable);
            } else {
                i(tag, appendThrowableMessage(message, throwable));
            }
        }
    }

    /** @see android.util.Log#w(String, String) */
    public static void w(String tag, String message) {
        if (logLevel <= LOG_LEVEL_WARNING) {
            android.util.Log.w(tag, message);
        }
    }

    /** @see android.util.Log#w(String, String, Throwable) */
    public static void w(String tag, String message, Throwable throwable) {
        if (logStackTraces) {
            android.util.Log.w(tag, message, throwable);
        } else {
            w(tag, appendThrowableMessage(message, throwable));
        }
    }

    /** android.util.Log#e(String, String) */
    public static void e(String tag, String message) {
        if (logLevel <= LOG_LEVEL_ERROR) {
            android.util.Log.e(tag, message);
        }
    }

    /** android.util.Log#e(String, String, Throwable) */
    public static void e(String tag, String message, Throwable throwable) {
        if (logLevel <= LOG_LEVEL_ERROR) {
            if (logStackTraces) {
                android.util.Log.w(tag, message, throwable);
            } else {
                e(tag, appendThrowableMessage(message, throwable));
            }
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

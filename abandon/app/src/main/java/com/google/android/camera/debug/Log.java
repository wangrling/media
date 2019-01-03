package com.google.android.camera.debug;

import androidx.annotation.NonNull;

public class Log {
    /**
     * All camera logging using using class will use this tag prefix.
     */
    public static final String CAMERA_LOGTAG_PREFIX = "CAM_";
    private static final Log.Tag TAG = new Log.Tag("Log");
    public static final class Tag {

        // The length limit from Android framework is 23.
        private static final int MAX_TAG_LEN = 23 - CAMERA_LOGTAG_PREFIX.length();

        final String mValue;

        public Tag(String tag) {
            final int lenDiff = tag.length() - MAX_TAG_LEN;
            if (lenDiff > 0) {
                w(TAG, "Tag " + tag + " is " + lenDiff + "  chars longer than limit.");
            }
            // 限制tag的最大长度为23个字符。
            mValue = CAMERA_LOGTAG_PREFIX + (lenDiff > 0 ? tag.substring(0, MAX_TAG_LEN) : tag);
        }

        @NonNull
        @Override
        public String toString() {
            return mValue;
        }
    }

    // DEBUG
    public static void d(Tag tag, String msg) {
        android.util.Log.d(tag.toString(), msg);
    }

    public static void d(Tag tag, Object instance, String msg) {
        android.util.Log.d(tag.toString(), LogUtil.addTags(instance, msg));
    }

    public static void d(Tag tag, Object instance, String msg, String tags) {
        android.util.Log.d(tag.toString(), LogUtil.addTags(instance, msg, tags));
    }

    public static void d(Tag tag, String msg, Throwable tr) {
        android.util.Log.d(tag.toString(), msg, tr);
    }

    // ERROR
    public static void e(Tag tag, String msg) {
        android.util.Log.e(tag.toString(), msg);
    }

    public static void e(Tag tag, Object instance, String msg) {
        android.util.Log.e(tag.toString(), LogUtil.addTags(instance, msg));
    }

    public static void e(Tag tag, Object instance, String msg, String tags) {
        android.util.Log.e(tag.toString(), LogUtil.addTags(instance, msg, tags));
    }

    public static void e(Tag tag, String msg, Throwable tr) {
        android.util.Log.e(tag.toString(), msg, tr);
    }

    // INFO
    public static void i(Tag tag, String msg) {
        android.util.Log.i(tag.toString(), msg);
    }

    public static void i(Tag tag, Object instance, String msg) {
        android.util.Log.i(tag.toString(), LogUtil.addTags(instance, msg));
    }

    public static void i(Tag tag, Object instance, String msg, String tags) {
        android.util.Log.i(tag.toString(), LogUtil.addTags(instance, msg, tags));
    }

    public static void i(Tag tag, String msg, Throwable tr) {
        android.util.Log.i(tag.toString(), msg, tr);
    }

    // VERBOSE
    public static void v(Tag tag, String msg) {
        android.util.Log.v(tag.toString(), msg);
    }

    public static void v(Tag tag, Object instance, String msg) {
        android.util.Log.v(tag.toString(), LogUtil.addTags(instance, msg));
    }

    public static void v(Tag tag, Object instance, String msg, String tags) {
        android.util.Log.v(tag.toString(), LogUtil.addTags(instance, msg, tags));
    }

    public static void v(Tag tag, String msg, Throwable tr) {
        android.util.Log.v(tag.toString(), msg, tr);
    }

    // WARNING
    public static void w(Tag tag, String msg) {
        android.util.Log.v(tag.toString(), msg);
    }

    public static void w(Tag tag, Object instance, String msg) {
        android.util.Log.w(tag.toString(), LogUtil.addTags(instance, msg));
    }

    public static void w(Tag tag, Object instance, String msg, String tags) {
        android.util.Log.w(tag.toString(), LogUtil.addTags(instance, msg, tags));
    }

    public static void w(Tag tag, String msg, Throwable tr) {
        android.util.Log.w(tag.toString(), msg, tr);
    }
}

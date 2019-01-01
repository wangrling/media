package com.google.android.camera.debug;

import com.google.android.camera.async.MainThread;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Helpful logging extensions for including more detailed info about the objects and threads
 * involved in the logging.
 */

public class LogUtil {

    /**
     * Prefixes a message with a hashcode tag of the object,
     * and a [ui] tag if the method was executed on the main thread.
     */
    public static String addTags(Object object, String msg) {
        return hashCodeTag(object) + mainThreadTag() + " " + msg;
    }

    /**
     * Prefixes a message with the bracketed tag specified in the tag list,
     * along with a hashcode tag of the object, and a [ui] tag if the method
     * was executed on the main thread.
     */
    public static String addTags(Object object, String msg, String tagList) {
        return hashCodeTag(object) + mainThreadTag() + formatTags(tagList);
    }

    private static String formatTags(String tagList) {
        // Split on common "divider" characters:
        // * All whitespace, except spaces: \x00-\x1F
        // * () Characters: \x28-\x29
        // * , Character: \x2C
        // * / Character: \x2F
        // * ;<=>? Characters: \x3B-\x3F
        // * [\] Characters: \x5B-\x5D
        // * {|} Characters: \x7B-\x7D
        // 碰到上面的字符不进行分割
        List<String> tags = Arrays.asList(tagList.split("[\\x00-\\x1F\\x28-\\x29\\x2C\\x2F"
                + "\\x3B-\\x3F\\x5B-\\x5D\\x7B-\\x7D]"));
        Collections.sort(tags);
        String result = "";
        // Convert all non-empty entries to tags.
        for (String tag : tags) {
            String trimmed = tag.trim();
            if (trimmed.length() > 0) {
                result += "[" + trimmed + "]";
            }
        }
        return result;
    }

    private static String hashCodeTag(Object object) {
        final String tag;
        if (object == null) {
            tag = "null";
        } else {
            tag = Integer.toHexString(Objects.hashCode(object));
        }
        // 最长9位
        return String.format("[%-9s]", "@" +tag);
    }

    private static String mainThreadTag() {
        return MainThread.isMainThread() ? "[ui]" : "";
    }
}

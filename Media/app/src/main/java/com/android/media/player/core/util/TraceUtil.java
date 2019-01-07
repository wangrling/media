package com.android.media.player.core.util;

import android.annotation.TargetApi;
import android.os.Trace;

import com.android.media.player.core.ExoPLayerLibraryInfo;

/**
 * Calls through to {@link android.os.Trace} method on supported API levels.
 */
public final class TraceUtil {
    private TraceUtil() {

    }

    /**
     * Writes a trace to {@link android.os.Trace} methods on supported API levels.
     *
     * @see android.os.Trace#beginSection(String)
     *
     * @param sectionName   The name of the code section to appear in the trace. This
     *                      may be at most 127 Unicode code units long.
     */
    public static void beginSection(String sectionName) {
        if (ExoPLayerLibraryInfo.TRACE_ENABLED && Util.SDK_INT >= 18) {
            beginSectionV18(sectionName);
        }
    }

    /**
     * Write a trace message to indicate that a given section of code has ended.
     *
     * @see Trace#endSection()
     */
    public static void endSection() {
        if (ExoPLayerLibraryInfo.TRACE_ENABLED && Util.SDK_INT >= 18) {
            endSectionV18();
        }
    }

    @TargetApi(18)
    private static void beginSectionV18(String sectionName) {
        android.os.Trace.beginSection(sectionName);
    }

    @TargetApi(18)
    private static void endSectionV18() {
        android.os.Trace.endSection();
    }
}

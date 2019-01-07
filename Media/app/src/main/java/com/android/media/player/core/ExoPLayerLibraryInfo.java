package com.android.media.player.core;

import com.android.media.player.core.util.Assertions;
import com.android.media.player.core.util.TraceUtil;

import java.util.HashSet;

/**
 * Information about the ExoPlayer library.
 */
public class ExoPLayerLibraryInfo {

    /**
     * A tag to use when logging library information.
     */
    public static final String TAG = "ExoPlayer";

    /** The version of the library expressed as a string, for example "1.2.3". */
    // Intentionally hardcoded. Do not derive from other constants (e.g. VERSION_INT) or vice versa.
    public static final String VERSION = "2.9.3";

    /**
     * The version of the library expressed as {@code "ExoPlayerLib/" + VERSION}.
     */
    // Intentionally hardcoded. Do not derive from other constants (e.g. VERSION) or vice versa.
    public static final String VERSION_SLASHY = "ExoPlayerLib/2.9.3";

    /**
     * The version of the library expressed as an integer, for example 1002003.
     * <p>
     * Three digits are used for each component of {@link #VERSION}. For example "1.2.3" has the
     * corresponding integer version 1002003 (001-002-003), and "123.45.6" has the corresponding
     * integer version 123045006 (123-045-006).
     */
    // Intentionally hardcoded. Do not derive from other constants (e.g. VERSION) or vice versa.
    public static final int VERSION_INT = 2009003;

    /**
     * Whether the library was compiled with {@link Assertions} checks enabled.
     */
    public static final boolean ASSERTIONS_ENABLED = true;

    /**
     * Whether an exception should be thrown in case of an OpenGl error.
     */
    public static final boolean GL_ASSERTIONS_ENABLED = false;

    /**
     * Whether the library was compiled with {@link TraceUtil} trace enabled.
     */
    public static final boolean TRACE_ENABLED = true;

    // 模块注册相关

    private static final HashSet<String> registerModules = new HashSet<>();

    private static String registeredModuleString = "goog.exo.core";

    // Prevents instantiation.
    private ExoPLayerLibraryInfo() { }

    // 防止多线程同时操作，在注册的时候同时在获取，或者多个线程同时注册。

    /**
     * 返回所有注册的模块，所用逗号作为分割。
     *
     * @return  A string consisting of registered module names separated by ",".
     */
    public static synchronized String registeredModules() {
        return registeredModuleString;
    }

    /**
     * Registers a module to be returned in the {@link #registeredModules()} string.
     *
     * @param name  The name of the module being registered.
     */
    public static synchronized void registerModule(String name) {
        if (registerModules.add(name)) {
            registeredModuleString = registeredModuleString + ", " + name;
        }
    }
}

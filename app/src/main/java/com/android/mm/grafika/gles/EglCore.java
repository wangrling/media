package com.android.mm.grafika.gles;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.util.Log;
import android.view.Surface;

// 主要看EGL协议

import static android.opengl.EGLExt.EGL_RECORDABLE_ANDROID;


/**
 * Core EGL state (display, context, config)
 * 包含EGLDisplay, EGLContext, EGLConfig核心元素。
 * 不包含Surface画布。
 * <p>
 *     The EGLContext must only be attach to one thread at a time. This class is not thread-safe.
 * </p>
 */

public class EglCore {
    private static final String TAG = GlUtil.TAG;

    /**
     * Constructor flag: surface must be recordable. This discourages EGL from using a
     * pixel format that cannot be converted efficiently to something usable by the video
     * encoder.
     */
    public static final int FLAG_RECORDABLE = 0x01;

    /**
     * Constructor flag: ask for GLES3, fall back to GLES2 if not available.  Without this
     * flag, GLES2 is used.
     */
    public static final int FLAG_TRY_GLES3 = 0x02;

    /**
     * Represents the abstract display on which graphics are drawn, in most environments
     * a display corresponds to a single physical screen.
     */
    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;

    /**
     * EGLContext is an opaque type representing a client API context.
     */
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;

    /**
     * An EGLConfig describes the format, type and size of the color buffers and
     * ancillary(辅助的) buffers for an EGLSurface.
     * 定义画笔的类型，例如是铅笔还是钢笔，是红色还是黑色。
     */
    private EGLConfig mEGLConfig = null;

    // OpenGL ES的版本
    private int mGlVersion = -1;

    /**
     * Prepares EGL display and context.
     * Equivalent to EglCore(null, 0).
     */
    public EglCore() {
        this(null, 0);
    }

    public EglCore(EGLContext sharedContext, int flags) {
        if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("EGL already set up");
        }

        if (sharedContext == null) {
            sharedContext = EGL14.EGL_NO_CONTEXT;
        }

        /**
         * Multiple calls made to eglGetDisplay with the same display_id will
         * return the same EGLDisplay handle.
         * If no display matching display_id is available, EGL_NO_DISPLAY is returned;
         * no error condition is raised in this case.
         */
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display");
        }

        int[] version = new int[2];
        /**
         * Native层对应的函数
         * EGLBoolean eglInitialize(EGLDisplay dpy, EGLint *major, EGLint *minor);
         * 将版本信息存入到version数组中。
         */
        if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
            mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }

        // Try to get a GLES3 context, if requested.
        if ((flags & FLAG_TRY_GLES3) != 0) {
            // Log.d(TAG, "Trying GLES 3");
            EGLConfig config = getConfig(flags, 3);
            if (config != null) {
                int[] attrib3_list = {
                        EGL14.EGL_CONTEXT_CLIENT_VERSION, 3,
                        EGL14.EGL_NONE
                };
                // attrib_list specifies a list of attributes for the context. The list has the same
                // structure as describe for eglChooseConfig.
                EGLContext context = EGL14.eglCreateContext(mEGLDisplay, config, sharedContext, attrib3_list, 0);
                if (EGL14.eglGetError() == EGL14.EGL_SUCCESS) {
                    Log.d(TAG, "Got GLES 3 config");
                    mEGLConfig = config;
                    mEGLContext = context;
                    mGlVersion = 3;
                }
            }
        }
        // 获取GLES 3 失败
        if (mEGLContext == EGL14.EGL_NO_CONTEXT) {  // GLES 2 only, or GLES 3 attempt failed.
            Log.d(TAG, "Trying GLES 2");
            EGLConfig config = getConfig(flags, 2);
            if (config == null) {
                throw new RuntimeException("Unable to find a suitable EGLConfig");
            }
            int[] attrib2_list = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL14.EGL_NONE
            };
            EGLContext context = EGL14.eglCreateContext(mEGLDisplay, config, sharedContext,
                    attrib2_list, 0);
            checkEglError("eglCreateContext");
            mEGLConfig = config;
            mEGLContext = context;
            mGlVersion = 2;
        }

        // Confirm with query.
        int[] values = new int[1];

        // 可以查询的类型
        // EGL_CONFIG_ID, EGL_CONTEXT_CLIENT_TYPE, EGL_CONTEXT_CLIENT_VERSION, EGL_RENDER_BUFFER.
        // Querying EGL_CONTEXT_CLIENT_VERSION returns the version of the client API this context
        // actually supports (which may differ from the version specified at context creation time).
        EGL14.eglQueryContext(mEGLDisplay, mEGLContext, EGL14.EGL_CONTEXT_CLIENT_VERSION, values, 0);
        Log.d(TAG, "EGLContext created, client version " + values[0]);
    }

    /**
     * Finds a suitable EGLConfig.
     * @param flags     Bit flags from constructor.
     * @param version   Must be 2 or 3.
     */
    private EGLConfig getConfig(int flags, int version) {
        int renderableType = EGL14.EGL_OPENGL_ES2_BIT;
        if (version >= 3) {
            renderableType |= EGLExt.EGL_OPENGL_ES3_BIT_KHR;
        }

        // The actual surface is generally RGBA or RGBX, so situationally omitting alpha
        // doesn't really help.  It can also lead to a huge performance hit on glReadPixels()
        // when reading into a GL_RGBA buffer.
        // If an attribute is not specified in attrib_list, then the default value is used.
        int[] attribList = {
                // bits of Red in the color buffer
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                //EGL14.EGL_DEPTH_SIZE, 16,
                //EGL14.EGL_STENCIL_SIZE, 8,
                // which client APIs are supported
                EGL14.EGL_RENDERABLE_TYPE, renderableType,
                EGL14.EGL_NONE, 0,      // placeholer for recordable [@-3]
                // The list is terminated with EGL_NONE.
                EGL14.EGL_NONE
        };
        if ((flags & FLAG_RECORDABLE) != 0) {
            // 替换　EGL14.EGL_NONE, 0
            // EGL的拓展
            attribList[attribList.length - 3] = EGL_RECORDABLE_ANDROID;
            attribList[attribList.length - 2] = 1;
        }
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        // 和eglGetConfigs类似
        if (!EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, configs, 0, configs.length,
                numConfigs, 0)) {

        }
        // Only configurations matching attrib_list will be returned.
        // 查找匹配的配置
        return configs[0];
    }

    public EGLSurface createWindowSurface(Object surface) {
        if (!(surface instanceof Surface) && !(surface instanceof SurfaceTexture)) {
            throw new RuntimeException("invalid surface: " + surface);
        }

        // Create a window surface, and attach it to the Surface we received.
        int[] surfaceAttribs = {
                EGL14.EGL_NONE
        };
        EGLSurface eglSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, mEGLConfig, surface,
                surfaceAttribs, 0);
        checkEglError("eglCreateWindowSurface");
        if (eglSurface == null) {
            throw new RuntimeException("surface was null");
        }

        return eglSurface;
    }

    /**
     * Creates an EGL surface associated with an offscreen buffer.
     * 1. Pbuffers are typically allocated in offscreen (non-visible) graphics memory
     * and are intended only for accelerated offscreen rendering.
     * 2. Pbuffers are EGL resources and have no associated native window or
     * native window type.
     */
    public EGLSurface createOffscreenSurface(int width, int height) {
        int[] surfaceAttribs = {
                EGL14.EGL_WIDTH, width,
                EGL14.EGL_HEIGHT, height,
                EGL14.EGL_NONE
        };
        EGLSurface eglSurface = EGL14.eglCreatePbufferSurface(mEGLDisplay, mEGLConfig,
                surfaceAttribs, 0);
        checkEglError("eglCreatePbufferSurface");
        if (eglSurface == null) {
            throw new RuntimeException("surface was null");
        }
        return eglSurface;
    }

    /**
     * Make our EGL context current, using the supplied surface for both "draw" and "read".
     * Note that the same EGLSurface may be specified for both draw and read.
     */
    public void makeCurrent(EGLSurface eglSurface) {
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            // called makeCurrent() before create?
            Log.d(TAG, "NOTE: makeCurrent w/o display");
        }
        if (!EGL14.eglMakeCurrent(mEGLDisplay, eglSurface, eglSurface, mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    /**
     * Makes our EGL context current, using the supplied "draw" and "read" surfaces.
     */
    public void makeCurrent(EGLSurface drawSurface, EGLSurface readSurface) {
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            // called makeCurrent() before create?
            Log.d(TAG, "NOTE: makeCurrent w/o display");
        }
        if (!EGL14.eglMakeCurrent(mEGLDisplay, drawSurface, readSurface, mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent(draw,read) failed");
        }
    }

    /**
     * Calls eglSwapBuffers.  Use this to "publish" the current frame.
     *
     * @return false on failure
     */
    public boolean swapBuffers(EGLSurface eglSurface) {
        return EGL14.eglSwapBuffers(mEGLDisplay, eglSurface);
    }

    /**
     * Sends the presentation time stamp to EGL.  Time is expressed in nanoseconds.
     */
    public void setPresentationTime(EGLSurface eglSurface, long nsecs) {
        EGLExt.eglPresentationTimeANDROID(mEGLDisplay, eglSurface, nsecs);
    }

    /**
     * Discards all resources held by this class, notably the EGL context.  This must be
     * called from the thread where the context was created.
     *
     * On completion, no context will be current.
     */
    public void release() {
        if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            // Android is unusual in that it uses a reference-counted EGLDisplay.  So for
            // every eglInitialize() we need an eglTerminate().
            EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE,
                    EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
            // Return EGL to its state at thread initialization.
            // eglReleaseThread may be called in any thread at any time, and may be called
            // more than once in a single thread.
            EGL14.eglReleaseThread();
            // Termination marks all EGL-specific resources, such as contexts and surfaces,
            // associated with the specified display for deletion.
            EGL14.eglTerminate(mEGLDisplay);
        }

        mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        mEGLContext = EGL14.EGL_NO_CONTEXT;
        mEGLConfig = null;
    }

    /**
     * An EGLSurface of any type (window, pbuffer, or pixmap) is destroyed.
     * Destroys the specified surface. Note the EGLSurface won't actually be destroyed if
     * it's still current in a context.
     */
    public void releaseSurface(EGLSurface eglSurface) {
        EGL14.eglDestroySurface(mEGLDisplay, eglSurface);
    }

    // Performs a simple surface query.
    public int querySurface(EGLSurface eglSurface, int what) {
        int[] value = new int[1];
        EGL14.eglQuerySurface(mEGLDisplay, eglSurface, what, value, 0);
        return value[0];
    }

    /**
     * Queries a string value.
     * returns in value the value of attribute for surface.
     */
    public String queryString(int what) {
        return EGL14.eglQueryString(mEGLDisplay, what);
    }

    /**
     * Checks for EGL errors. Throws an exception if an error has been raised.
     */
    private void checkEglError(String msg) {
        int error;
        if ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
            throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
        }
    }

    // Makes no context current.
    public void makeNothingCurrent() {
        if (!EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public int getGlVersion() {
        return mGlVersion;
    }
}

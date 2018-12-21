package com.android.mm.mediacts;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.TextureView;
import android.view.View;

import java.util.concurrent.CountDownLatch;

public class TextureViewCtsActivity extends Activity implements TextureView.SurfaceTextureListener {

    private final static long TIME_OUT_MS = 10000;
    private final Object mLock = new Object();

    private View mPreview;
    private TextureView mTextureView;
    private HandlerThread mGLThreadLooper;
    private Handler mGLThread;
    private CountDownLatch mEnterAnimationFence = new CountDownLatch(1);

    static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    static final int EGL_OPENGL_ES3_BIT = 4;
    static final int EGL_GL_COLORSPACE_KHR = 0x309D;
    static final int EGL_COLOR_COMPONENT_TYPE_EXT = 0x3339;
    static final int EGL_COLOR_COMPONENT_TYPE_FLOAT_EXT = 0x333B;

    private EGL14 mEgl;
    private EGLDisplay mEglDisplay;
    private EGLConfig mEglConfig;
    private EGLContext mEglContext;
    private EGLSurface mEglSurface;

    private SurfaceTexture mSurface;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private int mSurfaceUpdatedCount;

    private int mEglColorSpace = 0;
    private boolean mIsEGLWideGamut = false;
    private boolean mEGLExtensionUnsupported = false;

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}

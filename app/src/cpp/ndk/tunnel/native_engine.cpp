//
// Created by wangrl on 18-12-23.
//
#include "common.hpp"
#include "native_engine.hpp"

// max # of GL errors to print before giving up
#define MAX_GL_ERRORS 200

static NativeEngine* _singleton = NULL;

NativeEngine::NativeEngine(struct android_app *app) {
    LOGD("NativeEngine: initializing.");

    mApp = app;
    mHasFocus = mIsVisible = mHasFocus = false;
    mHasGLObjects = false;
    mEglDisplay = EGL_NO_DISPLAY;
    mEglSurface = EGL_NO_SURFACE;
    mEglContext = EGL_NO_CONTEXT;
    mEglConfig = 0;
    mSurfWidth = mSurfHeight = 0;
    mApiVersion = 0;
    mJniEnv = NULL;
    memset(&mState, 0, sizeof(mState));
    mIsFirstFrame = true;

    if (app->savedState != NULL) {
        // we are starting with previous saved state -- restore it
        mState = *(struct NativeEngineSavedState*) app->savedState;
    }

    // only one instance of NativeEngine may exist!
    MY_ASSERT(_singleton == NULL);
    _singleton = this;

    LOGD("NativeEngine: querying API level.");
    LOGD("NativeEngine: API version %d.", mApiVersion);
}

NativeEngine *NativeEngine::GetInstance() {
    MY_ASSERT(_singleton != NULL);
    return _singleton;
}

NativeEngine::~NativeEngine() {
    LOGD("NativeEngine: destructor running");
    KillContext();
    if (mJniEnv) {
        LOGD("Detaching current thread from JNI.");
        mApp->activity->vm->DetachCurrentThread();
        LOGD("Current thread detached from JNI.");
        mJniEnv = NULL;
    }
    _singleton = NULL;
}





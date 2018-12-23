#include <android/log.h>
#include <android_native_app_glue.h>
#include <condition_variable>
#include <dlfcn.h>
#include <EGL/egl.h>
#include <thread>
#include "TeapotRenderer.h"

// Indicate API mode to achieve 30 FPS.
enum APIMode {
    kAPINone,
    kAPINativeChoreographer,
    kAPIJavaChoreographer,
    kAPIEGLExtension,
};

// Declaration for native choreographer API.
struct AChoreographer;

//----------------------------------
// Shared state for our app.
//----------------------------------
struct android_app;

class Engine {
    android_app* app_;

    TeapotRenderer renderer_;


};

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_TeapotActivity_choreographerCallback(JNIEnv *env, jobject instance,
                                                             jlong frameTimeNanos) {

    // TODO

}

void android_main(android_app* state) {
    int id;
    int events;
    android_poll_source* source;

    while ((id = ALooper_pollAll(100, NULL, &events, (void**)&source)) >= 0) {

        // Process this event.
        if (source != NULL)
            source->process(state, source);

        // Check if we are exiting.
        if (state->destroyRequested != 0) {
            return;
        }
    }
}


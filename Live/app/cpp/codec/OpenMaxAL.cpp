#include <assert.h>
#include <jni.h>
#include <assert.h>
#include <jni.h>
#include <pthread.h>
#include <stdio.h>
#include <string.h>

// for __android_log_print(ANDROID_LOG_INFO, "YourApp", "formatted message");
#include <android/log.h>
#define TAG "OpenMaxAL"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)

// for native media
#include <OMXAL/OpenMAXAL.h>
#include <OMXAL/OpenMAXAL_Android.h>

// for native window JNI
#include <android/native_window_jni.h>
#include <android/asset_manager_jni.h>
#include "FileOpenAsset.h"

// engine interfaces
static XAObjectItf engineObject = NULL;
static XAEngineItf engineEngine = NULL;

// output mix interfaces
static XAObjectItf outputMixObject = NULL;

// streaming media player interfaces
static XAObjectItf playerObj = NULL;
static XAPlayItf playerPlayerItf = NULL;
// android平台自定义
static XAAndroidBufferQueueItf playerBQItf = NULL;
static XAStreamInformationItf playerStreamInfoItf = NULL;
static XAVolumeItf playerVolItf = NULL;

// 媒体文件结构体指针
static FILE* file;
static jobject android_java_asset_manager = NULL;

// number of buffers in our buffer queue, an arbitrary number
#define NB_BUFFERS 8

// video sink for the player
static ANativeWindow* theNativeWindow;

extern "C"
JNIEXPORT void JNICALL
Java_com_android_live_codec_OpenMaxALActivity_createEngine(JNIEnv *env, jclass type) {

    XAresult res;

    // create engine
    res = xaCreateEngine(&engineObject, 0, NULL, 0, NULL, NULL);
    assert(XA_RESULT_SUCCESS == res);

    // realize the engine
    res = (*engineObject)->Realize(engineObject, XA_BOOLEAN_FALSE);
    assert(XA_RESULT_SUCCESS == res);

    // get the engine interface, which is needed in order to create other objects
    res = (*engineObject)->GetInterface(engineObject, XA_IID_ENGINE, &engineEngine);
    assert(XA_RESULT_SUCCESS == res);

    // create output mix
    res = (*engineEngine)->CreateOutputMix(engineEngine, &outputMixObject, 0, NULL, NULL);
    assert(XA_RESULT_SUCCESS == res);

    // realize the output mix
    res = (*outputMixObject)->Realize(outputMixObject, XA_BOOLEAN_FALSE);
    assert(XA_RESULT_SUCCESS == res);

    LOGV("native engine created.");
}

// 记住传入值和返回值很容易引起crash.

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_live_codec_OpenMaxALActivity_createStreamingMediaPlayer(JNIEnv *env, jclass type,
                                                                         jobject assetManager,
                                                                         jstring filename_) {
    // JNI DETECTED ERROR IN APPLICATION: GetStringUTFChars received NULL jstring.
    const char *filename = env->GetStringUTFChars(filename_, 0);
    // TODO
    android_java_asset_manager = (env)->NewGlobalRef(assetManager);
    android_fopen_set_asset_manager(AAssetManager_fromJava(env, android_java_asset_manager));

    // open the file to play
    file = android_fopen(filename, "rb");
    if (file == NULL)
        return JNI_FALSE;

    // configure data source
    // 配置Buffer的大小。
    XADataLocator_AndroidBufferQueue loc_abq = { XA_DATALOCATOR_ANDROIDBUFFERQUEUE, NB_BUFFERS };
    // 指定解码格式为video/mp2ts，指定容器类型为MPEG_TS.
    XADataFormat_MIME format_mime = {
            XA_DATAFORMAT_MIME, XA_ANDROID_MIME_MP2TS, XA_CONTAINERTYPE_MPEG_TS
    };
    XADataSource dataSrc = {&loc_abq, &format_mime};

    // configure audio sink
    XADataLocator_OutputMix loc_outmix = {XA_DATALOCATOR_OUTPUTMIX, outputMixObject};
    XADataSink audioSnk = {&loc_outmix, NULL};

    // configure image video sink
    XADataLocator_NativeDisplay loc_nd = {
            XA_DATALOCATOR_NATIVEDISPLAY,        // locatorType
            // the video sink must be an ANativeWindow created from a Surface or SurfaceTexture
            (void*)theNativeWindow,              // hWindow
            // must be NULL
            NULL                                 // hDisplay
    };

    env->ReleaseStringUTFChars(filename_, filename);

    return JNI_TRUE;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_android_live_codec_OpenMaxALActivity_setPlayingStreamMediaPlayer(JNIEnv *env, jclass type,
                                                                          jboolean isPlaying) {

    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_live_codec_OpenMaxALActivity_shutdown(JNIEnv *env, jclass type) {

    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_live_codec_OpenMaxALActivity_setSurface(JNIEnv *env, jclass type,
                                                         jobject surface) {

    // TODO
    // obtain a native window from a Java surface
    theNativeWindow = ANativeWindow_fromSurface(env, surface);
    LOGV("native window created.");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_live_codec_OpenMaxALActivity_rewindStreamingMediaPlayer(JNIEnv *env, jclass type) {

    // TODO

}
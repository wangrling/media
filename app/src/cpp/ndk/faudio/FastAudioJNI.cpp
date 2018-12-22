#include <jni.h>
#include <SLES/OpenSLES_Android.h>
#include <sys/types.h>
#include <cassert>
#include <cstring>


// 创建SL引擎
extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_FastAudioActivity_createSLEngine(JNIEnv *env, jclass type, jint rate,
                                                         jint framesPerBuf, jlong delayInMs,
                                                         jfloat decay) {

    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_FastAudioActivity_deleteSLEngine(JNIEnv *env, jclass type) {

    // TODO

}

// 回环配置，传递延迟时间和衰减率。
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mm_ndk_FastAudioActivity_configureEcho(JNIEnv *env, jclass type, jint delayInMs,
                                                        jfloat decay) {

    // TODO
    return JNI_TRUE;
}

// 创建播放器
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mm_ndk_FastAudioActivity_createSLBufferQueueAudioPlayer(JNIEnv *env, jclass type) {

    // TODO
    return JNI_TRUE;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_FastAudioActivity_deleteSLBufferQueueAudioPlayer(JNIEnv *env, jclass type) {

    // TODO

}

// 创建录音
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mm_ndk_FastAudioActivity_createAudioRecorder(JNIEnv *env, jclass type) {

    // TODO
    return JNI_TRUE;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_FastAudioActivity_deleteAudioRecorder(JNIEnv *env, jclass type) {

    // TODO

}

// 回环测试
extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_FastAudioActivity_startPlay(JNIEnv *env, jclass type) {

    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_FastAudioActivity_stopPlay(JNIEnv *env, jclass type) {

    // TODO

}
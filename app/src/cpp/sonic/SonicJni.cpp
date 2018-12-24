//
// Created by wang on 18-12-21.
//

#pragma once

#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include "Sonic.h"

#define LOG_TAG "Sonic"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)

struct sonicInstStruct {
    sonicStream stream;
    short *byteBuf;
    int byteBufSize;
};

typedef struct sonicInstStruct* sonicInst;

#define getInst(sonicID) ((sonicInst)((char *)NULL + (sonicID)))

extern "C"
JNIEXPORT jlong JNICALL
Java_com_android_mm_sonic_Sonic_initNative(JNIEnv *env, jobject instance, jint sampleRate,
                                           jint channels) {

    // TODO
    sonicInst inst = (sonicInst)calloc(1, sizeof(struct sonicInstStruct));

    if(inst == NULL) {
        return 0;
    }
    LOGV("Creating sonic stream");
    inst->stream = sonicCreateStream(sampleRate, channels);
    if(inst->stream == NULL) {
        return 0;
    }
    inst->byteBufSize = 100;
    inst->byteBuf = (short *)calloc(inst->byteBufSize, sizeof(short));
    if(inst->byteBuf == NULL) {
        return 0;
    }
    return (jlong)((char *)inst - (char *)NULL);
}

// Teardown the C data structure.
extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_sonic_Sonic_closeNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicInst inst = getInst(sonicID);
    sonicStream stream = inst->stream;

    LOGV("Destroying stream");
    sonicDestroyStream(stream);
    free(inst->byteBuf);
    free(inst);
}

// Process any samples still in a sonic buffer.
extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_sonic_Sonic_flushNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Flushing stream");
    sonicFlushStream(stream);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_sonic_Sonic_setSampleRateNative(JNIEnv *env, jobject instance, jlong sonicID,
                                                    jint newSampleRate) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Set sample rate to %d", newSampleRate);
    sonicSetSampleRate(stream, newSampleRate);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_android_mm_sonic_Sonic_getSampleRateNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Reading sample rate");
    return sonicGetSampleRate(stream);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_sonic_Sonic_setNumChannelsNative(JNIEnv *env, jobject instance, jlong sonicID,
                                                     jint newNumChannels) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Set sample rate to %d", newNumChannels);
    sonicSetNumChannels(stream, newNumChannels);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_android_mm_sonic_Sonic_getNumChannelsNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Reading num channels");
    return sonicGetNumChannels(stream);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_sonic_Sonic_setPitchNative(JNIEnv *env, jobject instance, jlong sonicID,
                                               jfloat newPitch) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Set pitch to %f", newPitch);
    sonicSetPitch(stream, newPitch);
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_android_mm_sonic_Sonic_getPitchNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Reading pitch");
    return sonicGetPitch(stream);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_sonic_Sonic_setSpeedNative(JNIEnv *env, jobject instance, jlong sonicID,
                                               jfloat newSpeed) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Set speed to %f", newSpeed);
    sonicSetSpeed(stream, newSpeed);
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_android_mm_sonic_Sonic_getSpeedNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Reading speed");
    return sonicGetSpeed(stream);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_sonic_Sonic_setRateNative(JNIEnv *env, jobject instance, jlong sonicID,
                                              jfloat newRate) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Set rate to %f", newRate);
    sonicSetRate(stream, newRate);
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_android_mm_sonic_Sonic_getRateNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Reading rate");
    return sonicGetRate(stream);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_sonic_Sonic_setChordPitchNative(JNIEnv *env, jobject instance, jlong sonicID,
                                                    jboolean useChordPitch) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Set chord pitch to %d", useChordPitch);
    sonicSetChordPitch(stream, useChordPitch);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mm_sonic_Sonic_getChordPitchNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Reading chord pitch");
    return sonicGetChordPitch(stream);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mm_sonic_Sonic_putBytesNative(JNIEnv *env, jobject instance, jlong sonicID,
                                               jbyteArray buffer_, jint lenBytes) {
    // jbyte *buffer = env->GetByteArrayElements(buffer_, NULL);

    // TODO
    sonicInst inst = getInst(sonicID);
    sonicStream stream = inst->stream;
    int samples = lenBytes/(sizeof(short)*sonicGetNumChannels(stream));
    int remainingBytes = lenBytes - samples*sizeof(short)*sonicGetNumChannels(stream);

    // TODO: deal with case where remainingBytes is not 0.
    if(remainingBytes != 0) {
        LOGV("Remaining bytes == %d!!!", remainingBytes);
    }
    if(lenBytes > inst->byteBufSize*sizeof(short)) {
        inst->byteBufSize = lenBytes*(2/sizeof(short));
        inst->byteBuf = (short *)realloc(inst->byteBuf, inst->byteBufSize*sizeof(short));
        if(inst->byteBuf == NULL) {
            return 0;
        }
    }
    LOGV("Writing %d bytes to stream", lenBytes);
    (env)->GetByteArrayRegion(buffer_, 0, lenBytes, (jbyte *)inst->byteBuf);

    return sonicWriteShortToStream(stream, inst->byteBuf, samples);


    // env->ReleaseByteArrayElements(buffer_, buffer, 0);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_android_mm_sonic_Sonic_receiveBytesNative(JNIEnv *env, jobject instance, jlong sonicID,
                                                   jbyteArray ret_, jint lenBytes) {
    // jbyte *ret = env->GetByteArrayElements(ret_, NULL);

    // TODO
    sonicInst inst = getInst(sonicID);
    sonicStream stream = inst->stream;
    int available = sonicSamplesAvailable(stream)*sizeof(short)*sonicGetNumChannels(stream);
    int samplesRead, bytesRead;

    LOGV("Reading %d bytes from stream", lenBytes);
    if(lenBytes > available) {
        lenBytes = available;
    }
    if(lenBytes > inst->byteBufSize*sizeof(short)) {
        inst->byteBufSize = lenBytes*(2/sizeof(short));
        inst->byteBuf = (short *)realloc(inst->byteBuf, inst->byteBufSize*sizeof(short));
        if(inst->byteBuf == NULL) {
            return -1;
        }
    }
    //LOGV("Doing read %d", lenBytes);
    samplesRead = sonicReadShortFromStream(stream, inst->byteBuf,
                                           lenBytes/(sizeof(short)*sonicGetNumChannels(stream)));
    bytesRead = samplesRead*sizeof(short)*sonicGetNumChannels(stream);
    //LOGV("Returning %d", samplesRead);
    (env)->SetByteArrayRegion(ret_, 0, bytesRead, (jbyte *)inst->byteBuf);
    return bytesRead;

    // env->ReleaseByteArrayElements(ret_, ret, 0);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_android_mm_sonic_Sonic_availableBytesNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Reading samples available = %d", sonicSamplesAvailable(stream)*sizeof(short)*sonicGetNumChannels(stream));

    return sonicSamplesAvailable(stream)*sizeof(short)*sonicGetNumChannels(stream);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_sonic_Sonic_setVolumeNative(JNIEnv *env, jobject instance, jlong sonicID,
                                                jfloat newVolume) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Set volume to %f", newVolume);
    sonicSetVolume(stream, newVolume);
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_android_mm_sonic_Sonic_getVolumeNative(JNIEnv *env, jobject instance, jlong sonicID) {

    // TODO
    sonicStream stream = getInst(sonicID)->stream;
    LOGV("Reading volume");
    return sonicGetVolume(stream);
}


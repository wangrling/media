//
// Created by wang on 18-12-19.
//

#include <assert.h>
#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <limits.h>


#include <media/NdkMediaCodec.h>
#include <media/NdkMediaExtractor.h>

// for __android_log_print(ANDROID_LOG_INFO, "YourApp", "formatted message");
#include <android/log.h>
#define TAG "NativeCodec"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

// for native window JNI
#include <android/native_window_jni.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <time.h>
#include "Looper.h"

typedef struct {
    int fd;
    // 播放窗口
    ANativeWindow* window;
    AMediaExtractor* ex;
    AMediaCodec* codec;
    int64_t  renderStart;
    bool sawInputEOS;
    bool sawOutputEOS;
    bool isPlaying;
    bool renderOnce;
} WorkerData;

WorkerData data = {-1, NULL, NULL, NULL, 0, false, false, false, false};

// 播放状态
enum {
    kMsgCodecBuffer,
    kMsgPause,
    kMsgResume,
    kMsgPauseAck,
    kMsgDecodeDone,
    kMsgSeek
};

class MyLooper : public Looper {
    virtual void handle(int what, void* obj);

    void doCodecWork(WorkerData *ptr);
};

static MyLooper* mLooper = NULL;

void MyLooper::handle(int what, void *obj) {
    switch (what) {
        case kMsgCodecBuffer:
            doCodecWork((WorkerData*)obj);
            break;
        case kMsgDecodeDone: {
            WorkerData* d = (WorkerData*) obj;
            AMediaCodec_stop(d->codec);
            AMediaCodec_delete(d->codec);
            AMediaExtractor_delete(d->ex);
            d->sawInputEOS = true;
            d->sawOutputEOS = true;
            break;
        }
        case kMsgSeek: {
            WorkerData* d = (WorkerData*) obj;
            AMediaExtractor_seekTo(d->ex, 0, AMEDIAEXTRACTOR_SEEK_NEXT_SYNC);
            AMediaCodec_flush(d->codec);
            d->renderStart = -1;
            d->sawInputEOS = false;
            d->sawOutputEOS = false;
            if (!d->isPlaying) {
                d->renderOnce = true;
                post(kMsgCodecBuffer, d);
            }
            LOGV("seeked");
            break;
        }
        case kMsgPause: {
            WorkerData* d = (WorkerData*) obj;
            if (d->isPlaying) {
                // flush all outstanding codecbuffer messages with a no-op message
                d->isPlaying = false;
                post(kMsgPauseAck, NULL, true);
            }
            break;
        }
        case kMsgResume: {
            WorkerData* d = (WorkerData*) obj;
            if (!d->isPlaying) {
                d->renderStart = -1;
                d->isPlaying = true;
                post(kMsgCodecBuffer, d);
            }
        }
        break;
    }
}

int64_t systemNanoTime() {
    timespec now;
    clock_gettime(CLOCK_MONOTONIC, &now);

    return now.tv_sec * 1000000000LL + now.tv_nsec;
}

void MyLooper::doCodecWork(WorkerData *d) {
    ssize_t bufIdx = -1;
    if (!d->sawInputEOS) {
        bufIdx = AMediaCodec_dequeueInputBuffer(d->codec, 2000);
        LOGV("input buffer %zd", bufIdx);
        if (bufIdx >= 0) {
            size_t bufSize;
            auto buf = AMediaCodec_getInputBuffer(d->codec, bufIdx, &bufSize);
            // 将数据读到Buffer中。
            auto sampleSize = AMediaExtractor_readSampleData(d->ex, buf, bufSize);
            if (sampleSize < 0) {
                sampleSize = 0;
                d->sawInputEOS = true;
                LOGV("EOS");
            }
            auto presentationTimeUs = AMediaExtractor_getSampleTime(d->ex);

            AMediaCodec_queueInputBuffer(d->codec, bufIdx, 0, sampleSize, presentationTimeUs,
            d->sawInputEOS ? AMEDIACODEC_BUFFER_FLAG_END_OF_STREAM : 0);
            AMediaExtractor_advance(d->ex);
        }
    }

    if (!d->sawOutputEOS) {
        AMediaCodecBufferInfo info;
        auto status = AMediaCodec_dequeueOutputBuffer(d->codec, &info, 0);
        if (status >= 0) {
            if (info.flags & AMEDIACODEC_BUFFER_FLAG_END_OF_STREAM) {
                LOGV("output EOS");
                d->sawOutputEOS = true;
            }
            int64_t presentationNano = info.presentationTimeUs * 1000;
            if (d->renderStart < 0) {
                d->renderStart = systemNanoTime() - presentationNano;
            }
            int64_t delay = (d->renderStart + presentationNano) - systemNanoTime();
            if (delay > 0) {
                usleep(delay / 1000);
            }
            AMediaCodec_releaseOutputBuffer(d->codec, status, info.size != 0);
            if (d->renderOnce) {
                d->renderOnce = false;
                return ;
            }
        } else if (status == AMEDIACODEC_INFO_OUTPUT_BUFFERS_CHANGED) {
            LOGV("output buffers changed");
        } else if (status == AMEDIACODEC_INFO_OUTPUT_FORMAT_CHANGED) {
            auto format = AMediaCodec_getOutputFormat(d->codec);
            LOGV("format changed to: %s", AMediaFormat_toString(format));
            AMediaFormat_delete(format);
        } else if (status == AMEDIACODEC_INFO_TRY_AGAIN_LATER) {
            LOGV("no output buffer right now");
        } else {
            LOGV("unexpected info code: %zd", status);
        }
    }
    if (!d->sawInputEOS || !d->sawOutputEOS) {
        // 继续解码
        mLooper->post(kMsgCodecBuffer, d);
    }
}


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mm_ndk_NativeCodecActivity_createStreamingMediaPlayer(JNIEnv *env, jclass type,
                                                                       jobject assetMgr,
                                                                       jstring filename_) {
    const char *filename = env->GetStringUTFChars(filename_, 0);
    LOGV("opening %s", filename);
    // TODO

    off_t outStart, outLen;
    int fd = AAsset_openFileDescriptor(AAssetManager_open(AAssetManager_fromJava(env, assetMgr), filename, 0),
            &outStart, &outLen);
    env->ReleaseStringUTFChars(filename_, filename);

    if (fd < 0) {
        LOGE("failed to open file: %s %d (%s)", filename, fd, strerror(errno));
        return JNI_FALSE;
    }

    data.fd = fd;

    WorkerData* d = &data;

    AMediaExtractor* ex = AMediaExtractor_new();
    // 解码器获取开始的位置和片源长度。
    media_status_t err = AMediaExtractor_setDataSourceFd(ex, d->fd, static_cast<off64_t >(outStart),
                                                         static_cast<off64_t >(outLen));

    close(d->fd);
    if (err != AMEDIA_OK) {
        LOGV("setDataSource error: %d", err);
        return JNI_FALSE;
    }

    int numTracks = AMediaExtractor_getTrackCount(ex);

    AMediaCodec* codec = NULL;

    LOGV("input has %d tracks", numTracks);
    for (int i = 0; i < numTracks; i++) {
        // 获取媒体格式
        AMediaFormat* format = AMediaExtractor_getTrackFormat(ex, i);
        const char* s = AMediaFormat_toString(format);
        LOGV("track %d format: %s", i, s);
        const char *mime;
        // 根据格式获取类型
        if (!AMediaFormat_getString(format, AMEDIAFORMAT_KEY_MIME, &mime)) {
            LOGV("no mime type");
            return JNI_FALSE;
        } else if (!strncmp(mime, "video/", 6)) {
            // Omitting most error handling for clarity.
            // Production code should check for errors.
            // 只播放视频
            AMediaExtractor_selectTrack(ex, i);
            codec = AMediaCodec_createDecoderByType(mime);
            AMediaCodec_configure(codec, format, d->window, NULL, 0);
            d->ex = ex;
            d->codec = codec;
            d->renderStart = -1;
            d->sawInputEOS = false;
            d->sawOutputEOS = false;
            d->isPlaying = false;
            d->renderOnce = true;

            AMediaCodec_start(codec);
        }
        AMediaFormat_delete(format);
    }

    mLooper = new MyLooper();
    // 传递解码信息
    mLooper->post(kMsgCodecBuffer, d);

    return JNI_TRUE;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_NativeCodecActivity_setPlayingStreamingMediaPlayer(JNIEnv *env, jclass type,
                                                                           jboolean isPlaying) {

    // TODO
    LOGV("play/pause: %d", isPlaying);
    if (mLooper) {
        if (isPlaying) {
            mLooper->post(kMsgResume, &data);
        } else {
            mLooper->post(kMsgPause, &data);
        }
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_NativeCodecActivity_shutdown(JNIEnv *env, jclass type) {

    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_NativeCodecActivity_setSurface(JNIEnv *env, jclass type, jobject surface) {

    // TODO
    // obtain a native window from a Java surface
    if (data.window) {
        ANativeWindow_release(data.window);
        data.window = NULL;
    }

    data.window = ANativeWindow_fromSurface(env, surface);
    LOGV("setSurface %p", data.window);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_NativeCodecActivity_rewindStreamingMediaPlayer(JNIEnv *env, jclass type) {

    // TODO
    LOGV("rewind");
    if (mLooper) {
        mLooper->post(kMsgSeek, &data);
    }
}
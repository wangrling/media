//
// Created by wang on 18-12-19.
//

#include "Looper.h"

#include <assert.h>
#include <jni.h>
#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <limits.h>
#include <semaphore.h>

// for __android_log_print(ANDROID_LOG_INFO, "YourApp", "formatted message");
#include <android/log.h>
#define TAG "NativeCodecLooper"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)

struct LooperMessage;
typedef struct LooperMessage LooperMessage;

// 链表
struct LooperMessage {
    int what;
    void *obj;
    LooperMessage *next;
    bool quit;
};

Looper::Looper() {
    sem_init(&headDataAvailable, 0, 0);
    sem_init(&headWriteProtect, 0, 1);
    pthread_attr_t attr;
    pthread_attr_init(&attr);

    pthread_create(&worker, &attr, trampoline, this);
    running = true;
}

Looper::~Looper() {
    if (running) {
        LOGV("Looper deleted while still running. Some messages will not be processed");
        quit();
    }
}

void Looper::post(int what, void *data, bool flush) {
    LooperMessage* msg = new LooperMessage();
    msg->what = what;
    msg->obj = data;
    msg->next = NULL;
    msg->quit = false;
    addMsg(msg, flush);
}

void Looper::addMsg(LooperMessage *msg, bool flush) {
    sem_wait(&headWriteProtect);
    LooperMessage* h = head;

    if (flush) {
        while (h) {
            LooperMessage* next = h->next;
            delete h;
            h = next;
        }
        h = NULL;
    }
    if (h) {
        while (h->next) {
            h = h->next;
        }
        h->next = msg;
    } else {
        head = msg;
    }
    LOGV("post msg %d", msg->what);
    sem_post(&headWriteProtect);
    sem_post(&headDataAvailable);
}

void Looper::loop() {
    while (true) {
        // wait for available message
        sem_wait(&headDataAvailable);

        // get next available message.
        sem_wait(&headWriteProtect);
        LooperMessage* msg = head;
        if (msg == NULL) {
            LOGV("no msg");
            sem_post(&headWriteProtect);
            continue;
        }
        head = msg->next;
        sem_post(&headWriteProtect);
        if (msg->quit) {
            LOGV("quitting");
            delete msg;
            return;
        }
        LOGV("processing msg %d", msg->what);
        handle(msg->what, msg->obj);
        delete msg;
    }
}

void Looper::quit() {
    LOGV("quit");
    LooperMessage* msg = new LooperMessage();
    msg->what = 0;
    msg->obj = NULL;
    msg->next = NULL;
    msg->quit = true;
    addMsg(msg, false);

    void *retval;
    pthread_join(worker, &retval);
    sem_destroy(&headDataAvailable);
    sem_destroy(&headWriteProtect);
    running = false;
}

void Looper::handle(int what, void *data) {
    LOGV("dropping msg %d %p", what, data);
}

void *Looper::trampoline(void *p) {
    ((Looper*)p)->loop();
    return NULL;
}





//
// Created by wang on 18-12-19.
//
#pragma once

#include <sys/types.h>
#include <semaphore.h>

struct LooperMessage;

class Looper {
public:
    Looper();
    Looper& operator=(const Looper&) = delete;
    Looper(Looper&) = delete;

    virtual ~Looper();

    void post(int what, void* data, bool flush = false);

    void quit();

    virtual void handle(int what, void *data);

private:
    void addMsg(LooperMessage *msg, bool flush);
    static void* trampoline(void* p);
    void loop();
    LooperMessage *head;
    pthread_t worker;
    sem_t headWriteProtect;
    sem_t headDataAvailable;
    bool running;
};

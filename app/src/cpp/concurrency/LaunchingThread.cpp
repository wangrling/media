//
// Created by wang on 18-12-21.
//
// Segmentation fault (core dumped)程序崩溃。

#include <thread>

void doSomething(int& i) {
    ++i;
}

struct func {
    int& i;

    func(int& _i):i(_i) {

    }

    void operator()() {
        for (unsigned j = 0; j < 1000000; ++j) {
            // Potential access to dangling reference.
            doSomething(i);
        }
    }
};

void oops() {
    int someLocalState = 0;
    func mFunc(someLocalState);
    std::thread mThread(mFunc);

    // Don't wait for thread to finish.
    mThread.detach();
}
// New thread might still be running.

int main() {
    oops();
}

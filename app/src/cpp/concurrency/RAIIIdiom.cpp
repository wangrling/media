//
// Created by wang on 18-12-21.
//

#include <thread>

class ThreadGuard {
    std::thread& t;

public:
    explicit ThreadGuard(std::thread& _t) : t(_t) {

    }

    ~ThreadGuard() {
        if (t.joinable()) {
            t.join();
        }
    }

    ThreadGuard(ThreadGuard const&)= delete;
    ThreadGuard&operator=(ThreadGuard const&)= delete;
};

void doSomething(int& i) {
    ++i;
}

struct func {
    int& i;

    func(int& _i) : i(_i) {

    }

    void operator()() {
        for(unsigned j = 0; j < 1000000; ++j) {
            doSomething(i);
        }
    }
};

void doSomethingInCurrentThread() {

}

int main() {
    int someLocalState;
    func mFunc(someLocalState);
    std::thread t(mFunc);

    // 线程卫士
    ThreadGuard g(t);

    doSomethingInCurrentThread();
}
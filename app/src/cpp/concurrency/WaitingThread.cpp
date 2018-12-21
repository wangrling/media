//
// Created by wang on 18-12-21.
//
#include <thread>
#include <iostream>
#include <exception>

void doSomething(int &i) {
    ++i;
}

struct func {
    int& i;
    func(int& _i) : i(_i) {

    }

    void operator()() {
        for (unsigned j = 0; j < 1000000; ++j) {
            doSomething(i);
        }
    }
};

void doSomethingInCurrentThread() {
    std::cout << "Waiting in exceptional circumstances." << std::endl;
}

int main() {
    int someLocalState = 0;
    func mFunc(someLocalState);
    std::thread t(mFunc);
    try {
        doSomethingInCurrentThread();
    } catch (std::exception& e) {
        t.join();
        throw e;
    }
    t.join();
}

//
// Created by wang on 18-12-21.
//
#include <iostream>
#include <thread>

void hello() {
    std::cout<< "Hello Concurrent World\n";
}

int main() {
    std::thread t(hello);
    t.join();
}

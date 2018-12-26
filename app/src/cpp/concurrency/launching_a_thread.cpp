// A function that returns while a thread still has access to 
// local variables.

// 在main_thread執行my_thread.detach()之後，my_thread會仍然執行，
// 兩個線程分離開，
// 但是main_thread退出之後，my_thread不能訪問引用或者指針。

#include <thread>
#include <iostream>

void do_something(int& i) {
    ++i;
}

struct func {
    // 成員變量i
    int& i;
    
    // 構造函數
    func(int& i_) : i(i_) {

    }

    // 重載()操作符
    void operator()() {
        for (unsigned j = 0; j < 100000; ++j) {
            // Potential access to dangling reference.
            do_something(i);
            std::cout << "my_thread" << std::endl;
        }
    }
};

int main() {
    int some_local_state = 0;

    // 構建func的對象
    func my_func(some_local_state);

    std::thread my_thread(my_func);

    // Don't wait for thread to finish.
    my_thread.detach();

    std::cout << "main_thread" << std::endl;
}
// New thread might still be running.
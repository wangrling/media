#include <thread>
#include <iostream>

class thread_guard {
    std::thread& t;

    public:
    explicit thread_guard(std::thread& t_) : t(t_) {
        std::cout << "constructor thread_guard" << std::endl;
    }

    ~thread_guard() {
        // See if the std::thread object is joinable() before calling join().
        if(t.joinable()) {
            t.join();
        }
    }

    // 不予許進行賦值操作
    // The copy constructor and copy-assignment operator are maked =delete to ensure
    // that they're not automatically provided by the compiler.
    thread_guard(thread_guard const&) = delete;
    thread_guard& operator=(thread_guard const& ) = delete;
};

struct func {
    int& i;
    
    func(int& i_) : i(i_) {
        std::cout << "constructor func" << std::endl;
    }

    void operator()() {
        for (unsigned j = 0; j < 100000; j++) {
            ++i;
        }
    }
};

void do_something_in_current_thread() {
    std::cout << "do something in main thread" << std::endl;
}

int main() {
    int some_local_state;

    // 構建func結構體
    func my_func(some_local_state);

    // 開始運行
    std::thread t(my_func);

    // 賦值給thread_guard結構體
    thread_guard g(t);

    do_something_in_current_thread();
}
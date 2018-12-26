// Waiting for a thread to finish.
#include <thread>

struct func {
    int& i;

    func(int& i_) : i(i_) {

    }

    void operator()() {
        for (unsigned j = 0; j < 100000; j++) {
            ++i;
        }
    }
};

void do_something_in_current_thread() {

}

int main() {
    int some_local_state = 0;

    func my_func(some_local_state);
    std::thread t(my_func);

    // Uses a try/catch block to ensure that a thread with access to local state is finished
    // before the function exits, whether the cuntion exits normally or by an exception.
    try {
        do_something_in_current_thread();
    } catch(...) {
        t.join();
        throw;
    }
    t.join();
}
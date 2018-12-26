#include <vector>
#include <thread>
#include <algorithm>
#include <functional>
#include <iostream>

void do_work(unsigned id) {
    std::cout << "do_work " << id << std::endl;
}

int main() {

    std::vector<std::thread> threads;

    for (unsigned i = 0; i < 20; ++i) {
        threads.push_back(std::thread(do_work, i));
    }

    // Call join() on each thread in turn.
    std::for_each(threads.begin(), threads.end(), 
        std::mem_fn(&std::thread::join));
}

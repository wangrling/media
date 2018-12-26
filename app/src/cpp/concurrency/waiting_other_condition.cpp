#include <mutex>
#include <condition_variable>
#include <thread>
#include <queue>
#include <iostream>

// 典型的生產者消費者模型。

struct data_chunk {

};

bool more_data_to_prepare() {
    return true;
}

data_chunk prepare_data() {
    return data_chunk();
}

std::mutex mut;
// a queue that's used to pass the data between the two threads.
std::queue<data_chunk> data_queue;
// condition_variable使用
std::condition_variable data_cond;

void data_prepration_thread() {
    while (more_data_to_prepare()) {
        data_chunk const data = prepare_data();
        // using a std::lock_guard and pushs the data onto the queue.
        std::lock_guard<std::mutex> lk(mut);
        std::cout << "push data to queue" << std::endl;
        data_queue.push(data);
        // calls the notify_one() member function on the std::condition_variable
        // instance to notify the waiting thread (if there is one).
        data_cond.notify_one();
    }
}

void process(data_chunk&) {
    std::cout << "process data chunk" << std::endl;
}

bool is_last_chunk(data_chunk&) {
    return false;
}

/**
 * If the condition isn't satified (the lambda function return false), wait() unlocks
 * the mutex and puts the thread in a blocked or waiting state.
 * When the condition variable is notified by a call to notify_one() from the data-preparation
 * thread, the thread wakes from its slumber (unblocks it), reacuires the lock on the mutex, and
 * checks the condition again, returning from wait() with the mutex still locked if the condition
 * has been satisfied.
 */

void data_processing_thread() {
    while (true) {
        std::unique_lock<std::mutex> lk(mut);
        // 等待隊列有數據。
        // passing in the lock object and a lambda function that expresses 
        // the condition being waited for.
        data_cond.wait(lk, []{return !data_queue.empty();});
        data_chunk data = data_queue.front();
        data_queue.pop();
        lk.unlock();
        process(data);
        if (is_last_chunk(data)) 
            break;
    }
}

int main() {
    std::thread t1(data_prepration_thread);
    std::thread t2(data_processing_thread);

    // 等待線程結束
    t1.join();
    t2.join();
}
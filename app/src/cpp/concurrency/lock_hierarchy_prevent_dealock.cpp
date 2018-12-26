#include <mutex>
#include <iostream>
#include <thread>

class hierarchical_mutex
{
public:
    explicit hierarchical_mutex(unsigned level)
    {}
    
    void lock()
    {}
    void unlock()
    {}
};


hierarchical_mutex high_level_mutex(10000);
hierarchical_mutex low_level_mutex(5000);

int do_low_level_stuff()
{
    std::cout << std::this_thread::get_id() << " unlock low level mutex" << std::endl;
    return 42;
}


int low_level_func()
{
    std::lock_guard<hierarchical_mutex> lk(low_level_mutex);
    std::cout << std::this_thread::get_id() << " lock low level mutex" << std::endl;
    return do_low_level_stuff();
}

void high_level_stuff(int some_param) {}


void high_level_func()
{
    std::lock_guard<hierarchical_mutex> lk(high_level_mutex);
    std::cout << std::this_thread::get_id() << " lock high level mutex " << std::endl;
    high_level_stuff(low_level_func());
    std::cout << std::this_thread::get_id() << " unlock high level mutex" << std::endl;
}

void thread_a()
{
    std::cout << std::this_thread::get_id() << " thread a " << std::endl;
    high_level_func();
}

hierarchical_mutex other_mutex(100);

void do_other_stuff()
{}


void other_stuff()
{
    high_level_func();
    do_other_stuff();
}

void thread_b()
{
    std::lock_guard<hierarchical_mutex> lk(other_mutex);
    std::cout << std::this_thread::get_id() << " thread b " << std::endl;
    other_stuff();
}

int main() {
    std::thread a(thread_a);

    std::thread b(thread_b);
    a.join();
    b.join();
}
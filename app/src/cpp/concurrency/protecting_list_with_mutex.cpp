// Protecting shared data with mutexes

#include <list>
#include <mutex>
#include <algorithm>
#include <iostream>

// There's a single global variable, and it's protected with a corresponding global instance of 
// std::mutex.

std::list<int> some_list;
std::mutex some_mutex;

void add_to_list(int new_value) {
    // The use of std::lock_guard<std::mutex> in add_to_list().
    std::lock_guard<std::mutex> guard(some_mutex);
    std::cout << "add to list" << std::endl;
    some_list.push_back(new_value);
}

bool list_contains(int value_to_find) {
    // The use of std::lock_guard<std::mutex> in list_contains().
    std::lock_guard<std::mutex> guard(some_mutex);
    return std::find(some_list.begin(), some_list.end(), value_to_find) != some_list.end();
}


int main() {
    add_to_list(42);

    std::cout << "contains(1) = " << list_contains(1) << ", contains(42) = "
            << list_contains(42) << std::endl;
}

#include <thread>
#include <iostream>

// 從函數種返回thread類型，並且向函數傳遞可執行的函數。

void some_function() {
    std::cout << "some_function" << std::endl;
}

void some_other_function(int i) {
    std::cout << "some_other_function " << i <<std::endl;
}

// 定義一個f()函數，返回thread類型。
std::thread f() {
    void some_function();

    return std::thread(some_function);
}

std::thread g() {
    void some_other_function(int);
    
    std::thread t(some_other_function, 42);

    return t;
}


int main() {

    std::thread t1 = f();
    t1.join();

    std::thread t2 = g();
    t2.join();
}
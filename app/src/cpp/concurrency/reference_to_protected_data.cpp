#include <iostream>
#include <mutex>

// 不安全的數據操作

// 數據
class some_data {
    int a;
    std::string b;
public:
    // 對數據進行操作
    void do_something() {
        std::cout << "do something " << a << std::endl;
    }    
};

// 
class data_wrapper {
private:
    some_data data;
    std::mutex m;
public:
    template<typename Function>
    void process_data(Function func) {
        std::lock_guard<std::mutex> l(m);
        // 使用類對數據進行封裝。
        func(data);
    }    
};

// 構建對象
data_wrapper x;

some_data* unprotected;


void malicious_function(some_data& protected_data) {
    unprotected = &protected_data;
    std::cout << "malicious function" << std::endl;
}

int main() {
    // Pass in a malicious function.
    x.process_data(malicious_function);
    // Unprotected access to protected data.
    unprotected->do_something();
}
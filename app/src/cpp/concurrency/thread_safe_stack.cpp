#include <exception>
#include <memory>

struct empty_stack: std::exception {
    const char* what() const throw();
};

template<typename T>
class threadsafe_stack {
    public:
    threadsafe_stack();
    threadsafe_stack(const threadsafe_stack &);
    threadsafe_stack& operator=(const threadsafe_stack&) = delete;

    void push(T new_value);

    // return a pointer to the popped item.
    std::shared_ptr<T> pop();

    // pass in a reference
    void pop(T& value);

    bool empty() const;
};

int main() {
    
}
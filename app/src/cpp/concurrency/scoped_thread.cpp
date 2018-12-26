#include <thread>
#include <utility>
#include <iostream>

// scoped_thread裏面的輸出都沒有執行。

class scoped_thread
{
    std::thread t;
public:
    explicit scoped_thread(std::thread t_):
        // 允许从 t 到另一对象的有效率的资源传递。 
        t(std::move(t_))
    {
        std::cout << "constructor scoped_thread" << std::endl;
        if(!t.joinable())
            throw std::logic_error("No thread");
    }
    ~scoped_thread()
    {
        std::cout << "deconstructor scoped_thread" << std::endl;
        t.join();
    }
    scoped_thread(scoped_thread const&)=delete;
    scoped_thread& operator=(scoped_thread const&)=delete;
};

void do_something(int& i)
{
    ++i;
    std::cout << "do_something" << std::endl;
}

struct func
{
    int& i;

    func(int& i_):i(i_){}

    void operator()()
    {
        for(unsigned j=0;j<1000000;++j)
        {
            do_something(i);
        }
    }
};

void do_something_in_current_thread() {
    std::cout << "do something in current thread" << std::endl;
}


int main()
{
    int some_local_state = 1;
    scoped_thread l(std::thread(func(some_local_state)));
        
    do_something_in_current_thread();
}
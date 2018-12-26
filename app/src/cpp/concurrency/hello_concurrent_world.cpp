// A simple Hello, Concurrent World program.

#include <iostream>

// The first difference is the extra #include <thread>.
#include <thread>

// Second, the code for writing the message has been moved to a separate function.
void hello()
{
    std::cout<<"Hello Concurrent World\n";
}

int main()
{
    // The std::thread object named t has the new function hello() as its initial function.
    std::thread t(hello);

    // Causes the calling thread (in main()) to wait for the thread associated with the 
    // std::thread object, in this case, t.
    t.join();
}
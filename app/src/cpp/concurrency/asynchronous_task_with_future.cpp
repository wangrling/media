#include <future>
#include <iostream>

int find_the_answer_to_ltuae()
{
    std::cout << std::this_thread::get_id() << " find the answer" << std::endl;
    return 42;
}

void do_other_stuff(){
    std::cout << std::this_thread::get_id() << " do other stuff" << std::endl;
} 

int main() {
    std::future<int> the_answer = std::async(find_the_answer_to_ltuae);

    do_other_stuff();

    std::cout << std::this_thread::get_id() << " The answer is " << the_answer.get() << std::endl;
}
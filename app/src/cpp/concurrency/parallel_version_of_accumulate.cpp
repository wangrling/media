#include <thread>
#include <numeric>
#include <algorithm>
#include <functional>
#include <vector>
#include <iostream>

template<typename Iterator, typename T>
struct accumulate_block {
    void operator()(Iterator first, Iterator last, T& result) {
        result = std::accumulate(first, last, result);
    }
};

template<typename Iterator, typename T>
T parallel_accumulate(Iterator first ,Iterator last, T init) {

    unsigned long const length = std::distance(first, last);

    // If the input range is empty, just return the initial value init.
    if(!length)
        return init;

    unsigned long const min_per_thread = 25;
    // 最少有一個線程，長度爲100就會有4個線程。
    unsigned long const max_threads = 
        (length + min_per_thread - 1) / min_per_thread;

    unsigned long const hardware_threads = 
        std::thread::hardware_concurrency();
    // 最多hardware個線程，最少1個線程。
    // Don't wnat to run more threads than the hardware can support.
    unsigned long const num_threads = 
        std::min(hardware_threads != 0 ? hardware_threads : 2, max_threads);     

    // 根據線程把長度分成若干塊。
    unsigned long const block_size = length / num_threads;

    // for the intermediate results.
    std::vector<T> results(num_threads);
    std::vector<std::thread> threads(num_threads - 1);

    Iterator block_start = first;
    for (unsigned long i = 0; i < num_threads - 1; i++) {
        Iterator block_end = block_start;
        // Advance the block_end iterator to the end of the current block.
        std::advance(block_end, block_size);
        // Launch a new thread to accumulate the results for this block.
        // 調用accumulate_block方法，傳遞參數開始計算。
        threads[i] = std::thread(
            accumulate_block<Iterator, T>(),
            block_start, block_end, std::ref(results[i]));
        // The start of the next block is the end of this one.    
        block_start = block_end;    
    }        

    // main thread can then process hte final block.
    accumulate_block<Iterator, T>()(block_start, last, results[num_threads-1]);

    // wait for all the threads you spawned with std::for_each.
    std::for_each(threads.begin(), threads.end(), std::mem_fn(&std::thread::join));

    return std::accumulate(results.begin(), results.end(), init);
}

int main() {
    std::vector<int> vi;

    for (int i = 0; i < 10; i++) {
        vi.push_back(10);
    }

    int sum = parallel_accumulate(vi.begin(), vi.end(), 5);

    std::cout << "sum = " << sum << std::endl;
}
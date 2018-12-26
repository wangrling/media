
cmake_minimum_required(VERSION 2.8)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -pthread -fno-rtti -fexceptions -Wall")

find_package(Threads)

set(TESTS
        hello_concurrent_world
        
        # 2-8
        launching_a_thread
        waiting_in_exceptional_circumstances
        wait_for_a_thread_to_complete
        running_threads_in_the_background
        return_thread_from_a_function
        scoped_thread
        spawn_some_thread_and_wait_to_finish
        parallel_version_of_accumulate

        # 3-13
        protecting_list_with_mutex
        reference_to_protected_data
        stack_container_adapter
        thread_safe_stack
        fleshed_out_class_stack
        deadlock_solution
        lock_hierarchy_prevent_dealock
        simple_hierarchical_mutex
        flexible_unique_lock
        locking_appropriate_granularity
        lazy_initialization_using_mutex
        lazy_initialization_use_call_once
        # 未安裝
        # boost_shared_mutex

        # 4-16
        waiting_other_condition
        queue_interface
        thread_safe_queue
        extracting_push_pop
        queue_definition_condition_variable
        asynchronous_task_with_future
        passing_argument_with_async
        partial_packaged_task
        packaged_task_gui_thread
        promises_multiple_connections
        timeout_waiting_condition
        sequential_quicksort
        futures_quicksort
        spawn_task_implementation
        atm_logic_class
        get_pin_state_function

        # 5-13

        # 6-13

        # 7-21

        # 8-13

        # 9-13
        )

foreach(TEST ${TESTS})
    add_executable(${TEST} ${TEST}.cpp)
    target_link_libraries(${TEST} ${CMAKE_THREAD_LIBS_INIT})
endforeach(TEST)
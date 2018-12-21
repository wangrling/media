
cmake_minimum_required(VERSION 3.6)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fexceptions -Wall")

find_package(Threads)

set(TESTS
        SimpleHello
        LaunchingThread WaitingThread RAIIIdiom
        )

foreach(TEST ${TESTS})
    add_executable(${TEST} ${TEST}.cpp)
    target_link_libraries(${TEST} ${CMAKE_THREAD_LIBS_INIT})
endforeach(TEST)

target_link_libraries(SimpleHello ${CMAKE_THREAD_LIBS_INIT})
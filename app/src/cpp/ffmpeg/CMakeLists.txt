
cmake_minimum_required(VERSION 3.6)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
# -D__STDC_CONSTANT_MACROS
# the compiler complains about 'UINT64_C' was not declared in this scope.
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fno-exceptions -Wall -D__STDC_CONSTANT_MACROS")

# 包含当前头文件搜索路径
include_directories(${CMAKE_CURRENT_SOURCE_DIR})

# 链接库位置
# link_directories(/home/wangrl/github/media/app/src/cpp/ffmpeg/libs)
link_directories(${CMAKE_CURRENT_SOURCE_DIR}/libs)
link_directories(/usr/local/include/)
#link_directories(/usr/lib/x86_64-linux-gnu/)

set(libffmpeg  libavutil.so  libavdevice.so libavfilter.so libavformat.so libavcodec.so
        libswresample.so libswscale.so)

# 生成可执行文件
# set(TESTS seek_test matedata)
# foreach(TEST ${TESTS})
#     add_executable(${TEST} tests/seek_test.cpp)
#     target_link_libraries(${TEST}　${libffmpeg})
# endforeach(TEST)

add_executable(seek_test tests/seek_test.cpp)
# Specify libraries of flags to use when linking a given target and or its dependents.
target_link_libraries(seek_test  ${libffmpeg})

add_executable(metadata tests/metadata.cpp)
target_link_libraries(metadata ${libffmpeg})

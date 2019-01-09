
cmake_minimum_required(VERSION 3.6)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")

# -D__STDC_CONSTANT_MACROS
# the compiler complains about 'UINT64_C' was not declared in this scope.
# set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fno-exceptions -Wall -D__STDC_CONSTANT_MACROS")

# 包含当前头文件搜索路径
include_directories(${CMAKE_CURRENT_SOURCE_DIR})

# 链接库位置
# link_directories(/home/wangrl/github/media/app/src/cpp/ffmpeg/libs)
link_directories(${CMAKE_CURRENT_SOURCE_DIR}/libs)

set(libffmpeg  libavutil.so  libavdevice.so libavfilter.so libavformat.so libavcodec.so
        libswresample.so libswscale.so)

set(TESTS avio_dir_cmd avio_reading decode_audio decode_video demuxing_decoding encode_audio encode_video
        extract_mvs filter_audio filtering_video http_multiclient hw_decode metadata muxing  remuxing
        resampling_audio scaling_video seek transcode_aac transcoding vaapi_encode vaapi_transcode)
foreach(TEST ${TESTS})
        add_executable(${TEST} tests/${TEST}.c)
        target_link_libraries(${TEST} ${libffmpeg} -lm)
endforeach(TEST)

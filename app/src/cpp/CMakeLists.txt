cmake_minimum_required(VERSION 3.6)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fno-exceptions -Wall")

if (${ANDROID_PLATFORM_LEVEL} LESS 18)
    message(FATAL_ERROR "OpenGL 3 is not supported before API level 18 \
            (currently using ${ANDROID_PLATFORM_LEVEL}).")
    return()
else ()
    set(OPENGL_LIB GLESv3)
endif (${ANDROID_PLATFORM_LEVEL} LESS 18)

set (SAMPLES ndk)
include_directories(${SAMPLES})

add_library(androidndk SHARED
            ${SAMPLES}/GLES3JNI.cpp
            ${SAMPLES}/GLES3Renderer.cpp
            ${SAMPLES}/Plasma.cpp
            ${SAMPLES}/NativeCodec.cpp
            ${SAMPLES}/Looper.cpp
        )
# OpenSL ES OpenSLES
# plasma    jnigraphics
# OpenGL ES GLESv3
# MediaCodec OpenMAXAL mediandk
target_link_libraries(androidndk
        ${OPENGL_LIB} jnigraphics mediandk OpenMAXAL android EGL log m )

# Concurrency 部分单独编译
# add_subdirectory(concurrency)

# sonic
add_library(sonic SHARED sonic/SonicJni.cpp sonic/Sonic.cpp)
target_link_libraries(sonic android log m)

# decode/encode codec
add_library(codec SHARED codec/WavReader.cpp)
target_link_libraries(codec android log m)
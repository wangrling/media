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
        )

target_link_libraries(androidndk
        ${OPENGL_LIB} android EGL log m)
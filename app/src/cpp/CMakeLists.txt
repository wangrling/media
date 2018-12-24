cmake_minimum_required(VERSION 3.6)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
# -D__STDC_CONSTANT_MACROS
# the compiler complains about 'UINT64_C' was not declared in this scope.
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fno-exceptions -Wall -D__STDC_CONSTANT_MACROS")

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
            ${SAMPLES}/JNICallback.cpp
            ${SAMPLES}/NativeAudio.cpp
            ${SAMPLES}/NativeCodec.cpp
            ${SAMPLES}/Looper.cpp
            ${SAMPLES}/OpenMaxMedia.cpp
            ${SAMPLES}/AndroidFopen.cpp
            ${SAMPLES}/faudio/FastAudioJNI.cpp
            ${SAMPLES}/NativeActivity.cpp
        )
# OpenSL ES OpenSLES
# plasma    jnigraphics
# OpenGL ES GLESv3
# MediaCodec OpenMAXAL mediandk
target_link_libraries(androidndk
        ${OPENGL_LIB} jnigraphics mediandk OpenSLES OpenMAXAL android EGL log m )

# Concurrency 部分单独编译
# add_subdirectory(concurrency)

# sonic
add_library(sonic SHARED sonic/SonicJni.cpp sonic/Sonic.cpp)
target_link_libraries(sonic android log m)

# decode/encode codec
add_library(codec SHARED codec/WavReader.cpp)
target_link_libraries(codec android log m)

# native activity
add_library(glue STATIC
        ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c)

# Export ANativeActivity_onCreate()
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -u ANativeActivity_onCreate")
add_library(native SHARED ${SAMPLES}/NativeActivity.cpp)
include_directories(native PRIVATE
        ${ANDROID_NDK}/sources/android/native_app_glue/)
# add lib dependencies
target_link_libraries(native android glue EGL  GLESv3 log)

add_library(teapot SHARED ${SAMPLES}/TeapotActivity.cpp ${SAMPLES}/TeapotRenderer.cpp)
target_link_libraries(teapot android glue EGL GLESv3 log)

include_directories(glm)
add_subdirectory(glm)

# opengl-es-sdk-for-android
include_directories(opengl)

# 用于glm测试的库
add_library(glmtest SHARED test/GlmTest.cpp)
target_link_libraries(glmtest glm_shared android log m)

# 配置网址
# https://hacpai.com/article/1538565561197
include_directories(ffmpeg)

# 编译器的类型问题
# libavutil.so: error adding symbols: File in wrong forma
# 直接在Ubuntu电脑上使用，配置底层的编译器太麻烦。
# /home/wang/Android/Sdk/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/clang++
# add_library(avformat SHARED IMPORTED)
# set_target_properties(avformat PROPERTIES IMPORTED_LOCATION
#         ${CMAKE_CURRENT_SOURCE_DIR}/ffmpeg/libavformat.so)

# add_library(avutil SHARED IMPORTED)
# set_target_properties(avutil PROPERTIES IMPORTED_LOCATION
#         ${CMAKE_CURRENT_SOURCE_DIR}/ffmpeg/libavutil.so)





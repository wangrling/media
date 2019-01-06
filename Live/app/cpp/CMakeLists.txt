cmake_minimum_required(VERSION 3.6)


add_library(codec SHARED codec/OpenMaxAL.cpp
        codec/FileOpenAsset.cpp
        )
#MediaCodec OpenMAXAL mediandk
target_link_libraries(codec mediandk OpenMAXAL android log m)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/filament/include)
add_library(filament SHARED
        filament/jni/Engine.cpp

        filament/Engine.cpp
        filament/Filament.cpp)
target_link_libraries(filament log GLESv3 EGL android jnigraphics)
cmake_minimum_required(VERSION 3.6)


add_library(codec SHARED codec/OpenMaxAL.cpp
        codec/FileOpenAsset.cpp
        )
#MediaCodec OpenMAXAL mediandk
target_link_libraries(codec mediandk OpenMAXAL android log m)
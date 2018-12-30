
cmake_minimum_required(VERSION 3.6)

include_directories(libAACdec/src)
include_directories(libAACdec/include)
include_directories(libAACenc/src)
include_directories(libAACenc/include)
include_directories(libArithCoding/src)
include_directories(libArithCoding/include)
include_directories(libDRCdec/src)
include_directories(libDRCdec/include)
include_directories(libFDK/src)
include_directories(libFDK/include)
include_directories(libMpegTPDec/src)
include_directories(libMpegTPDec/include)
include_directories(libMpegTPEnc/src)
include_directories(libMpegTPEnc/include)
include_directories(libPCMutils/src)
include_directories(libPCMutils/include)
include_directories(libSACdec)
include_directories(libSACdec/include)
include_directories(libSACenc/src)
include_directories(libSACenc/include)
include_directories(libSBRdec/src)
include_directories(libSBRdec/include)
include_directories(libSBRenc/src)
include_directories(libSBRenc/include)
include_directories(libSYS/src)
include_directories(libSYS/include)
include_directories(test)

include_directories(${CMAKE_CURRENT_SOURCE_DIR})

file (GLOB aac_dec "libAACdec/src/*.cpp")
file (GLOB aac_enc "libAACenc/src/*.cpp")
file (GLOB arith_coding "libArithCoding/src/*.cpp")
file (GLOB drc_dec   "libDRCdec/src/*.cpp")
file (GLOB fdk  "libFDK/src/*.cpp")
file (GLOB mpeg_tp_dec "libMpegTPDec/src/*.cpp")
file (GLOB mpeg_tp_end "libMpegTPEnc/src/*.cpp")
file (GLOB pcm_utils "libPCMutils/src/*.cpp")
file (GLOB sac_dec "libSACdec/src/*.cpp")
file (GLOB sac_enc "libSACenc/src/*.cpp")
file (GLOB sbr_dec "libSBRdec/src/*.cpp")
file (GLOB srb_enc "libSBRenc/src/*.cpp")
file (GLOB sys "libSYS/src/*.cpp")

set(aac_src ${aac_dec} ${aac_enc} ${arith_coding}
        ${drc_dec} ${fdk} ${mpeg_tp_dec} ${mpeg_tp_end} ${pcm_utils}
        ${sac_dec} ${sac_enc} ${sbr_dec} ${srb_enc} ${sys})

add_library(aac SHARED ${aac_src})

add_executable(aac_enc test/aac_enc.c test/wav_reader.c)
target_link_libraries(aac_enc aac)




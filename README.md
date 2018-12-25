# MultiMedia
关注移动端多媒体。

jni调用层
processFile(String path, String mimeType, MediaScannerClient client);
android_media_MediaScanner_processFile(JNIEnv* env, jobject thiz, jstring path, jobject client);
如果是静态调用就使用jclass类型。

AudioTrack的cts测试，主要使用流程set -> getMinBufferSize -> write(重要) -> play -> release等，其中有生成正弦波形的代码。

1. 精通Java, 熟悉c/c++, 熟悉Android/ios平台
架构原理；
2. 熟悉多线程、网络编程，了解http, rtmp, rtsp
等协议。
3. 掌握OpenGLES渲染技术，熟悉多种格式的音视频
软硬编解码技术；
4. 熟练使用FFmpeg等开源框架，熟悉OpenH264、
H265、AAC、RTMP、RTSP、HLS、MP4等各种网络协议
或者视频格式等优先。
5. 丰富的多媒体行业经验，对视频处理方面具有前瞻性
和自己的见解和规划

C++库中添加FFmpeg头文件
extern "C" {
#include <libavutil/imgutils.h>
#include <libavcodec/avcodec.h>
#include <libswscale/swscale.h>
}

endian的问题参考Computer Systems: A Programmer's Perspective
修改wav_header.h文件，使它不依赖WEBRTC_ARCH_LITTLE_ENDIAN定义。

wav格式协议
http://soundfile.sapp.org/doc/WaveFormat/

opus
https://opus-codec.org/

x264
http://git.videolan.org/git/x264.git
x264 is a free software library and application for encoding video streams into
the H.264/MPEG-4 AVC compression format, and is released under the terms of the
GNU GPL.




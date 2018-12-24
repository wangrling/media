package com.android.mm.ffmpeg;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * 打印配置信息。
 * char info[10000] = {0};
 * sprintf(info, "%s\n", avcodec_configuration());
 * return (env)->NewStringUTF(info);
 *
 * 支持的编解码信息。
 * av_register_all();
 * AVCodec* temp = av_codec_next(NULL);
 * while (temp != NULL) {
 *     if (temp->decode != NULL) {
 *
 *     } else {
 *
 *     }
 *     switch(temp->type) {
 *         case AVMEDIA_TYPE_VIDEO:
 *              break;
 *         case AVMEDIA_TYPE_AUDIO:
 *              break;
 *         default:
 *              break;
 *     }
 *     // 编解码器信息。
 *     info = temp->name;
 *     temp = temp->next;
 * }
 */

/**
 * libavutil contains various routines used to simplify programming, including random number
 * generators, data structures, mathematics routines, core multimedia utilities, and much more.
 *
 * libavcodec provides a decoding and encoding API, and all the supported codecs.
 *
 * libavformat provides a demuxing and muxing API, and all the supported muxers and de-muxers.
 *
 * libavdevice provides an interface for grabbing from input devices (e.g. webcames or line-in
 *      audio) and rendering to output devices, and all the supported input and output devices
 *
 * libswscale provides a scaling and (raw pixel) format conversions API, with high
 *      speed/assembly optimized versions of several scaling routines.
 *
 * libavfilter provides an audio and video filtering API, and all the supported filters.
 *
 * libpostproc provides video postprocessing routines
 *
 * libswresample provides an audio resampling, rematrixing and sample format conversion API,
 *      and many high-quality optimized routines.
 */

// 详细内容参考http://ffmpeg.org/doxygen/trunk/index.html

public class FFMpegActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}

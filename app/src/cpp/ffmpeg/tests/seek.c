#include "libavutil/common.h"
#include "libavutil/mathematics.h"
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"

static char buffer[20];

static const char *ret_str(int v)
{
    switch (v)
    {
    case AVERROR_EOF:
        return "-EOF";
    case AVERROR(EIO):
        return "-EIO";
    case AVERROR(ENOMEM):
        return "-ENOMEM";
    case AVERROR(EINVAL):
        return "-EINVAL";
    default:
        snprintf(buffer, sizeof(buffer), "%2d", v);
        return buffer;
    }
}

static void ts_str(char buffer[60], int64_t ts, AVRational base)
{
    if (ts == AV_NOPTS_VALUE)
    {
        strcpy(buffer, " NOPTS   ");
        return;
    }
    ts = av_rescale_q(ts, base, (AVRational){1, 1000000});
    snprintf(buffer, 60, "%c%" PRId64 ".%06" PRId64 "", ts < 0 ? '-' : ' ', FFABS(ts) / 1000000, FFABS(ts) % 1000000);
}

int main(int argc, char *argv[])
{

    const char *filename;

    AVFormatContext *context = avformat_alloc_context();

    int64_t seek_first = AV_NOPTS_VALUE;

    int64_t timestamp;

    // 设置格式
    AVDictionary *format_opts = NULL;

    int first_back = 0;

    int ret;

    int stream_id;

    // 指定帧数
    int frame_count = 1;

    // 指定时间
    int duration = 4;

    for (int i = 2; i < argc; i += 2)
    {
        if (!strcmp(argv[i], "-seek_forward"))
        {
            seek_first = atoi(argv[i + 1]);
        }
        else if (!strcmp(argv[i], "-seek_back"))
        {
            seek_first = atoi(argv[i + 1]);
            first_back = 1;
        }
        else if (!strcmp(argv[i], "-frames"))
        {
            frame_count = atoi(argv[i + 1]);
        }
        else if (!strcmp(argv[i], "-duration"))
        {
            duration = atoi(argv[i + 1]);
        }
        else if (!strcmp(argv[i], "-fast_seek"))
        {
            if (atoi(argv[i + 1]))
            {
                // Flags modifying the (de)muxer behaviour.
                context->flags != AVFMT_FLAG_FAST_SEEK;
            }
        }
        else if (argv[i][0] == '-' && argv[i + 1])
        {
            // 检测单字符
            av_dict_set(&format_opts, argv[i] + 1, argv[i + 1], 0);
        }
        else
        {
            argc = 1;
        }
    }

    // 设置声道和采样率。
    av_dict_set(&format_opts, "channels", "1", 0);
    av_dict_set(&format_opts, "sample_rate", "22050", 0);

    if (argc < 2)
    {
        printf("usage: %s input_file\n"
               "\n",
               argv[0]);
        return 1;
    }

    filename = argv[1];

    ret = avformat_open_input(&context, filename, NULL, &format_opts);

    av_dict_free(&format_opts);

    if (ret < 0)
    {
        fprintf(stderr, "cannot open %s\n", filename);
        return 1;
    }

    ret = avformat_find_stream_info(context, NULL);
    if (ret < 0)
    {
        fprintf(stderr, "%s: could not find codec parameters\n", filename);
        return 1;
    }

    if (seek_first != AV_NOPTS_VALUE)
    {
        if (first_back)
            avformat_seek_file(context, -1, INT64_MIN, seek_first, seek_first, 0);
        else
            avformat_seek_file(context, -1, seek_first, seek_first, INT64_MAX, 0);
    }

    for (int i = 0;; i++)
    {
        AVPacket pkt = {0};
        AVStream *av_uninit(st);

        char ts_buf[60];

        if (ret >= 0)
        {
            for (int j = 0; j < frame_count; j++)
            {
                ret = av_read_frame(context, &pkt);
                if (ret >= 0)
                {
                    char dts_buf[60];
                    st = context->streams[pkt.stream_index];
                    ts_str(dts_buf, pkt.dts, st->time_base);
                    ts_str(ts_buf, pkt.pts, st->time_base);
                    printf("ret:%-10s st:%2d flags:%d dts:%s pts:%s pos:%7" PRId64 " size:%6d",
                           ret_str(ret), pkt.stream_index, pkt.flags, dts_buf, ts_buf, pkt.pos,
                           pkt.size);
                    av_packet_unref(&pkt);
                }
                else
                    printf("ret:%s", ret_str(ret)); // necessary to avoid trailing whitespace
                printf("\n");
            }
        }

        if (i > 25)
            break;

        stream_id = (i >> 1) % (context->nb_streams + 1) - 1;
        timestamp = (i * 19362894167LL) % (duration * AV_TIME_BASE) - AV_TIME_BASE;
        if (stream_id >= 0)
        {
            st = context->streams[stream_id];
            timestamp = av_rescale_q(timestamp, AV_TIME_BASE_Q, st->time_base);
        }
        //FIXME fully test the new seek API
        if (i & 1)
            ret = avformat_seek_file(context, stream_id, INT64_MIN, timestamp, timestamp, 0);
        else
            ret = avformat_seek_file(context, stream_id, timestamp, timestamp, INT64_MAX, 0);
        ts_str(ts_buf, timestamp, stream_id < 0 ? AV_TIME_BASE_Q : st->time_base);
        printf("ret:%-10s st:%2d flags:%d  ts:%s\n", ret_str(ret), stream_id, i & 1, ts_buf);
    }

    avformat_close_input(&context);

    return ret;
}

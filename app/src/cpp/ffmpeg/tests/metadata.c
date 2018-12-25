#include <stdio.h>
#include <libavformat/avformat.h>
#include <libavutil/dict.h>

/**
 * 解析文件的元数据，慢慢实践理解。
 */

int main(int argc, char **argv)
{

    /**
     * Format I/O context.
     * Use avformat_alloc_context() to create AVFormatContext.
     * 结构体有几十个成员变量。
     */
    AVFormatContext *fmt_ctx = NULL;

    /**
     * Metadata that applies to the whole file.
     *
     * - demuxing: set by libavformat in avformat_open_input()
     * - muxing: may be set by the caller before avformat_write_header()
     *
     * Freed by libavformat in avformat_free_context().
     */
    AVDictionaryEntry *tag = NULL;

    int ret;

    if (argc != 2)
    {
        printf("usage: %s <input_file>\n"
               "example program to demonstrate the use of the libavformat metadata API.\n"
               "\n",
               argv[0]);
        return 1;
    }

    /**
     * Open an input stream and read the header.
     *
     * @param ps    传递AVFormatContext** ps变量。
     * May be a pointer to NULL, in which case an AVFormatContext is allocated by this function
     * and written int ps.
     * @return  a negative AVERROR on failure.
     */
    if ((ret = avformat_open_input(&fmt_ctx, argv[1], NULL, NULL)))
        return ret;

    /**
     * Get a dictionary entry with matching key.
     * 函数原型AVDictionaryEntry *av_dict_get(const AVDictionary *m, const char *key,
     * const AVDictionaryEntry *prev, int flags);
     *
     * AVDictionaryEntry只有两个成员变量 char* key, char* value.
     * To iterate through all the dictionary entries, you can set the matching key to the null
     * string "" and set the AV_DICT_IGNORE_SUFFIX flag.
     *
     * 遍历所有的元数据。
     */
    while ((tag = av_dict_get(fmt_ctx->metadata, "", tag, AV_DICT_IGNORE_SUFFIX)))
        printf("%s=%s\n", tag->key, tag->value);

    /**
     * Close an opened input AVFormatContext.
     * Free it and all its contents and set AVFormatContext to NULL.
     */
    avformat_close_input(&fmt_ctx);

    return 0;
}

//
// Created by wang on 18-12-21.
//

#include "WavReader.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#define TAG(a, b, c, d) (((a) << 24) | ((b) << 16) | ((c) << 8) | (d))

struct wavReader {
    FILE* wav;
    uint32_t dataLength;

    int format;
    int sampleRate;
    int bitsPerSample;
    int channels;
    int byteRate;
    int blockAlign;

    // 以流的形式传递？
    int streamed;
};

// big endian
static uint32_t readTag(struct wavReader* wr) {
    uint32_t tag = 0;
    tag = (tag << 8) | fgetc(wr->wav);
    tag = (tag << 8) | fgetc(wr->wav);
    tag = (tag << 8) | fgetc(wr->wav);
    tag = (tag << 8) | fgetc(wr->wav);
    return tag;
}

// 小端模式
static uint32_t  readInt32(struct wavReader* wr) {
    uint32_t value = 0;
    value |= fgetc(wr->wav) << 0;
    value |= fgetc(wr->wav) << 8;
    value |= fgetc(wr->wav) << 16;
    value |= fgetc(wr->wav) << 24;
    return value;
}

// 小端模式
static uint16_t readInt16(struct wavReader* wr) {
    uint16_t value = 0;
    value |= fgetc(wr->wav) << 0;
    value |= fgetc(wr->wav) << 8;

    return value;
}

static void skip(FILE* f, int n) {
    int i;
    for (i = 0; i < n; i++) {
        fgetc(f);
    }
}

void *wavReadOpen(const char *filename) {
    // 分配地址
    struct wavReader* wr = (struct wavReader*) malloc(sizeof(*wr));
    memset(wr, 0, sizeof(*wr));

    long dataPos = 0;

    // FILE类型的文件结构体
    wr->wav = fopen(filename, "rb");
    if (wr->wav == NULL) {
        free(wr);
        return NULL;
    }

    // WAVE的详细描述在
    // http://soundfile.sapp.org/doc/WaveFormat/

    while (1) {
        uint32_t chunkID, chunkSize, format;
        // 第四字节
        chunkID = readTag(wr);

        if (feof(wr->wav))
            break;

        // ChunkSize 文件总长度，以字节计算，不包括开头的八个字节。
        //　第八字节
        chunkSize = readInt32(wr);

        // 如果文件太大就以流的形式传递。
        // 为什么规定0x7fff0000大小。
        if (!chunkSize || chunkSize >= 0x7fff0000) {
            wr->streamed = 1;
            //取反 0xffffffff
            chunkSize = ~0;
        }

        // 判断文件是否以RIFF开头。
        if (chunkID != TAG('R', 'I', 'F', 'F') || chunkSize < 4) {
            // 跳到文件的结尾。
            fseek(wr->wav, chunkSize, SEEK_CUR);
            continue;
        }

        // 第12字节
        format = readTag(wr);
        chunkSize -= 4;
        if (format != TAG('W', 'A', 'V', 'E')) {
            // 跳到文件的结尾寻找WAVE开头的字段。
            fseek(wr->wav, chunkSize, SEEK_CUR);
        }

        // RIFF chunk found, iterate through it.
        // SubChunkID + SubChunkSize == 8字节。
        while (chunkSize >= 8) {
            uint32_t subChunkID, subChunkSize;
            // 'fmt ' 或者 'data'
            subChunkID = readTag(wr);
            if (feof(wr->wav))
                break;
            subChunkSize = readInt32(wr);
            chunkSize -= 8;
            // 第一次进入循环减去12字节，总长度小于剩余的长度就会跳出。
            if (chunkSize < subChunkSize)
                break;
            if (subChunkID == TAG('f', 'm', 't', ' ')) {
                // 获取到格式信息。
                // 长度不包括本身。
                if (subChunkSize < 16) {
                    // Insufficient data for 'fmt '
                    break;
                }
                // AudioFormat
                wr->format = readInt16(wr);
                // NumChannels
                wr->channels = readInt16(wr);
                // SampleRate
                wr->sampleRate = readInt32(wr);
                wr->byteRate = readInt32(wr);
                wr->blockAlign = readInt16(wr);
                wr->bitsPerSample = readInt16(wr);

                // format的信息读取完成。
                // 第36字节
                // PCM = 1 (i.e. Linear quantization)
                // Values other than 1 indicate some
                // form of compression.
                // WAVEFORMATEX structure
                // 目前不讨论，进入else判断。
                if (wr->format == 0xfffe) {
                    if (subChunkSize < 28) {
                        // Insufficient data for waveformatex
                        break;
                    }
                    skip(wr->wav, 8);
                    wr->format = readInt32(wr);
                    skip(wr->wav, subChunkSize - 28);
                } else {
                    // 相当于跳过0字节。
                    skip(wr->wav, subChunkSize - 16);
                }
            } else if (subChunkID == TAG('d', 'a', 't', 'a')) {
                dataPos = ftell(wr->wav);
                wr->dataLength = subChunkSize;
                if (!wr->dataLength || wr->streamed) {
                    // 前面的判断为0,
                    wr->streamed = 1;
                    return wr;
                }
                fseek(wr->wav, subChunkSize, SEEK_CUR);
            } else {
                skip(wr->wav, subChunkSize);
            }
            chunkSize -= subChunkSize;
        }

        // 不在内层循环里。
        if (chunkSize > 0) {
            // Back chunk?
            fseek(wr->wav, chunkSize, SEEK_CUR);
        }
    }
    fseek(wr->wav, dataPos, SEEK_SET);
    return wr;
}

void wavReadClose(void *obj) {
    struct wavReader* wr = (struct wavReader*) obj;
    if (wr->wav != stdin)
        fclose(wr->wav);
    free(wr);
}

// open之后获取文件指针然后获取头信息。
int wavGetHeader(void *obj, int *format, int *channels, int *sampleRate, int *bitsPerSample,
                 unsigned int *dataLength) {

    struct wavReader* wr = (struct wavReader*) obj;
    if (format)
        *format = wr->format;
    if (channels)
        *channels = wr->channels;
    if (sampleRate)
        *sampleRate = wr->sampleRate;
    if (bitsPerSample)
        *bitsPerSample = wr->bitsPerSample;
    if (dataLength)
        *dataLength = wr->dataLength;
    return wr->format && wr->sampleRate;
}

int wavReadData(void *obj, unsigned char *data, unsigned int length) {
    struct wavReader* wr = (struct wavReader*) obj;
    int n;
    if (wr->wav == NULL)
        return -1;
    if (length > wr->dataLength && !wr->streamed)
        length = wr->dataLength;

    // 以单个字节的形式读取长度为length的数据存储到data指针中。
    // 读的数据不会大于指定的长度。
    n = fread(data, 1, length, wr->wav);

    return n;
}

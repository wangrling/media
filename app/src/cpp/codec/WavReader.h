#pragma once

#ifdef __cplusplus
extern "C" {
#endif

void* wavReadOpen(const char* filename);
void wavReadClose(void* obj);

int wavGetHeader(void* obj, int* format, int* channels, int* sampleRate, int* bitsPerSample,
                 unsigned int* dataLength);

int wavReadData(void* obj, unsigned char* data, unsigned int length);


#ifdef __cplusplus
};
#endif
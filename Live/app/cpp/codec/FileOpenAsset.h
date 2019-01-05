#pragma

#include <android/asset_manager.h>
#include <cstdio>

extern "C" {
    void android_fopen_set_asset_manager(AAssetManager* manager);
    FILE* android_fopen(const char* filename, const char* mode);
}

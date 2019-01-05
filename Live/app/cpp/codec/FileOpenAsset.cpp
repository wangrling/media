
#include <cerrno>
#include "FileOpenAsset.h"

// must be established by someone else.
static AAssetManager* android_asset_manager = NULL;

static int android_read(void* cookie, char* buf, int size) {
    return AAsset_read((AAsset*)cookie, buf, size);
}

static int android_write(void* cookie, const char* buf, int size) {
    return EACCES; // can't provide write access to the apk
}

static fpos_t android_seek(void* cookie, fpos_t offset, int whence) {
    return AAsset_seek((AAsset*)cookie, offset, whence);
}

static int android_close(void* cookie) {
    AAsset_close((AAsset*)cookie);
    return 0;
}

void android_fopen_set_asset_manager(AAssetManager *manager) {
    android_asset_manager = manager;
}

FILE *android_fopen(const char *filename, const char *mode) {
    if (mode[0] == 'w')
        return NULL;

    AAsset* asset = AAssetManager_open(android_asset_manager, filename, 0);
    if (!asset)
        return NULL;

    // 定义四个函数指针
    return funopen(asset, android_read, android_write, android_seek, android_close);
}

# 需要指定编译器，具体需要的时候网上再查。

# Android默认使用clang++
# export PREFIX=/usr/local/ffmpeg/

# 设置NDK路径
# NDK=

# 设置编译工具链
# TOOLCHAIN=$NDK/toolchains/

./configure --disable-x86asm  \
--prefix=/usr/local/ffmpeg/ \
--target-os=android \
--disable-doc   \
--enable-shared \
--disable-static    \
--disable-dct   \
--disable-dwt   \
--disable-error-resilience  \
--disable-w32threads \
--disable-lsp   \
--disable-lzo   \
--disable-mdct  \
--disable-rdft  \
--disable-fft   \
--disable-faan  \
--disable-pixelutils    \
--disable-everything    \
--disable-sdl2  \
--disable-securetransport   \
--disable-amf       \
--disable-appkit    \
--disable-avfoundation  \
--disable-bzlib     \
--disable-coreimage \
--disable-iconv \
--disable-sndio \
--disable-schannel  \
--disable-xlib  \
--disable-zlib  \
--enable-demuxer=mp3    \
--enable-demuxer=aac    \
--enable-muxer=mp3      \
--enable-muxer=mp4








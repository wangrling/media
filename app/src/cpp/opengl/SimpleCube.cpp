#include <jni.h>
#include <android/log.h>
#include <cstdio>
#include <cstdlib>
#include <cmath>

#define LOG_TAG "SimpleCube"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

GLuint simpleCubeProgram;
GLuint vertexLocation;
GLuint vertexColourLocation;
GLuint projectionLocation;
GLuint modelViewLocation;

float projectionMatrix[16];
float modelViewMatrix[16];
float angle = 0;

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_opengl_SimpleCubeActivity_init(JNIEnv *env, jclass type, jint width,
                                                   jint height) {

    // TODO


}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_opengl_SimpleCubeActivity_step(JNIEnv *env, jclass type) {

    // TODO

}
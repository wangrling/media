#pragma once

#include <jni.h>
#include <errno.h>

#include <vector>

#include <EGL/egl.h>
#include <GLES/gl.h>

#include <android/sensor.h>
#include <android/log.h>
#include <android_native_app_glue.h>
#include <android/native_window_jni.h>

#define BUFFER_OFFSET(i) ((char*)NULL + (i))

struct TEAPOT_VERTEX {
    float pos[3];
    float normal[3];
};

enum SHADER_ATTRIBUTES {
    ATTRIB_VERTEX,
    ATTRIB_NORMAL,
    ATTRIB_UV,
};

struct SHADER_PARAMS {
    GLuint program_;
    GLuint light_;
    GLuint material_diffuse_;
    GLuint material_ambient_;
    GLuint material_specular_;

    GLuint matrix_projection_;
    GLuint matrix_view_;
};

struct TEAPOT_MATERIALS {
    float diffuse_color[3];
    float specular_color[4];
    float ambient_color[3];
};

class TeapotRenderer {
    int32_t num_indices_;
    int32_t num_vertices_;

    GLuint ibo_;
    GLuint vbo_;

    SHADER_PARAMS shader_params_;

    bool LoadShaders(SHADER_PARAMS* params, const char* strVsh,
                     const char* strFsh);


};

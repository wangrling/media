
#include <jni.h>
#include <android/log.h>

// 和equal函数相关。
#include <ext/scalar_relational.hpp>
#include <ext/vector_relational.hpp>

// -----------------------------------------------
// Core features
// Features that implement in C++ the GLSL specification as closely as possible.
// -----------------------------------------------
// Common functions provides GLSL common functions.
#include <common.hpp>
// Exponential functions provides GLSL exponential functions.
#include <exponential.hpp>
// Geometric functions  These operate on vectors as vectors, not component-wise.
#include <geometric.hpp>
// Vector types Vector types of two to four components with an exhaustive set of operators.
// Matrix types Matrix types of with C columns and R rows where C and R are values between 2 to 4 included.
#include <vec2.hpp>
#include <vec3.hpp>
#include <vec4.hpp>
#include <mat2x2.hpp>
#include <mat2x3.hpp>
#include <mat2x4.hpp>
#include <mat3x2.hpp>
#include <mat3x3.hpp>
#include <mat3x4.hpp>
#include <mat4x2.hpp>
#include <mat4x3.hpp>
#include <mat4x4.hpp>
// Integer functions Providers GLSL function on integer types.
#include <integer.hpp>
// Matrix functions Provides GLSL matrix functions.
#include <matrix.hpp>
// Floating-Point Pack and Unpack Functions
// Provides GLSL functions to pack and unpack half, single and double-precision floating
// point values into more compact integer types.
#include <packing.hpp>
// Function parameters specified as angle are assumed to be in units of radians.
#include <trigonometric.hpp>
// Vector Relational Functions
// Relational and equality operators (<, <=, >, >=, ==, !=) are
// defined to operate on scalars and produce scalar Boolean results
#include <vector_relational.hpp>


#define LOG_TAG "GLM"
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define ALOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)

int error(0);

int testFloor() {

        glm::vec3 A(1.1f);
        glm::vec3 B = glm::floor(A);
        error += glm::all(glm::equal(B, glm::vec3(1.0), 0.0001f)) ? 0 : 1;

    return error;
}

// Returns the fractional part of x and sets i to the integer part.
int testModf() {
    glm::vec4 X(1.1f, 1.2f, 1.5f, 1.7f);
    glm::vec4 I(0.0f);
    glm::vec4 A = glm::modf(X, I);

    error += glm::ivec4(I) == glm::ivec4(1) ? 0 : 1;
    // vec<L, bool, Q> glm::equal(vec<L, T, Q> const& x, vec<L, T, Q> const & y, T epsilon);
    // returns teh component-wise comparison of |x-y| < epsilon.
    // True if the expression is satisfied.
    // L Integer between 1 and 4 included that qualify the dimension of the vector.
    // T Floating-point or integer scalar types.
    // Q Value from qualifier enum.
    error += glm::all(glm::equal(A, glm::vec4(0.1f, 0.2f, 0.5f, 0.7f), 0.00001f)) ? 0 : 1;

    return error;
}

int testMod() {
    float A(3.0);
    float B(2.0f);
    float C = glm::mod(A, B);
    error += glm::equal(C, 1.0f, 0.00001f) ? 0 : 1;

    return error;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_android_mm_ndk_GlmTestActivity_checkTests(JNIEnv *env, jclass type) {

    // TODO
    return testFloor() + testModf() + testMod();
}



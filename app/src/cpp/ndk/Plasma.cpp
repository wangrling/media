//
// Created by wang on 18-12-18.
//

#include <jni.h>
#include <time.h>
#include <android/log.h>
#include <android/bitmap.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define  LOG_TAG    "plasma"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

/* Set to 1 to enable debug log traces. */
#define DEBUG 1

/* Set to 1 to optimize memory stores when generating plasma. */
#define OPTIMIZE_WRITES  1

/* Return current time in milliseconds */
static double nowMs(void)
{
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec*1000. + tv.tv_usec/1000.;
}

/* We're going to perform computations for every pixel of the target
 * bitmap. floating-point operations are very slow on ARMv5, and not
 * too bad on ARMv7 with the exception of trigonometric functions.
 *
 * For better performance on all platforms, we're going to use fixed-point
 * arithmetic and all kinds of tricks
 */

typedef int32_t  Fixed;

#define  FIXED_BITS           16
#define  FIXED_ONE            (1 << FIXED_BITS)
#define  FIXED_AVERAGE(x,y)   (((x) + (y)) >> 1)

#define  FIXED_FROM_INT(x)    ((x) << FIXED_BITS)
#define  FIXED_TO_INT(x)      ((x) >> FIXED_BITS)

#define  FIXED_FROM_FLOAT(x)  ((Fixed)((x)*FIXED_ONE))
#define  FIXED_TO_FLOAT(x)    ((x)/(1.*FIXED_ONE))

#define  FIXED_MUL(x,y)       (((int64_t)(x) * (y)) >> FIXED_BITS)
#define  FIXED_DIV(x,y)       (((int64_t)(x) * FIXED_ONE) / (y))

#define  FIXED_DIV2(x)        ((x) >> 1)
#define  FIXED_AVERAGE(x,y)   (((x) + (y)) >> 1)

#define  FIXED_FRAC(x)        ((x) & ((1 << FIXED_BITS)-1))
#define  FIXED_TRUNC(x)       ((x) & ~((1 << FIXED_BITS)-1))

#define  FIXED_FROM_INT_FLOAT(x,f)   (Fixed)((x)*(FIXED_ONE*(f)))

typedef int32_t  Angle;

#define  ANGLE_BITS              9

#if ANGLE_BITS < 8
#  error ANGLE_BITS must be at least 8
#endif

#define  ANGLE_2PI               (1 << ANGLE_BITS)
#define  ANGLE_PI                (1 << (ANGLE_BITS-1))
#define  ANGLE_PI2               (1 << (ANGLE_BITS-2))
#define  ANGLE_PI4               (1 << (ANGLE_BITS-3))

#define  ANGLE_FROM_FLOAT(x)   (Angle)((x)*ANGLE_PI/M_PI)
#define  ANGLE_TO_FLOAT(x)     ((x)*M_PI/ANGLE_PI)

#if ANGLE_BITS <= FIXED_BITS
#  define  ANGLE_FROM_FIXED(x)     (Angle)((x) >> (FIXED_BITS - ANGLE_BITS))
#  define  ANGLE_TO_FIXED(x)       (Fixed)((x) << (FIXED_BITS - ANGLE_BITS))
#else
#  define  ANGLE_FROM_FIXED(x)     (Angle)((x) << (ANGLE_BITS - FIXED_BITS))
#  define  ANGLE_TO_FIXED(x)       (Fixed)((x) >> (ANGLE_BITS - FIXED_BITS))
#endif

static Fixed angleSinTab[ANGLE_2PI+1];

static void initAngles(void) {
    int  nn;
    for (nn = 0; nn < ANGLE_2PI+1; nn++) {
        double  radians = nn*M_PI/ANGLE_PI;
        angleSinTab[nn] = FIXED_FROM_FLOAT(sin(radians));
    }
}

static __inline__ Fixed angleSin( Angle  a )
{
    return angleSinTab[(uint32_t)a & (ANGLE_2PI-1)];
}

static __inline__ Fixed angleCos( Angle  a )
{
    return angleSin(a + ANGLE_PI2);
}

static __inline__ Fixed fixedSin( Fixed  f )
{
    return angleSin(ANGLE_FROM_FIXED(f));
}

static __inline__ Fixed  fixedCos( Fixed  f )
{
    return angleCos(ANGLE_FROM_FIXED(f));
}

/* simple stats management */
typedef struct {
    double renderTime;
    double frameTime;
} FrameStats;

#define  MAX_FRAME_STATS  200
#define  MAX_PERIOD_MS    1500

typedef struct {
    double firstTime;
    double lastTime;
    double frameTime;

    int firstFrame;
    int numFrames;
    FrameStats frames[MAX_FRAME_STATS];
} Stats;

/* Color palette used for rendering the plasma */
#define  PALETTE_BITS   8
#define  PALETTE_SIZE   (1 << PALETTE_BITS)

#if PALETTE_BITS > FIXED_BITS
#  error PALETTE_BITS must be smaller than FIXED_BITS
#endif

static uint16_t  palette[PALETTE_SIZE];

static uint16_t  make565(int red, int green, int blue)
{
    return (uint16_t)( ((red   << 8) & 0xf800) |
                       ((green << 3) & 0x07e0) |
                       ((blue  >> 3) & 0x001f) );
}

static void initPalette(void) {
    int  nn, mm = 0;
    /* fun with colors */
    for (nn = 0; nn < PALETTE_SIZE/4; nn++) {
        int  jj = (nn-mm)*4*255/PALETTE_SIZE;
        palette[nn] = make565(255, jj, 255-jj);
    }

    for ( mm = nn; nn < PALETTE_SIZE/2; nn++ ) {
        int  jj = (nn-mm)*4*255/PALETTE_SIZE;
        palette[nn] = make565(255-jj, 255, jj);
    }

    for ( mm = nn; nn < PALETTE_SIZE*3/4; nn++ ) {
        int  jj = (nn-mm)*4*255/PALETTE_SIZE;
        palette[nn] = make565(0, 255-jj, 255);
    }

    for ( mm = nn; nn < PALETTE_SIZE; nn++ ) {
        int  jj = (nn-mm)*4*255/PALETTE_SIZE;
        palette[nn] = make565(jj, 0, 255);
    }
}

// Angles expressed as fixed point radians.
static void initTables(void) {
    initPalette();
    initAngles();
}

static void statsInit( Stats*  s )
{
    s->lastTime = nowMs();
    s->firstTime = 0.;
    s->firstFrame = 0;
    s->numFrames  = 0;
}

static void statsStartFrame(Stats* s) {
    s->frameTime = nowMs();
}

static __inline__ uint16_t  paletteFromFixed( Fixed  x )
{
    if (x < 0) x = -x;
    if (x >= FIXED_ONE) x = FIXED_ONE-1;
    int  idx = FIXED_FRAC(x) >> (FIXED_BITS - PALETTE_BITS);
    return palette[idx & (PALETTE_SIZE-1)];
}

static void fillPlasma( AndroidBitmapInfo*  info, void*  pixels, double  t )
{
    Fixed yt1 = FIXED_FROM_FLOAT(t/1230.);
    Fixed yt2 = yt1;
    Fixed xt10 = FIXED_FROM_FLOAT(t/3000.);
    Fixed xt20 = xt10;

#define  YT1_INCR   FIXED_FROM_FLOAT(1/100.)
#define  YT2_INCR   FIXED_FROM_FLOAT(1/163.)

    int  yy;
    for (yy = 0; yy < info->height; yy++) {
        uint16_t*  line = (uint16_t*)pixels;
        Fixed      base = fixedSin(yt1) + fixedSin(yt2);
        Fixed      xt1 = xt10;
        Fixed      xt2 = xt20;

        yt1 += YT1_INCR;
        yt2 += YT2_INCR;

#define  XT1_INCR  FIXED_FROM_FLOAT(1/173.)
#define  XT2_INCR  FIXED_FROM_FLOAT(1/242.)

#if OPTIMIZE_WRITES
        /* optimize memory writes by generating one aligned 32-bit store
         * for every pair of pixels.
         */
        uint16_t*  line_end = line + info->width;

        if (line < line_end) {
            if (((uint32_t)(uintptr_t)line & 3) != 0) {
                Fixed ii = base + fixedSin(xt1) + fixedSin(xt2);

                xt1 += XT1_INCR;
                xt2 += XT2_INCR;

                line[0] = paletteFromFixed(ii >> 2);
                line++;
            }

            while (line + 2 <= line_end) {
                Fixed i1 = base + fixedSin(xt1) + fixedSin(xt2);
                xt1 += XT1_INCR;
                xt2 += XT2_INCR;

                Fixed i2 = base + fixedSin(xt1) + fixedSin(xt2);
                xt1 += XT1_INCR;
                xt2 += XT2_INCR;

                uint32_t  pixel = ((uint32_t)paletteFromFixed(i1 >> 2) << 16) |
                                  (uint32_t)paletteFromFixed(i2 >> 2);

                ((uint32_t*)line)[0] = pixel;
                line += 2;
            }

            if (line < line_end) {
                Fixed ii = base + fixedSin(xt1) + fixedSin(xt2);
                line[0] = paletteFromFixed(ii >> 2);
                line++;
            }
        }
#else /* !OPTIMIZE_WRITES */
        int xx;
        for (xx = 0; xx < info->width; xx++) {

            Fixed ii = base + fixed_sin(xt1) + fixed_sin(xt2);

            xt1 += XT1_INCR;
            xt2 += XT2_INCR;

            line[xx] = palette_from_fixed(ii / 4);
        }
#endif /* !OPTIMIZE_WRITES */

        // go to next line
        pixels = (char*)pixels + info->stride;
    }
}

static void statsEndFrame(Stats* s) {
    double now = nowMs();
    double renderTime = now - s->frameTime;
    double frameTime  = now - s->lastTime;
    int nn;

    if (now - s->firstTime >= MAX_PERIOD_MS) {
        if (s->numFrames > 0) {
            double minRender, maxRender, avgRender;
            double minFrame, maxFrame, avgFrame;
            int count;

            nn = s->firstFrame;
            minRender = maxRender = avgRender = s->frames[nn].renderTime;
            minFrame  = maxFrame  = avgFrame  = s->frames[nn].frameTime;
            for (count = s->numFrames; count > 0; count-- ) {
                nn += 1;
                if (nn >= MAX_FRAME_STATS)
                    nn -= MAX_FRAME_STATS;
                double render = s->frames[nn].renderTime;
                if (render < minRender) minRender = render;
                if (render > maxRender) maxRender = render;
                double frame = s->frames[nn].frameTime;
                if (frame < minFrame) minFrame = frame;
                if (frame > maxFrame) maxFrame = frame;
                avgRender += render;
                avgFrame  += frame;
            }
            avgRender /= s->numFrames;
            avgFrame  /= s->numFrames;

            LOGI("frame/s (avg,min,max) = (%.1f,%.1f,%.1f) "
                 "render time ms (avg,min,max) = (%.1f,%.1f,%.1f)\n",
                 1000./avgFrame, 1000./maxFrame, 1000./minFrame,
                 avgRender, minRender, maxRender);
        }
        s->numFrames  = 0;
        s->firstFrame = 0;
        s->firstTime  = now;
    }

    nn = s->firstFrame + s->numFrames;
    if (nn >= MAX_FRAME_STATS)
        nn -= MAX_FRAME_STATS;

    s->frames[nn].renderTime = renderTime;
    s->frames[nn].frameTime  = frameTime;

    if (s->numFrames < MAX_FRAME_STATS) {
        s->numFrames += 1;
    } else {
        s->firstFrame += 1;
        if (s->firstFrame >= MAX_FRAME_STATS)
            s->firstFrame -= MAX_FRAME_STATS;
    }

    s->lastTime = now;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_mm_ndk_PlasmaActivity_renderPlasma(JNIEnv *env, jclass type, jobject bitmap,
                                                    jlong time) {

    // TODO
    AndroidBitmapInfo   info;
    void*               pixels;
    int                 ret;
    static Stats        stats;
    static int          init;

    if (!init) {
        initTables();
        statsInit(&stats);
        init = 1;
    }

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }

    if (info.format != ANDROID_BITMAP_FORMAT_RGB_565) {
        LOGE("Bitmap format is not RGB_565 !");
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    statsStartFrame(&stats);

    // Now fill the values with a nice little plasma.
    fillPlasma(&info, pixels, time);

    AndroidBitmap_unlockPixels(env, bitmap);

    statsEndFrame(&stats);
}


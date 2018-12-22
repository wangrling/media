package com.android.mm.grafika;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.mm.R;

import androidx.annotation.Nullable;

/**
 * Displays RGB color bars.
 * 使用SurfaceView进行绘制。
 */

public class ColorBarActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = GrafikaActivity.TAG;

    private SurfaceView mSurfaceView;

    private static final String[] COLOR_NAMES = {
            "black", "red", "green", "yellow", "blue", "magenta", "cyan", "white"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_color_bar);

        mSurfaceView = findViewById(R.id.colorBarSurfaceView);
        mSurfaceView.getHolder().addCallback(this);
        // Set the desired PixelFormat of the surface.
        mSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // ignore
        Log.v(TAG, "surfaceCreated holder=" + holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, "surfaceChanged fmt=" + format + " size=" + width + "x" + height +
                " holder=" + holder);
        // surfaceChanged fmt=1 size=720x1232 holder=android.view.SurfaceView$4@8591034
        // 调用一次
        Surface surface = holder.getSurface();
        drawColorBars(surface);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // ignore
        Log.v(TAG, "Surface destroyed holder=" + holder);
    }

    /**
     * Draw color bars with text labels.
     */
    private void drawColorBars(Surface surface) {
        Canvas canvas = surface.lockCanvas(null);
        try {
            // TODO: if the device is in portrait, draw the color bars horizontally.  Right
            // now this only looks good in landscape.
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            int least = Math.min(width, height);

            Log.d(TAG, "Drawing color bars at " + width + "x" + height);

            Paint textPaint = new Paint();
            Typeface typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
            textPaint.setTypeface(typeface);
            textPaint.setTextSize(least / 20);
            textPaint.setAntiAlias(true);

            Paint rectPaint = new Paint();
            for (int i = 0; i < 8; i++) {
                int color = 0xff000000;
                if ((i & 0x01) != 0) {
                    color |= 0x00ff0000;
                }
                if ((i & 0x02) != 0) {
                    color |= 0x0000ff00;
                }
                if ((i & 0x04) != 0) {
                    color |= 0x000000ff;
                }
                // 最后为白色
                rectPaint.setColor(color);

                float sliceWidth = width / 8;
                canvas.drawRect(sliceWidth * i, 0, sliceWidth * (i+1), height, rectPaint);
            }

            // Draw the labels last so they're on top of everything.
            for (int i = 0; i < 8; i++) {
                drawOutlineText(canvas, textPaint, COLOR_NAMES[i],
                        (width / 8) * i + 4, (height / 8) *((i & 1) + 1));
            }
        } finally {
            surface.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Draw white text surrounded by a 1-pixel black outline.
     */
    private static void drawOutlineText(Canvas canvas, Paint textPaint, String str,
                                        float x, float y) {
        // Is there a better way to do this?
        textPaint.setColor(0xff000000);
        canvas.drawText(str, x-1,    y,      textPaint);
        canvas.drawText(str, x+1,    y,      textPaint);
        canvas.drawText(str, x,      y-1,    textPaint);
        canvas.drawText(str, x,      y+1,    textPaint);
        canvas.drawText(str, x-0.7f, y-0.7f, textPaint);
        canvas.drawText(str, x+0.7f, y-0.7f, textPaint);
        canvas.drawText(str, x-0.7f, y+0.7f, textPaint);
        canvas.drawText(str, x+0.7f, y+0.7f, textPaint);
        textPaint.setColor(0xffffffff);
        canvas.drawText(str, x, y, textPaint);
    }
}

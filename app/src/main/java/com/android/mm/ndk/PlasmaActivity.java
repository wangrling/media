package com.android.mm.ndk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;

public class PlasmaActivity extends Activity {

    static {
        System.loadLibrary("androidndk");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);

        setContentView(new PlasmaView(this, displaySize.x, displaySize.y));
    }

    private class PlasmaView extends View {
        private Bitmap mBitmap;
        private long mStartTime;

        public PlasmaView(Context context, int width, int height) {
            super(context);

            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            mStartTime = System.currentTimeMillis();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            renderPlasma(mBitmap, System.currentTimeMillis() - mStartTime);
            canvas.drawBitmap(mBitmap, 0, 0, null);

            // force a redraw, with a different time-based pattern.
            invalidate();
        }
    }

    private static native void renderPlasma(Bitmap bitmap, long time);
}

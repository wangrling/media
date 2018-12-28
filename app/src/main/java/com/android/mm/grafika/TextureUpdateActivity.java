package com.android.mm.grafika;

import android.app.Activity;
import android.os.Bundle;

import com.android.mm.R;

import androidx.annotation.Nullable;

/**
 * An unscientific test of texture upload speed.
 */

public class TextureUpdateActivity extends Activity {

    private static final String TAG = GrafikaActivity.TAG;

    // Texture width/height.

    private static final int WIDTH = 512;       // must be power of 2
    private static final int HEIGHT = 512;
    private static final int ITERATIONS = 10;   // 10 iterations...
    private static final int TEX_PER_ITER = 8;  // ...uploading 8 textures per iteration

    private volatile boolean mIsCanceled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}

package com.android.mm.ndk;

import android.app.Activity;
import android.os.Bundle;

import com.android.mm.R;

import androidx.annotation.Nullable;

public class NativeAudioActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_native);
    }
}

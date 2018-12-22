package com.android.mm.ndk;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.mm.R;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

public class JNICallbackActivity extends Activity {

    static {
        System.loadLibrary("androidndk");
    }

    int hour = 0;
    int minute = 0;
    int second = 0;

    TextView tickView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_callback_jni);
        tickView = findViewById(R.id.tickView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hour = minute = second = 0;

        startTicks();
    }

    // 三件事情，java层调用c层start, stop函数，c层调用java层updateTimer函数。
    public native void startTicks();
    public native void stopTicks();

    @Override
    protected void onPause() {
        super.onPause();

        stopTicks();
    }

    // A function calling from JNI to update current timer.
    // 调用Java层的接口函数。
    @Keep
    private void updateTimer() {
        ++second;
        if (second >= 60) {
            ++minute;
            second -= 60;
            if (minute >= 60) {
                ++hour;
                minute -= 60;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTextView();
            }
        });
    }

    private void updateTextView() {
        String ticks = JNICallbackActivity.this.hour + ":" +
                JNICallbackActivity.this.minute + ":" +
                JNICallbackActivity.this.second;
        JNICallbackActivity.this.tickView.setText(ticks);
    }
}

package com.android.mm.ndk;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.mm.R;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

public class JNICallbackActivity extends Activity {

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

    private void startTicks() {

    }

    @Override
    protected void onPause() {
        super.onPause();

        stopTicks();
    }

    private void stopTicks() {

    }

    // A function calling from JNI to update current timer.
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

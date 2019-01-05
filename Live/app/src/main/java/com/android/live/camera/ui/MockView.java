package com.android.live.camera.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.android.live.camera.debug.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MockView extends FrameLayout {

    private static final Log.Tag TAG = new Log.Tag("MockView");


    public MockView(Context context) {
        super(context);
    }

    public MockView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.d(TAG, "onInterceptTouchEvent");

        return true;
    }
}

package com.android.mocktest.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class SwipeEventLayout extends CoordinatorLayout {

    private static final String TAG = "SwipeEventLayout";

    public SwipeEventLayout(@NonNull Context context) {
        super(context);
    }

    public SwipeEventLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.d(TAG, "action: " + ev.getActionMasked());

        return false;
    }
}

package com.android.live.camera.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.live.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ModeSelectorItem extends FrameLayout {

    private TextView mText;
    private ModeIconView mIcon;

    private int VisibleWidth = 0;
    private final int mMinVisibleWidth;
    private VisibleWidthChangedListener mListener = null;

    /**
     * A listener that gets notified when the visible width of the current item
     * is changed.
     */
    public interface VisibleWidthChangedListener {
        public void onVisibleWidthChanged(int width);
    }

    public ModeSelectorItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        mMinVisibleWidth = getResources()
                .getDimensionPixelSize(R.dimen.mode_selector_icon_block_width);
    }

    @Override
    protected void onFinishInflate() {


        super.onFinishInflate();
    }
}

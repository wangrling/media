package com.android.live.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.android.live.R;

import androidx.annotation.Nullable;

/**
 * 模式选择的图标
 * This class encapsulates the logic of drawing different states of the icon in
 * mode drawer for when it is highlighted (to indicate the current module), or when
 * it is selected by the user. It handles the internal state change like a state
 * list drawable. The advantage over a state list drawable is that in the class
 * multiple states can be rendered using the same drawable with some color modification,
 * whereas a state list drawable would require a different drawable for each state.
 */

public class ModeIconView extends View {

    private final GradientDrawable mBackground;

    private final int mIconBackgroundSize;
    private int mHighlightColor;
    private final int mBackgroundDefaultColor;
    private final int mIconDrawableSize;
    private Drawable mIconDrawable = null;


    public ModeIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mBackgroundDefaultColor = getResources().getColor(R.color.mode_selector_icon_background, null);
        mIconBackgroundSize = getResources().getDimensionPixelSize(R.dimen.mode_selector_icon_block_width);
        mBackground = (GradientDrawable) getResources()
                .getDrawable(R.drawable.mode_icon_background, null).mutate();
        mIconDrawableSize = getResources().getDimensionPixelSize(
                R.dimen.mode_selector_icon_drawable_size);
    }

    public void setIconDrawable(Drawable drawable) {
        mIconDrawable = drawable;

        // Center icon in the background.
        if (mIconDrawable != null) {
            mIconDrawable.setBounds(mIconBackgroundSize / 2 - mIconDrawableSize / 2,
                    mIconBackgroundSize / 2 - mIconDrawableSize / 2,
                    mIconBackgroundSize / 2 + mIconDrawableSize / 2,
                    mIconBackgroundSize / 2 + mIconDrawableSize / 2);
            invalidate();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mBackground.draw(canvas);
        if (mIconDrawable != null) {
            mIconDrawable.draw(canvas);
        }
    }

    /**
     * @return A clone of the icon drawable associated with this view.
     */
    public Drawable getIconDrawableClone() {
        return mIconDrawable.getConstantState().newDrawable();
    }

    /**
     * @return The size of the icon drawable.
     */
    public int getIconDrawableSize() {
        return mIconDrawableSize;
    }

    /**
     * This gets called when the selected state is changed. When selected, the background
     * drawable will use a solid pre-defined color to indicate selection.
     *
     * @param selected true when selected, false otherwise.
     */
    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            mBackground.setColor(mHighlightColor);
        } else {
            mBackground.setColor(mBackgroundDefaultColor);
        }

        invalidate();
    }

    /**
     * Sets the color that will be used in the drawable for highlight state.
     *
     * @param highlightColor color for the highlight state
     */
    public void setHighlightColor(int highlightColor) {
        mHighlightColor = highlightColor;
    }

    /**
     * @return The highlightColor color the the highlight state.
     */
    public int getHighlightColor() {
        return mHighlightColor;
    }
}

package com.android.live.camera.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.android.live.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 在View上画个小三角形图标。
 * This is a cling widget for settings button. In addition to drawing a cling button
 * background and overlaying text, it draws a small triangle that points at the
 * settings button that this cling is for.
 */
public class SettingsCling extends FrameLayout {

    private final int mClingTriangleHeight;
    private final int mClingTriangleWidth;

    private final Path mTrianglePath = new Path();
    private final Paint mClingPaint = new Paint();

    public SettingsCling(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        mClingTriangleHeight = getResources().getDimensionPixelSize(
                R.dimen.settings_cling_triangle_height);
        mClingTriangleWidth = getResources().getDimensionPixelSize(
                R.dimen.settings_cling_triangle_width);
        mClingPaint.setColor(getResources().getColor(R.color.settings_cling_color, null));
        mClingPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Updates the current position of the cling based on a reference view. If there
     * is enough space to lay out the cling on top of the reference view, then have
     * the cling on top. Otherwise, position the cling underneath the reference view.
     *
     * @param referenceView a view that cling uses as a position reference
     */
    public void updatePosition(View referenceView) {
        if (referenceView == null) {
            return ;
        }

        // 设计小三角形的路径

        // Right align cling:
        float referenceRight = referenceView.getX() + referenceView.getMeasuredWidth();
        setTranslationX(referenceRight - getMeasuredWidth());

        float referenceTop = referenceView.getY();

        if (referenceTop < getMeasuredHeight()) {
            // Layout cling under reference view.
            setTranslationY(referenceTop + referenceView.getMeasuredHeight());
            float triangleStartX = getMeasuredWidth() - referenceView.getMeasuredWidth() / 2;
            float triangleStartY = 0;
            mTrianglePath.reset();
            mTrianglePath.moveTo(triangleStartX, triangleStartY);
            mTrianglePath.lineTo(triangleStartX - mClingTriangleWidth / 2,
                    triangleStartY + mClingTriangleHeight);
            mTrianglePath.lineTo(triangleStartX + mClingTriangleWidth / 2,
                    triangleStartY + mClingTriangleHeight);
            mTrianglePath.lineTo(triangleStartX, triangleStartY);
        } else {
            // Layout cling on top of reference view.
            setTranslationY(referenceTop - getMeasuredHeight());
            float triangleStartX = getMeasuredWidth() - referenceView.getMeasuredWidth() / 2;
            float triangleStartY = getMeasuredHeight();
            mTrianglePath.reset();
            mTrianglePath.moveTo(triangleStartX, triangleStartY);
            mTrianglePath.lineTo(triangleStartX - mClingTriangleWidth / 2,
                    triangleStartY - mClingTriangleHeight);
            mTrianglePath.lineTo(triangleStartX + mClingTriangleWidth / 2,
                    triangleStartY - mClingTriangleHeight);
            mTrianglePath.lineTo(triangleStartX, triangleStartY);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw triangle
        canvas.drawPath(mTrianglePath, mClingPaint);
    }
}

package com.android.mm.exoplayer.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.accessibility.CaptioningManager;

import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * A view for display subtitle {@link Cue}s.
 *
 * 核心围绕样式展开，将接受TextOutput的回调进行显示。
 */

public class SubtitleView extends View implements TextOutput {

    /**
     * The default fractional text size.
     *
     * @see #setFractionalTextSize(float, boolean)
     */
    public static final float DEFAULT_TEXT_SIZE_FRACTION = 0.0533f;

    /**
     * The default bottom padding to apply when {@link Cue#line} is {@link Cue#DIMEN_UNSET}, as a
     * fraction of the viewport height.
     *
     * @see #setBottomPaddingFraction(float)
     */
    public static final float DEFAULT_BOTTOM_PADDING_FRACTION = 0.08f;

    private final List<SubtitlePainter> painters;

    private List<Cue> cues;
    private @Cue.TextSizeType int textSizeType;
    private float textSize;
    private boolean applyEmbeddedStyles;
    private boolean applyEmbeddedFontSizes;
    // 比如绘制阴影
    private CaptionStyleCompat style;
    private float bottomPaddingFraction;

    public SubtitleView(Context context) {
        this(context, null);
    }

    public SubtitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        painters = new ArrayList<>();
        // 字体的类型
        textSizeType = Cue.TEXT_SIZE_TYPE_FRACTIONAL;
        // 字体的大小
        textSize = DEFAULT_TEXT_SIZE_FRACTION;
        applyEmbeddedStyles = true;
        applyEmbeddedFontSizes = true;

        style = CaptionStyleCompat.DEFAULT;
        bottomPaddingFraction = DEFAULT_BOTTOM_PADDING_FRACTION;
    }

    @Override
    public void onCues(List<Cue> cues) {
        setCues(cues);
    }

    /**
     * Sets teh cues to be displayed by the view.
     *
     * @param cues The cues to display, or null to clear the cues.
     */
    public void setCues(List<Cue> cues) {
        if (this.cues == cues) {
            return;
        }
        this.cues = cues;
        // Ensure we have sufficient painters.
        int cueCount = (cues == null) ? 0 : cues.size();
        while (painters.size() < cueCount) {
            painters.add(new SubtitlePainter(getContext()));
        }// Invalidate to trigger drawing.
        invalidate();
    }

    // 对各种属性进行设置
    // 每次参数的改变都会进行重新绘制。
    // 字体大小，样式，底部的距离

    /**
     * Set the text size to a given unit and value.
     * <p>
     *     See {@link TypedValue} for the possible dimension units.
     * </p>
     * @param unit
     * @param size
     */
    public void setFixedTextSize(int unit, float size) {
        Context context = getContext();
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        setTextSize(Cue.TEXT_SIZE_TYPE_ABSOLUTE,
                TypedValue.applyDimension(unit, size, resources.getDisplayMetrics()));
    }

    /**
     * Sets the text size to one derived from {@link CaptioningManager#getFontScale()}, or to a
     * default size before API level 19.
     */
    public void setUserDefaultTextSize() {
        float fontScale = Util.SDK_INT >= 19 && !isInEditMode() ? getUserCaptionFontScaleV19() : 1f;
        setFractionalTextSize(DEFAULT_TEXT_SIZE_FRACTION * fontScale);
    }

    /**
     * 高度的几分之几
     * Sets the text size to be a fraction of the view's remaining height after its top and
     * bottom padding have been subtracted.
     * <p>
     *     Equivalent to {@code #setFractionalTextSize(fractionOfHeight, false)}.
     * </p>
     * @param fractionOfHeight    A fraction between 0 and 1.
     */
    public void setFractionalTextSize(float fractionOfHeight) {
        setFractionalTextSize(fractionOfHeight, false);
    }

    /**
     * Sets the text size to be a fraction of the height of this view.
     *
     * @param fractionOfHeight A fraction between 0 and 1.
     * @param ignorePadding Set to true if {@code fractionOfHeight} should be interpreted as a
     *     fraction of this view's height ignoring any top and bottom padding. Set to false if
     *     {@code fractionOfHeight} should be interpreted as a fraction of this view's remaining
     *     height after the top and bottom padding has been subtracted.
     */
    public void setFractionalTextSize(float fractionOfHeight, boolean ignorePadding) {
        setTextSize(
                ignorePadding
                        ? Cue.TEXT_SIZE_TYPE_FRACTIONAL_IGNORE_PADDING
                        : Cue.TEXT_SIZE_TYPE_FRACTIONAL,
                fractionOfHeight);
    }

    private void setTextSize(@Cue.TextSizeType int textSizeType, float textSize) {
        if (this.textSizeType == textSizeType && this.textSize == textSize) {
            return;
        }
        this.textSizeType = textSizeType;
        this.textSize = textSize;
        // Invalidate to trigger drawing.
        invalidate();
    }

    /**
     * Sets whether styling embedded within the cues should be applied. Enabled by default.
     * Overrides any setting made with {@link SubtitleView#setApplyEmbeddedFontSizes}.
     *
     * @param applyEmbeddedStyles Whether styling embedded within the cues should be applied.
     */
    public void setApplyEmbeddedStyles(boolean applyEmbeddedStyles) {
        if (this.applyEmbeddedStyles == applyEmbeddedStyles
                && this.applyEmbeddedFontSizes == applyEmbeddedStyles) {
            return;
        }
        this.applyEmbeddedStyles = applyEmbeddedStyles;
        this.applyEmbeddedFontSizes = applyEmbeddedStyles;
        // Invalidate to trigger drawing.
        invalidate();
    }

    /**
     * Sets whether font sizes embedded within the cues should be applied. Enabled by default.
     * Only takes effect if {@link SubtitleView#setApplyEmbeddedStyles} is set to true.
     *
     * @param applyEmbeddedFontSizes Whether font sizes embedded within the cues should be applied.
     */
    public void setApplyEmbeddedFontSizes(boolean applyEmbeddedFontSizes) {
        if (this.applyEmbeddedFontSizes == applyEmbeddedFontSizes) {
            return;
        }
        this.applyEmbeddedFontSizes = applyEmbeddedFontSizes;
        // Invalidate to trigger drawing.
        invalidate();
    }

    /**
     * Sets the caption style to be equivalent to the one returned by
     * {@link CaptioningManager#getUserStyle()}, or to a default style before API level 19.
     */
    public void setUserDefaultStyle() {
        setStyle(
                Util.SDK_INT >= 19 && isCaptionManagerEnabled() && !isInEditMode()
                        ? getUserCaptionStyleV19()
                        : CaptionStyleCompat.DEFAULT);
    }

    /**
     * Sets the caption style.
     *
     * @param style A style for the view.
     */
    public void setStyle(CaptionStyleCompat style) {
        if (this.style == style) {
            return;
        }
        this.style = style;
        // Invalidate to trigger drawing.
        invalidate();
    }

    /**
     * Sets the bottom padding fraction to apply when {@link Cue#line} is {@link Cue#DIMEN_UNSET},
     * as a fraction of the view's remaining height after its top and bottom padding have been
     * subtracted.
     * <p>
     * Note that this padding is applied in addition to any standard view padding.
     *
     * @param bottomPaddingFraction The bottom padding fraction.
     */
    public void setBottomPaddingFraction(float bottomPaddingFraction) {
        if (this.bottomPaddingFraction == bottomPaddingFraction) {
            return;
        }
        this.bottomPaddingFraction = bottomPaddingFraction;
        // Invalidate to trigger drawing.
        invalidate();
    }

    // Called by draw to draw the child views.
    @Override
    protected void dispatchDraw(Canvas canvas) {
        int cueCount = (cues == null) ? 0 : cues.size();
        int rawTop = getTop();
        int rawBottom = getBottom();

        // Calculate the bounds after padding is taken into account.
        int left = getLeft() + getPaddingLeft();
        int top = rawTop + getPaddingTop();
        int right = getRight() - getPaddingRight();
        int bottom = rawBottom - getPaddingBottom();
        if (bottom <= top || right <= left) {
            // No space to draw subtitles.
            return;
        }

        int rawViewHeight = rawBottom - rawTop;
        int viewHeightMinusPadding = bottom - top;

        float defaultViewTextSizePx =
                resolveTextSize(textSizeType, textSize, rawViewHeight, viewHeightMinusPadding);
        if (defaultViewTextSizePx <= 0) {
            // Text has no height.
            return ;
        }

        // 将所有的cue绘制出来。
        for (int i = 0; i < cueCount; i++) {
            Cue cue = cues.get(i);
            float cueTextSizePx = resolveCueTextSize(cue, rawViewHeight, viewHeightMinusPadding);
            SubtitlePainter painter = painters.get(i);

            painter.draw(cue, applyEmbeddedStyles, applyEmbeddedFontSizes,
                    style, defaultViewTextSizePx, cueTextSizePx,
                    bottomPaddingFraction, canvas, left, top, right, bottom);
        }
    }

    // 首先判断样式，其次判断大小，最后获取值，很合理。
    private float resolveCueTextSize(Cue cue, int rawViewHeight, int viewHeightMinusPadding) {
        if (cue.textSizeType == Cue.TYPE_UNSET || cue.textSize == Cue.DIMEN_UNSET) {
            return 0;
        }

        float defaultCueTextSizePx =
                resolveTextSize(cue.textSizeType, cue.textSize, rawViewHeight, viewHeightMinusPadding);

        return Math.max(defaultCueTextSizePx, 0);
    }

    private float resolveTextSize(@Cue.TextSizeType int textSizeType,
                                  float textSize, int rawViewHeight, int viewHeightMinusPadding) {
        switch (textSizeType) {
            case Cue.TEXT_SIZE_TYPE_ABSOLUTE:
                return textSize;
            case Cue.TEXT_SIZE_TYPE_FRACTIONAL:
                return textSize * viewHeightMinusPadding;
            case Cue.TEXT_SIZE_TYPE_FRACTIONAL_IGNORE_PADDING:
                return textSize * rawViewHeight;
            case Cue.TYPE_UNSET:
            default:
                return Cue.DIMEN_UNSET;
        }
    }
    @TargetApi(19)
    private boolean isCaptionManagerEnabled() {
        CaptioningManager captioningManager =
                (CaptioningManager) getContext().getSystemService(Context.CAPTIONING_SERVICE);
        return captioningManager.isEnabled();
    }

    @TargetApi(19)
    private float getUserCaptionFontScaleV19() {
        CaptioningManager captioningManager =
                (CaptioningManager) getContext().getSystemService(Context.CAPTIONING_SERVICE);
        return captioningManager.getFontScale();
    }

    @TargetApi(19)
    private CaptionStyleCompat getUserCaptionStyleV19() {
        CaptioningManager captioningManager =
                (CaptioningManager) getContext().getSystemService(Context.CAPTIONING_SERVICE);
        return CaptionStyleCompat.createFromCaptionStyle(captioningManager.getUserStyle());
    }
}

package com.android.live.camera.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.android.live.camera.filmstrip.FilmstripContentPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FilmstripLayout extends FrameLayout implements FilmstripContentPanel {


    public FilmstripLayout(@NonNull Context context) {
        this(context, null);
    }

    public FilmstripLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilmstripLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    public void setFilmstripListener(Listener listener) {

    }

    @Override
    public boolean animateHide() {
        return false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

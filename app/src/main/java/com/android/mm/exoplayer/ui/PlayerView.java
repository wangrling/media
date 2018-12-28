package com.android.mm.exoplayer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlayerView extends FrameLayout {


    public PlayerView(@NonNull Context context) {
        super(context);
    }

    public PlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Set the {@link Player} to use.
     *
     * @param player
     */
    public void setPlayer(SimpleExoPlayer player) {

    }
}

package com.android.live.camera.filmstrip;

import com.android.live.camera.widget.FilmstripLayout;

/**
 * The filmstrip panel holding the filmstrip and other controls/widgets.
 */
public interface FilmstripContentPanel {

    interface Listener {

    }

    /** Sets the listener. */
    void setFilmstripListener(FilmstripLayout.Listener listener);

    /**
     * Hides this panel with animation.
     *
     * @return {@code false} if already hidden.
     */
    boolean animateHide();

    /** Hides this panel */
    void hide();

    /** Shows this panel */
    void show();

    /**
     * Called when the back key is pressed.
     *
     * @return Whether the UI responded to the key event.
     */
    boolean onBackPressed();
}

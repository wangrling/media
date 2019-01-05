package com.android.live.camera.filmstrip;

/**
 * An interface which define the controller of filmstrip.
 *
 * A filmstrip has 4 states:
 * <ol>
 *     <li>Filmstrip</li>
 *     Images are scale down and the user can navigate quickly by swiping.
 *     Action bar and controls are shown.
 *     <li>Full-screen</li>
 *     One single image occupies the whole screen. Action bar and controls are hidden.
 *     <li>Zoom view</li>
 *     Zoom in to view the details of one single image.
 * </ol>
 *
 * Only the following state transitions can happen:
 * <ol>
 *     <li>filmstrip --> full-screen</li>
 *     <li>full-screen --> filmstrip</li>
 *     <li>full-screen --> full-screen with UIs</li>
 *     <li>full-screen --> zoom view</li>
 *     <li>zoom view --> full-screen</li>
 * </ol>
 */

public interface FilmstripController {


    /**
     * @return The ID of the current item, or -1.
     */
    public int getCurrentAdapterIndex();
}

package com.google.android.core;

import com.google.android.core.audio.AudioListener;

/**
 * A media player interface defining traditional high-level functionality, such as the ability to
 * play, pause, seek and query properties of the currently playing media.
 * <p>
 * Some important properties of media players that implement this interface are:
 * <ul>
 *
 * </ul>
 */

public interface Player {

    /** The audio component of a {@link Player}. */
    interface AudioComponent {


        void addAudioListener(AudioListener listener);


        void removeAudioListener(AudioListener listener);
    }
}

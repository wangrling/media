package com.android.live.concurrency.composeobject;

import android.text.method.KeyListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * VisualComponent uses a {@link CopyOnWriteArrayList} to store each listener list;
 * this is a thread-safe List implementation particularly suited for managing listener
 * lists.
 */

interface MouseListener {

}

public class VisualComponent {

    private final List<KeyListener> keyListeners
            = new CopyOnWriteArrayList<>();
    private final List<MouseListener> mouseListeners
            = new CopyOnWriteArrayList<>();

    public void addKeyListener(KeyListener listener) {
        keyListeners.add(listener);
    }

    public void addMouseListener(MouseListener listener) {
        mouseListeners.add(listener);
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.remove(listener);
    }

    public void removeMouseListener(MouseListener listener) {
        mouseListeners.remove(listener);
    }
}

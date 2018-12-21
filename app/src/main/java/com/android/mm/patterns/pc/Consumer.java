package com.android.mm.patterns.pc;

import android.media.session.MediaSession;
import android.util.Log;

import static com.android.mm.patterns.PatternsActivity.TAG;

public class Consumer {

    private final ItemQueue queue;

    private final String name;

    public Consumer(String name, ItemQueue queue) {
        this.name = name;
        this.queue = queue;
    }

    /**
     * Consume item from the queue.
     */
    public Item consume() throws InterruptedException {
        Item item = queue.take();

        return item;
    }

    public String getName() {
        return name;
    }
}

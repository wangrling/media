package com.android.mm.patterns.pc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Class as a channel for {@link Producer}-{@link Consumer} exchange.
 */
public class ItemQueue {
    private BlockingQueue<Item> queue;

    public ItemQueue() {
        queue = new LinkedBlockingDeque<>(5);
    }

    public void  put(Item item) throws InterruptedException {
        queue.put(item);
    }

    public Item take() throws InterruptedException {
        return queue.take();
    }
}

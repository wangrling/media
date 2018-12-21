package com.android.mm;

import android.util.Log;

import com.android.mm.patterns.pc.Consumer;
import com.android.mm.patterns.pc.Item;
import com.android.mm.patterns.pc.ItemQueue;
import com.android.mm.patterns.pc.Producer;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.android.mm.patterns.PatternsActivity.TAG;

public class DesignPatternsTest {

    @Test
    public void testProducerConsumer() {
        ItemQueue queue = new ItemQueue();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 2; i++) {

            final Producer producer = new Producer("Producer_" + i, queue);
            executorService.submit(() -> {
                while (true) {
                    producer.produce();
                }
            });
        }

        for (int i = 0; i < 3; i++) {
            final Consumer consumer = new Consumer("Consumer_" + i, queue);
            executorService.submit(() -> {
                while (true) {
                    Item item = consumer.consume();
                    System.out.println("Consumer [{" + consumer.getName() + "}] consume item [{" +
                            item.getId() + "}] produced by [{" + item.getProducer() + "}]");
                }
            });
        }

        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
            executorService.shutdownNow();
        } catch (InterruptedException e) {
            Log.e(TAG, "Error waiting for ExecutorService shutdown");
        }
    }
}

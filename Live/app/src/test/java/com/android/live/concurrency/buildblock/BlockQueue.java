package com.android.live.concurrency.buildblock;

import java.io.File;
import java.io.FileFilter;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Bounded queues are a powerful resource management tool for building reliable
 * applications: they make your program more robust to overload by throttling
 * activities that threaten to produce more work than can be handled.
 *
 * The class library contains several implementations of BlockingQueue . Link-
 * edBlockingQueue and ArrayBlockingQueue are FIFO queues, analogous to Link-
 * edList and ArrayList but with better concurrent performance than a synchro-
 * nized List . PriorityBlockingQueue is a priority-ordered queue, which is useful
 * when you want to process elements in an order other than FIFO. Just like other
 * sorted collections, PriorityBlockingQueue can compare elements according to
 * their natural order (if they implement Comparable ) or using a Comparator.
 *
 * The last BlockingQueue implementation, SynchronousQueue , is not really a
 * queue at all, in that it maintains no storage space for queued elements. Instead,
 * it maintains a list of queued threads waiting to enqueue or dequeue an element.
 * In the dish-washing analogy, this would be like having no dish rack, but instead
 * handing the washed dishes directly to the next available dryer. While this may
 * seem a strange way to implement a queue, it reduces the latency associated with
 * moving data from producer to consumer because the work can be handed off
 * directly.
 *
 * {@link Deque} and {@link BlockingDeque}, that extend Queue and BlockingQueue. A Deque
 * is a double-ended queue that allows efficient insertion and removal from bot the
 * head and the tail. Implementations include ArrayDeque and LinkedBlockingDeque.
 *
 * 每个线程都保持自己的Deque，自己线程的数据消耗完成，会去其它线程那里拿数据。
 */

public class BlockQueue {

    private static final int N_COMSUMERS = 10;

    public static void startIndexing(File[] roots){
        BlockingQueue<File> queue = new LinkedBlockingDeque<>(1000);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        };

        for (File root : roots) {
            new Thread(new FileCrawler(queue, filter, root)).start();
        }

        for (int i = 0; i < N_COMSUMERS; i++) {
            new Thread(new Indexer(queue)).start();
        }
    }
}

// 生产者
class FileCrawler implements Runnable {
    private BlockingQueue<File> fileQueue;
    private FileFilter fileFilter;
    private File root;

    // 构造器
    public FileCrawler(BlockingQueue<File> queue, FileFilter filter, File root) {
        this.fileQueue = queue;
        this.fileFilter = filter;
        this.root = root;
    }


    @Override
    public void run() {
        try {
            crawl(root);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void crawl(File root) throws InterruptedException {
        File[] entries = root.listFiles(fileFilter);
        if (entries != null) {
            for (File entry : entries) {
                if (entry.isDirectory())
                    crawl(entry);
                else if (!alreadyIndexed(entry))
                    fileQueue.put(entry);
            }
        }
    }

    private boolean alreadyIndexed(File entry) {
        return false;
    }
}

// 消费者
class Indexer implements Runnable {
    private final BlockingQueue<File> queue;

    public Indexer(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                indexFile(queue.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void indexFile(File file) {

    }
}


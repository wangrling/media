package com.android.mm.algs.structures;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CircularBuffer {
    private char[] buffer;
    private final int bufferSize;
    private int writeIndex = 0;
    private int readIndex = 0;

    // 原子操作
    private AtomicInteger readableData = new AtomicInteger(0);

    public CircularBuffer(int bufferSize) {
        if (!isPowerOfTwo(bufferSize)) {
            throw new IllegalArgumentException();
        }
        this.bufferSize = bufferSize;
        buffer = new char[bufferSize];
    }

    // 经典
    private boolean isPowerOfTwo(int i) {
        return (i & (i - 1)) == 0;
    }

    private int getTrueIndex(int i) {
        return i % bufferSize;
    }

    public boolean writeToCharBuffer(char c) {
        boolean result = false;

        // if we can write to the buffer.
        if (readableData.get() < bufferSize) {
            // write to buffer.
            buffer[getTrueIndex(writeIndex)] = c;
            readableData.incrementAndGet();
            writeIndex++;
            result = true;
        }
        return result;
    }

    public static class WriteWorker implements Runnable {
        String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
        CircularBuffer buffer;
        Random random = new Random();

        public WriteWorker(CircularBuffer cb) {
            this.buffer = cb;
        }

        private char getRandomChar() {
            return alphabet.charAt(random.nextInt(alphabet.length()));
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                if (!buffer.writeToCharBuffer(getRandomChar())) {
                    Thread.yield();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        return ;
                    }
                }
            }
        }
    }

    private Character readOutChar() {
        Character result = null;

        // if we have data to read
        if (readableData.get() > 0) {
            result = new Character(buffer[getTrueIndex(readIndex)]);
            readableData.decrementAndGet();
            readIndex++;
        }
        return result;
    }

    public static class ReadWorker implements Runnable {
        CircularBuffer buffer;
        public ReadWorker(CircularBuffer cb) {
            this.buffer = cb;
        }

        @Override
        public void run() {
            System.out.println("Printing Buffer:");
            while (!Thread.interrupted()) {
                Character c = buffer.readOutChar();
                if (c != null) {
                    System.out.print(c.charValue());
                } else {
                    Thread.yield();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println();
                        return;
                    }
                }
            }
        }
    }
}

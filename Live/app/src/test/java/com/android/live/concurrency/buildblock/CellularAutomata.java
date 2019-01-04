package com.android.live.concurrency.buildblock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Barriers are similar to latches in that they block a group of threads until some
 * event has occurred. The key difference is that with a barrier, all the
 * threads must come together at a barrier point at the same time in order to proceed.
 * Latches are for waiting for events;
 *
 * Everyone meet at McDonald’s at 6:00; once you get there, stay there
 * until everyone shows up, and then we’ll figure out what we’re doing next.
 */

public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;
    public CellularAutomata(Board board) {
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count,
                new Runnable() {
                    public void run() {
                        mainBoard.commitNewValues();
                    }});
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++)
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
    }
    private class Worker implements Runnable {
        private final Board board;
        public Worker(Board board) { this.board = board; }
        public void run() {
            while (!board.hasConverged()) {
                for (int x = 0; x < board.getMaxX(); x++)
                    for (int y = 0; y < board.getMaxY(); y++)
                        board.setNewValue(x, y, computeValue(x, y));
                try {
                    barrier.await();
                } catch (InterruptedException ex) {
                    return;
                } catch (BrokenBarrierException ex) {
                    return;
                }
            }
        }
    }
    public void start() {
        for (int i = 0; i < workers.length; i++)
            new Thread(workers[i]).start();
        mainBoard.waitForConvergence();
    }

    private int computeValue(int x, int y) {
        return x + y;
    }
}

class Board {

    public void commitNewValues() {
        
    }

    public void waitForConvergence() {

    }

    public Board getSubBoard(int count, int i) {
        return null;
    }

    public boolean hasConverged() {
        return false;
    }

    public int getMaxX() {
        return 0;
    }

    public int getMaxY() {
        return 0;
    }

    public void setNewValue(int x, int y, int i) {

    }
}

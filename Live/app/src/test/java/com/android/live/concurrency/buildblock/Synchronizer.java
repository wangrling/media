package com.android.live.concurrency.buildblock;

/**
 * A synchronizer is any object that coordinates the control flow of threads based
 * on its state. Blocking queues can act as synchronizers; other types of synchronizers
 * include semaphores, barriers, and latches.
 *
 * 看门狗
 * A latch is a synchronizer that can delay the progress of threads until it reaches
 * its terminal state.
 *
 * Once the latch reaches the　terminal state, it cannot change state again,
 * so it remains open forever.
 *
 */
public class Synchronizer {


}

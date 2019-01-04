package com.android.live.concurrency.tasksexection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Whenever you see code of the form:
 *      new Thread(runnable).start();
 * and you think you might at some point want a more flexible execution
 * policy, seriously consider replacing it with the use of an Executor.
 */

public class TaskExecutionWebServer {

    private static final int NTHREADS = 100;

    private static final Executor exec =
            Executors.newFixedThreadPool(NTHREADS);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleRequest(connection);
                }
            };
            exec.execute(task);
        }
    }

    private static void handleRequest(Socket connection) {

    }
}

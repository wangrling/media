package com.android.live.concurrency.buildblock;

import com.google.common.util.concurrent.ForwardingExecutorService;

import org.junit.runner.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class LifecycleWebServer {

    private final ExecutorService exec = new ForwardingExecutorService() {
        @Override
        protected ExecutorService delegate() {
            return null;
        }
    };

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!exec.isShutdown()) {
            final Socket conn = socket.accept();
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    handleRequest(conn);
                }
            });
        }
    }

    void handleRequest(Socket connection) {
        Request req = readRequest(connection);
        if (isShutdownRequest(req))
            stop();
        else
            dispatchRequest(req);
    }

    private void dispatchRequest(Request req) {

    }

    private void stop() {
        exec.shutdown();
    }

    private boolean isShutdownRequest(Request req) {
        return false;
    }

    private Request readRequest(Socket connection) {
        return null;
    }
}

package org.example.connection.state.handler.limiter;

import java.io.IOException;
import java.io.OutputStream;

public class ThrottledOutputStream extends OutputStream {
    private final OutputStream outputStream;
    private final int userSpeedLimit;
    private final int globalSpeedLimit;
    private long bytesWritten = 0;
    private final long startTime = System.nanoTime();

    public ThrottledOutputStream(OutputStream outputStream, int userSpeedLimit, int globalSpeedLimit) {
        this.outputStream = outputStream;
        this.userSpeedLimit = userSpeedLimit;
        this.globalSpeedLimit = globalSpeedLimit;
    }

    @Override
    public void write(int b) throws IOException {
        throttle();
        outputStream.write(b);
        bytesWritten++;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        throttle();
        outputStream.write(b, off, len);
        bytesWritten += len;
    }

    private void throttle() throws IOException {
        long elapsedTime = System.nanoTime() - startTime;
        long expectedTime = (bytesWritten * 1_000_000_000L) / Math.min(userSpeedLimit, globalSpeedLimit);

        if (elapsedTime < expectedTime) {
            long sleepTime = (expectedTime - elapsedTime) / 1_000_000L;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new IOException("Interrupted during throttling", e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}


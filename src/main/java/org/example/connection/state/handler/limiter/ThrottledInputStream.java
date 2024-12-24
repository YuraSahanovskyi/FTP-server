package org.example.connection.state.handler.limiter;

import java.io.IOException;
import java.io.InputStream;

public class ThrottledInputStream extends InputStream {
    private final InputStream inputStream;
    private final int userSpeedLimit;
    private long bytesRead = 0;
    private final long startTime = System.nanoTime();

    public ThrottledInputStream(InputStream inputStream, int userSpeedLimit) {
        this.inputStream = inputStream;
        this.userSpeedLimit = userSpeedLimit;
    }

    @Override
    public int read() throws IOException {
        throttle();
        int byteRead = inputStream.read();
        if (byteRead != -1) {
            bytesRead++;
        }
        return byteRead;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throttle();
        int bytesToRead = Math.min(len, userSpeedLimit);
        int bytesReadNow = inputStream.read(b, off, bytesToRead);
        if (bytesReadNow != -1) {
            bytesRead += bytesReadNow;
        }
        return bytesReadNow;
    }

    private void throttle() throws IOException {
        long elapsedTime = System.nanoTime() - startTime;
        long expectedTime = (bytesRead * 1_000_000_000L) / userSpeedLimit;

        if (expectedTime > elapsedTime) {
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
        inputStream.close();
    }
}

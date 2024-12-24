package org.example.connection.state.handler.limiter;

import java.io.IOException;
import java.io.OutputStream;

public class ThrottledOutputStream extends OutputStream {
    private final OutputStream outputStream;
    private final int userSpeedLimit; // обмеження швидкості в байтах на секунду
    private long bytesWritten = 0;
    private final long startTime = System.nanoTime();

    public ThrottledOutputStream(OutputStream outputStream, int userSpeedLimit) {
        this.outputStream = outputStream;
        this.userSpeedLimit = userSpeedLimit;
    }

    @Override
    public void write(int b) throws IOException {
        throttle(1); // одна операція запису
        outputStream.write(b);
        bytesWritten++;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        throttle(len); // кількість байт, яку будемо записувати
        outputStream.write(b, off, len);
        bytesWritten += len;
    }

    // Функція для контролю швидкості
    private void throttle(int len) throws IOException {
        long elapsedTime = System.nanoTime() - startTime;
        long expectedTime = ((bytesWritten + len) * 1_000_000_000L) / userSpeedLimit; // очікуваний час для запису всіх байт

        // Якщо не досягнута потрібна швидкість, додаємо затримку
        if (elapsedTime < expectedTime) {
            long sleepTime = (expectedTime - elapsedTime) / 1_000_000L; // перерва між записами в мілісекундах
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

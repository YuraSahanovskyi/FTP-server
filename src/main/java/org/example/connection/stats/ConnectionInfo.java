package org.example.connection.stats;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConnectionInfo {
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private long bytesSent;
    private long bytesReceived;

    public ConnectionInfo() {
        this.startTime = LocalDateTime.now();
        this.bytesSent = 0;
        this.bytesReceived = 0;
    }

    public void endConnection() {
        this.endTime = LocalDateTime.now();
    }

    public void addBytesSent(long bytes) {
        this.bytesSent += bytes;
    }

    public void addBytesReceived(long bytes) {
        this.bytesReceived += bytes;
    }

    @Override
    public String toString() {
        return "ConnectionInfo{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", bytesSent=" + bytesSent +
                ", bytesReceived=" + bytesReceived +
                '}';
    }
}

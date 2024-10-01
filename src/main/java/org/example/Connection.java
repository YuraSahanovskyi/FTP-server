package org.example;

import java.time.LocalDateTime;

public class Connection {
    public Connection(User user, LocalDateTime startTime, LocalDateTime endTime) {
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}

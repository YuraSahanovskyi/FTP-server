package org.example.builder;

import java.util.Objects;

public class Permission {
    private String path;
    private boolean read;
    private boolean write;
    private boolean execute;

    public Permission(String path, boolean read, boolean write, boolean execute) {
        this.path = path;
        this.read = read;
        this.write = write;
        this.execute = execute;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isExecute() {
        return execute;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission that)) return false;
        return isRead() == that.isRead() && isWrite() == that.isWrite() && isExecute() == that.isExecute() && Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath(), isRead(), isWrite(), isExecute());
    }

    @Override
    public String toString() {
        return "Permission{" +
                "path='" + path + '\'' +
                ", read=" + read +
                ", write=" + write +
                ", execute=" + execute +
                '}';
    }
}

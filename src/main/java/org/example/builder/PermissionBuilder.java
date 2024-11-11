package org.example.builder;

public class PermissionBuilder {
    private String path;
    private boolean read;
    private boolean write;
    private boolean execute;

    public void reset() {
        this.path = null;
        this.read = false;
        this.write = false;
        this.execute = false;
    }

    public PermissionBuilder setPath(String path) {
        this.path = path;
        return this;
    }
    public PermissionBuilder setRead(boolean read) {
        this.read = read;
        return this;
    }
    public PermissionBuilder setWrite(boolean write) {
        this.write = write;
        return this;
    }
    public PermissionBuilder setExecute(boolean execute) {
        this.execute = execute;
        return this;
    }
    public Permission build() {
        return new Permission(path, read, write, execute);
    }
}

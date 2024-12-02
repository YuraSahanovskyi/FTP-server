package org.example;

import java.util.Set;

public class AuthenticatedUser extends User {
    public AuthenticatedUser(String username, String password, Set<Permission> permissions) {
        super(username, password, permissions);
    }
    private String currentDirectory = System.getProperty("user.dir");
    public String getCurrentDirectory() {
        return currentDirectory;
    }
    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }
    public static class CurrentDirectoryMemento {
        private final String path;
        public CurrentDirectoryMemento(String path) {
            this.path = path;
        }
        public String getCurrentDirectory() {
            return path;
        }
    }
    public void restore(CurrentDirectoryMemento currentDirectoryMemento) {
        this.currentDirectory = currentDirectoryMemento.path;
    }
    public CurrentDirectoryMemento save() {
        return new CurrentDirectoryMemento(currentDirectory);
    }

}

package org.example.authentication;

import org.example.user.Permission;
import org.example.user.User;

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
    public static class CurrentDirectoryMemento implements UserMemento {
        private final String path;
        public CurrentDirectoryMemento(String path) {
            this.path = path;
        }
    }
    public void restore(UserMemento userMemento) {
        this.currentDirectory = ((CurrentDirectoryMemento) userMemento).path;
    }
    public CurrentDirectoryMemento save() {
        return new CurrentDirectoryMemento(currentDirectory);
    }

}

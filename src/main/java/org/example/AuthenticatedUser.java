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
}

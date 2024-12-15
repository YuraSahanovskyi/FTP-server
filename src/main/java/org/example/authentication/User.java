package org.example.authentication;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class User {
    private Integer id;
    private String username;
    private String password;
    private String currentDirectory;
    private int speedLimit;
    private Set<Permission> permissions;

    public static class CurrentDirectoryMemento implements UserMemento {
        private final String path;
        public CurrentDirectoryMemento(String path) {
            this.path = path;
        }
    }
    public void restore(UserMemento userMemento) {
        this.currentDirectory = ((CurrentDirectoryMemento) userMemento).path;
    }
    public UserMemento save() {
        return new CurrentDirectoryMemento(currentDirectory);
    }
}

package org.example.builder;

import java.util.Set;

public class UserBuilder {
    private String username;
    private String password;
    private Set<Permission> permissions;

    public void reset() {
        username = null;
        password = null;
        permissions = null;
    }
    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }
    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }
    public UserBuilder addPermissions(Permission permission) {
        this.permissions.add(permission);
        return this;
    }
    public User build() {
        return new User(username, password, permissions);
    }
}

package org.example.user;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private String username;
    private String password;
    private Set<Permission> permissions;

    public User(String username, String password, Set<Permission> permissions) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
    }

    public static class Builder {
        private String username;
        private String password;
        private Set<Permission> permissions;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }
        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }
        public Builder withPermission(Permission permission) {
            if (this.permissions == null) {
                this.permissions = new HashSet<>();
            }
            this.permissions.add(permission);
            return this;
        }
        public User build() {
            return new User(username, password, permissions);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getPermissions(), user.getPermissions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getPermissions());
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}

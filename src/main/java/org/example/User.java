package org.example;

public class User {
    public User(String password, String name, int id, Role role) {
        this.password = password;
        this.name = name;
        this.id = id;
        this.role = role;
    }

    private int id;
    private String name;
    private String password;
    private Role role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

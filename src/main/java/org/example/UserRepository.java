package org.example;

import org.example.builder.PermissionBuilder;
import org.example.builder.User;
import org.example.builder.UserBuilder;

import java.util.HashSet;
import java.util.Set;

public class UserRepository {
    private final Set<User> users = new HashSet<>();

    public UserRepository() {
        // Predefined users
        UserBuilder userBuilder = new UserBuilder();
        PermissionBuilder permissionBuilder = new PermissionBuilder();
        String[] usernames = {"user1", "user2", "user3", "user4", "user5"};
        String[] passwords = {"password1", "password2", "password3", "password4", "password5"};
        for (int i = 0; i < usernames.length; i++) {
            userBuilder.reset();
            users.add(userBuilder
                    .setUsername(usernames[i])
                    .setPassword(passwords[i])
                    .build());
        }
    }

    public boolean isExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }
    public String getPassword(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .map(User::getPassword)
                .orElse(null);
    }
}

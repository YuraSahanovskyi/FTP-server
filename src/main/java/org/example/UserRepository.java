package org.example;

import java.util.HashSet;
import java.util.Set;

public class UserRepository {
    private final Set<User> users = new HashSet<>();

    public UserRepository() {
        // Predefined users
        String[] usernames = {"user1", "user2", "user3", "user4", "user5"};
        String[] passwords = {"password1", "password2", "password3", "password4", "password5"};
        for (int i = 0; i < usernames.length; i++) {
            users.add(User.builder()
                            .withUsername(usernames[i])
                            .withPassword(passwords[i])
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

package org.example;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final  Map<String, String> users = new HashMap<>();

    public UserRepository() {
        // Predefined users
        users.put("user1", "password1");
        users.put("user2", "password2");
    }

    public boolean contains(String username) {
        return users.containsKey(username);
    }
    public String getPassword(String username) {
        return users.get(username);
    }
}

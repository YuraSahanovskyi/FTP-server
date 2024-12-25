package org.example.authentication;

import org.example.database.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class Authenticator {
    private boolean isExists(String username, String password) {
        User user;
        try {
            user = UserRepository.getUserByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public User authenticate(String line, BufferedReader in, PrintWriter out) throws IOException {
        if (line.startsWith("USER")) {
            String username = line.split(" ")[1];
            out.println("331 Username okay, need password");

            line = in.readLine();
            if (line.startsWith("PASS")) {
                String password = line.split(" ")[1];
                if (this.isExists(username, password)) {
                    out.println("230 User logged in, proceed");
                    try {
                        return UserRepository.getUserByUsername(username);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    out.println("530 Not logged in");
                    return null;
                }
            }
        } else {
            out.println("530 Not logged in");
        }
        return null;
    }
}
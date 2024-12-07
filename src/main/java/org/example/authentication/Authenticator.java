package org.example.authentication;

import org.example.user.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Authenticator {
    private final UserRepository userRepository;

    public Authenticator() {
        this.userRepository = new UserRepository();
    }

    private boolean isExists(String username, String password) {
        return userRepository.isExists(username) && userRepository.getPassword(username).equals(password);
    }

    public AuthenticatedUser authenticate(String line, BufferedReader in, PrintWriter out) throws IOException {
        if (line.startsWith("USER")) {
            String username = line.split(" ")[1];
            out.println("331 Username okay, need password");

            line = in.readLine();
            if (line.startsWith("PASS")) {
                String password = line.split(" ")[1];
                if (this.isExists(username, password)) {
                    out.println("230 User logged in, proceed");
                    return new AuthenticatedUser(username, password, userRepository.getPermissions(username));
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
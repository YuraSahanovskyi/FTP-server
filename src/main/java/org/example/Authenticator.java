package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Authenticator {
    private final UserRepository userRepository;

    public Authenticator() {
        this.userRepository = new UserRepository();
    }

    private boolean authenticate(String username, String password) {
        return userRepository.contains(username) && userRepository.getPassword(username).equals(password);
    }

    public boolean authenticate(String line, BufferedReader in, PrintWriter out) throws IOException {
        if (line.startsWith("USER")) {
            String username = line.split(" ")[1];
            out.println("331 Username okay, need password");

            line = in.readLine();
            if (line.startsWith("PASS")) {
                String password = line.split(" ")[1];
                if (this.authenticate(username, password)) {
                    out.println("230 User logged in, proceed");
                    return true;
                } else {
                    out.println("530 Not logged in");
                    return false;
                }
            }
        } else {
            out.println("530 Not logged in");
        }
        return false;
    }
}
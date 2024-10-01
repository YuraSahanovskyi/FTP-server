package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server(UserManager userManager) {
        this.userManager = userManager;
    }

    private UserManager userManager;
    private final int PORT = 21;

    public void start() {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    // handle requests
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}

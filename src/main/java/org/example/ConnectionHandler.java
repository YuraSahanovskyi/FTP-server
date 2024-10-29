package org.example;

import org.example.states.ConnectionContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private final Socket clientSocket;
    private ConnectionContext connectionContext;

    public ConnectionHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("220 Welcome to Simple FTP Server");

            connectionContext = new ConnectionContext(clientSocket, in, out, System.getProperty("user.dir"));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);
                connectionContext.handleCommand(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connectionContext.closeDataSockets();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
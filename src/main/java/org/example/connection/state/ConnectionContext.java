package org.example.connection.state;

import lombok.Getter;
import lombok.Setter;
import org.example.authentication.User;
import org.example.authentication.UserMemento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ConnectionContext {
    @Setter
    private ConnectionState state;
    @Getter
    private final PrintWriter out;
    @Getter
    private final BufferedReader in;
    @Setter
    @Getter
    private User user;
    @Setter
    @Getter
    private ServerSocket dataServerSocket;
    @Setter
    @Getter
    private Socket dataSocket;
    @Getter
    private final Socket clientSocket;
    @Getter
    private final int globalSpeedLimit;
    static Map<String, UserMemento> history = new HashMap<>();

    public ConnectionContext(Socket clientSocket, BufferedReader in, PrintWriter out, int globalSpeedLimit) {
        this.clientSocket = clientSocket;
        this.in = in;
        this.out = out;
        this.globalSpeedLimit = globalSpeedLimit;
        this.state = new UnauthenticatedState(this);
    }

    public void start() throws IOException {
        out.println("220 Welcome to Simple FTP Server");
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println("Received: " + line);
            handleCommand(line);
        }
    }

    public void handleCommand(String line) throws IOException {
        this.state.handleCommand(line);
    }

    public String getCurrentDirectory() {
        return user.getCurrentDirectory();
    }

    public void setCurrentDirectory(String currentDirectory) {
        this.user.setCurrentDirectory(currentDirectory);
    }

    public void closeDataSockets() {
        try {
            if (dataSocket != null && !dataSocket.isClosed()) {
                dataSocket.close();
            }
            if (dataServerSocket != null && !dataServerSocket.isClosed()) {
                dataServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveHistory() {
        history.put(user.getUsername(), user.save());
    }
    public void restoreHistory() {
        UserMemento userMemento = history.get(user.getUsername());
        if (userMemento != null) {
            user.restore(history.get(user.getUsername()));
        }
    }
}
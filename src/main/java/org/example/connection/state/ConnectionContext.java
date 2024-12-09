package org.example.connection.state;

import org.example.authentication.AuthenticatedUser;
import org.example.authentication.UserMemento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ConnectionContext {
    private ConnectionState state;
    private final PrintWriter out;
    private final BufferedReader in;
    private AuthenticatedUser user;
    private ServerSocket dataServerSocket;
    private Socket dataSocket;
    private final Socket clientSocket;
    static Map<String, UserMemento> history = new HashMap<>();

    public ConnectionContext(Socket clientSocket, BufferedReader in, PrintWriter out) {
        this.clientSocket = clientSocket;
        this.in = in;
        this.out = out;
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

    public void setState(ConnectionState state) {
        this.state = state;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public AuthenticatedUser getUser() {
        return user;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public String getCurrentDirectory() {
        return user.getCurrentDirectory();
    }

    public void setCurrentDirectory(String currentDirectory) {
        this.user.setCurrentDirectory(currentDirectory);
    }

    public ServerSocket getDataServerSocket() {
        return dataServerSocket;
    }

    public void setDataServerSocket(ServerSocket dataServerSocket) {
        this.dataServerSocket = dataServerSocket;
    }

    public Socket getDataSocket() {
        return dataSocket;
    }

    public void setDataSocket(Socket dataSocket) {
        this.dataSocket = dataSocket;
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
package org.example.states;

import org.example.AuthenticatedUser;
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
    static Map<String, AuthenticatedUser.CurrentDirectoryMemento> history = new HashMap<>();

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

    void setState(ConnectionState state) {
        this.state = state;
    }

    Socket getClientSocket() {
        return clientSocket;
    }

    PrintWriter getOut() {
        return out;
    }

    BufferedReader getIn() {
        return in;
    }

    AuthenticatedUser getUser() {
        return user;
    }

    void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    String getCurrentDirectory() {
        return user.getCurrentDirectory();
    }

    void setCurrentDirectory(String currentDirectory) {
        this.user.setCurrentDirectory(currentDirectory);
    }

    ServerSocket getDataServerSocket() {
        return dataServerSocket;
    }

    void setDataServerSocket(ServerSocket dataServerSocket) {
        this.dataServerSocket = dataServerSocket;
    }

    Socket getDataSocket() {
        return dataSocket;
    }

    void setDataSocket(Socket dataSocket) {
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
    void saveHistory() {
        history.put(user.getUsername(), user.save());
    }
    void restoreHistory() {
        AuthenticatedUser.CurrentDirectoryMemento currentDirectoryMemento = history.get(user.getUsername());
        if (currentDirectoryMemento != null) {
            user.restore(history.get(user.getUsername()));
        }
    }
}
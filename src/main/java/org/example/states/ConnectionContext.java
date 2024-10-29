package org.example.states;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionContext {
    private ConnectionState state;
    private final PrintWriter out;
    private final BufferedReader in;
    private String currentDirectory;
    private ServerSocket dataServerSocket;
    private Socket dataSocket;
    private final Socket clientSocket;

    public ConnectionContext(Socket clientSocket, BufferedReader in, PrintWriter out, String currentDirectory) {
        this.clientSocket = clientSocket;
        this.in = in;
        this.out = out;
        this.currentDirectory = currentDirectory;
        this.state = new UnauthenticatedState(this);
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

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
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
}
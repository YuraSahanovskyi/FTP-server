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

    String getCurrentDirectory() {
        return currentDirectory;
    }

    void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
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
}
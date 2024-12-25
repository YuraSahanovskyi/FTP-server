package org.example.connection.state;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class WaitingCommandState implements ConnectionState {
    private final ConnectionContext context;

    public WaitingCommandState(ConnectionContext context) {
        this.context = context;
    }

    private void handlePASV() throws IOException {
        if (context.getDataServerSocket() != null) {
            context.getDataServerSocket().close();
        }
        context.setDataServerSocket(new ServerSocket(context.getDataPort()));
        int port = context.getDataServerSocket().getLocalPort();
        int p1 = port / 256;
        int p2 = port % 256;
        context.getOut().println("227 Entering Passive Mode (127,0,0,1," + p1 + "," + p2 + ")");
        context.setDataSocket(context.getDataServerSocket().accept());
    }

    private void handlePORT(String line) throws IOException {
        if (context.getDataSocket() != null) {
            context.getDataSocket().close();
        }
        String[] parts = line.substring(5).split(",");
        String ip = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
        int port = Integer.parseInt(parts[4]) * 256 + Integer.parseInt(parts[5]);
        context.setDataSocket(new Socket(ip, port));
        context.getOut().println("200 Command okay.");
    }

    @Override
    public void handleCommand(String line) throws IOException {
        String command = line.split(" ")[0].toUpperCase();
        switch (command) {
            case "PASV":
                handlePASV();
                break;
            case "PORT":
                handlePORT(line);
                break;
            case "PWD", "CWD", "LIST", "STOR", "RETR", "MKD", "DELE":
                context.setState(new FileOperationsState(context));
                context.handleCommand(line);
                break;
            case "QUIT":
                context.getConnectionInfo().endConnection();
                context.getOut().println("221 Service closing control connection");
                break;
            default:
                context.getOut().println("502 Command not implemented");
                break;

        }
    }
}
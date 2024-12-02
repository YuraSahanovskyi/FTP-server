package org.example.connection;

import org.example.connection.state.ConnectionContext;
import org.example.connection.builder.ConnectionContextBuilder;
import org.example.connection.builder.Director;
import java.io.IOException;
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
            ConnectionContextBuilder builder = new ConnectionContextBuilder();
            Director director = new Director();
            director.buildConnectionContext(builder, clientSocket);
            connectionContext = builder.build();
            connectionContext.start();
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
package org.example.connection;

import org.example.connection.state.ConnectionContext;
import org.example.connection.builder.ConnectionContextBuilder;
import org.example.connection.builder.Director;
import org.example.database.ConnectionStatsRepository;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ConnectionHandler implements Runnable {
    private final Socket clientSocket;
    private final int dataPort;
    private final int globalSpeedLimit;
    private ConnectionContext connectionContext;

    public ConnectionHandler(Socket socket, int dataPort, int globalSpeedLimit) {
        this.clientSocket = socket;
        this.dataPort = dataPort;
        this.globalSpeedLimit = globalSpeedLimit;
    }

    @Override
    public void run() {
        try {
            ConnectionContextBuilder builder = new ConnectionContextBuilder();
            Director director = new Director();
            director.buildConnectionContext(builder, clientSocket, globalSpeedLimit, dataPort);
            connectionContext = builder.build();
            connectionContext.start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connectionContext.closeDataSockets();
                ConnectionStatsRepository.saveConnectionInfo(connectionContext.getConnectionInfo());
                System.out.println(connectionContext.getConnectionInfo());
                clientSocket.close();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
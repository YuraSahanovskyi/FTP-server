package org.example;

import org.example.database.DatabaseConnection;
import org.example.connection.ConnectionHandler;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int THREAD_POOL_SIZE = 10;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        int controlPort = getIntEnv("CONTROL_PORT");
        int dataPort = getIntEnv("DATA_PORT");
        int globalSpeedLimit = getIntEnv("GLOBAL_SPEED_LIMIT");

        try {
            Connection connection = DatabaseConnection.getConnection();
            System.out.println("Connected to database" + connection.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (ServerSocket serverSocket = new ServerSocket(controlPort)) {
            System.out.println("FTP Server started on port " + controlPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ConnectionHandler(clientSocket, dataPort, globalSpeedLimit));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    private static int getIntEnv(String envName) {
        String envStr = System.getenv(envName);
        int env;
        if (envStr != null) {
            try {
                env = Integer.parseInt(envStr);
            } catch (NumberFormatException e) {
                System.out.println(envName + " is not an integer: " + envStr);
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Environment variable " + envName + " is not set");
            throw new RuntimeException("Environment variable " + envName + " is not set");
        }
        return env;
    }

}

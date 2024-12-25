package org.example;

import org.example.database.DatabaseConnection;
import org.example.connection.ConnectionHandler;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final AtomicInteger activeConnections = new AtomicInteger(0);

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        int controlPort = getIntEnv("CONTROL_PORT");
        int dataPort = getIntEnv("DATA_PORT");
        int globalSpeedLimit = getIntEnv("GLOBAL_SPEED_LIMIT");
        int maxConnections = getIntEnv("MAX_CONNECTIONS");
        int treadPoolSize = getIntEnv("THREAD_POOL_SIZE");
        ExecutorService threadPool = Executors.newFixedThreadPool(treadPoolSize);

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

                if (activeConnections.get() < maxConnections) {
                    activeConnections.incrementAndGet();
                    threadPool.submit(new ConnectionHandler(clientSocket, dataPort, globalSpeedLimit, activeConnections));
                } else {
                    System.out.println("Max connections reached. Rejecting new connection from " + clientSocket.getRemoteSocketAddress());
                    clientSocket.close();
                }
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

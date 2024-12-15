package org.example;

import org.example.authentication.database.DatabaseConnection;
import org.example.connection.ConnectionHandler;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT = 2121;
    private static final int THREAD_POOL_SIZE = 10;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
//            Class.forName("org.postgresql.Driver");
            Connection connection = DatabaseConnection.getConnection();
            System.out.println("Connected to database" + connection.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("FTP Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ConnectionHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}

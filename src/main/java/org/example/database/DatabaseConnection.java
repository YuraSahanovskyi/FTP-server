package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://" + System.getenv("DATABASE_HOST") + ":" + System.getenv("DATABASE_PORT") + "/" + System.getenv("DATABASE_NAME");
    private static final String USER = System.getenv("DATABASE_USER");
    private static final String PASSWORD = System.getenv("DATABASE_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

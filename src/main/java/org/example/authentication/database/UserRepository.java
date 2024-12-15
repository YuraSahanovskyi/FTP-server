package org.example.authentication.database;

import org.example.authentication.User;

import java.sql.*;

public class UserRepository {

    // Метод для отримання користувача за username
    public static User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password, working_dir, speed_limit FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return User.builder()
                            .id(resultSet.getInt("id"))
                            .username(resultSet.getString("username"))
                            .password(resultSet.getString("password"))
                            .speedLimit(resultSet.getInt("speed_limit"))
                            .currentDirectory(resultSet.getString("working_dir"))
                            .build();
                } else {
                    return null; // Користувач не знайдений
                }
            }
        }
    }

    public static boolean createUser(String username, String password, String workingDir, int speedLimit) throws SQLException {
        String sql = "INSERT INTO users (username, password, working_dir, speed_limit) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password); // У реальній практиці не забувайте хешувати пароль
            statement.setString(3, workingDir);
            statement.setInt(4, speedLimit);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Якщо рядок було додано успішно
        }
    }

    public static boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Якщо рядок було видалено успішно
        }
    }
}

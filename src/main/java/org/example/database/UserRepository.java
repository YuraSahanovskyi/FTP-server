package org.example.database;

import org.example.authentication.Permission;
import org.example.authentication.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserRepository {
    public static User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT u.id, u.username, u.password, u.working_dir, u.speed_limit, " +
                "p.id AS permission_id, p.path, p.read, p.write, p.execute " +
                "FROM users u " +
                "LEFT JOIN permissions p ON u.id = p.user_id " +
                "WHERE u.username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                User user = null;
                Set<Permission> permissions = new HashSet<>();

                while (resultSet.next()) {
                    if (user == null) {
                        user = User.builder()
                                .id(resultSet.getInt("id"))
                                .username(resultSet.getString("username"))
                                .password(resultSet.getString("password"))
                                .speedLimit(resultSet.getInt("speed_limit"))
                                .currentDirectory(resultSet.getString("working_dir"))
                                .permissions(permissions)
                                .build();
                    }

                    int permissionId = resultSet.getInt("permission_id");
                    if (permissionId != 0) {
                        Permission permission = new Permission();
                        permission.setId(permissionId);
                        permission.setPath(resultSet.getString("path"));
                        permission.setRead(resultSet.getBoolean("read"));
                        permission.setWrite(resultSet.getBoolean("write"));
                        permission.setExecute(resultSet.getBoolean("execute"));
                        permission.setUser(user);

                        permissions.add(permission);
                    }
                }

                return user;
            }
        }
    }
}

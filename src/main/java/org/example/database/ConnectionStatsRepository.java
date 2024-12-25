package org.example.database;

import org.example.connection.stats.ConnectionInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionStatsRepository {
    private static final String INSERT_SQL = "INSERT INTO connection_stats (start_time, end_time, bytes_sent, bytes_received) VALUES (?, ?, ?, ?)";

    public static void saveConnectionInfo(ConnectionInfo connectionInfo) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setTimestamp(1, java.sql.Timestamp.valueOf(connectionInfo.getStartTime()));
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(connectionInfo.getEndTime()));
            statement.setLong(3, connectionInfo.getBytesSent());
            statement.setLong(4, connectionInfo.getBytesReceived());

            statement.executeUpdate();
        }
    }
}

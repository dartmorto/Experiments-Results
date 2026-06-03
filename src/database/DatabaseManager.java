package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    public static Connection getConnection() throws SQLException {
        String url = Config.get("db.url");
        String user = Config.get("db.user");
        String password = Config.get("db.password");

        if (url == null || url.isBlank()) {
            throw new SQLException("Не указан db.url");
        }

        return DriverManager.getConnection(url, user, password);
    }
}

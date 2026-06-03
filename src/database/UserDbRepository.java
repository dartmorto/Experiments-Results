package database;

import user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDbRepository {

    public void saveAll(Connection connection, Collection<User> users) throws SQLException {
        for (User user : users) {
            save(connection, user);
        }
    }

    public void save(Connection connection, User user) throws SQLException {
        String sql = SQLLoader.load("users/save.sql");

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPasswordHash());
            statement.executeUpdate();
        }
    }

    public void saveIfNotExists(Connection connection, String username) throws SQLException {
        String sql = SQLLoader.load("users/save_if_not_exists.sql");

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, "imported");
            statement.executeUpdate();
        }
    }

    public List<User> findAll(Connection connection) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = SQLLoader.load("users/find_all.sql");

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("password_hash")
                ));
            }
        }

        return users;
    }
}

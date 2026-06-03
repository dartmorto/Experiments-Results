package database;

import domain.MeasurementParam;
import domain.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

public class ResultRepository {

    public void deleteAll(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLLoader.load("results/delete_all.sql"))) {
            statement.executeUpdate();
        }
    }

    public void save(Connection connection, Result result) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLLoader.load("results/insert.sql"))) {
            statement.setLong(1, result.getId());
            statement.setLong(2, result.getRunId());
            statement.setString(3, result.getParam().name());
            statement.setDouble(4, result.getValue());
            statement.setString(5, result.getUnit());
            statement.setString(6, result.getComment());
            statement.setTimestamp(7, Timestamp.from(result.getCreatedAt()));
            statement.executeUpdate();
        }
    }

    public Map<Long, Result> findAll(Connection connection) throws SQLException {
        Map<Long, Result> results = new TreeMap<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLLoader.load("results/find_all.sql"));
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Result result = new Result(
                        resultSet.getLong("id"),
                        resultSet.getLong("run_id"),
                        resultSet.getString("comment"),
                        resultSet.getDouble("value"),
                        resultSet.getString("unit"),
                        resultSet.getTimestamp("created_at").toInstant(),
                        MeasurementParam.valueOf(resultSet.getString("param"))
                );
                results.put(result.getId(), result);
            }
        }

        return results;
    }
}

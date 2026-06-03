package database;

import domain.Run;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class RunRepository {

    public void deleteAll(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLLoader.load("runs/delete_all.sql"))) {
            statement.executeUpdate();
        }
    }

    public void save(Connection connection, Run run) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLLoader.load("runs/insert.sql"))) {
            statement.setLong(1, run.getId());
            statement.setLong(2, run.getExperimentId());
            statement.setString(3, run.getOperatorUsername());
            statement.setString(4, run.getName());
            statement.executeUpdate();
        }
    }

    public Map<Long, Run> findAll(Connection connection) throws SQLException {
        Map<Long, Run> runs = new TreeMap<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLLoader.load("runs/find_all.sql"));
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Run run = new Run(
                        resultSet.getLong("id"),
                        resultSet.getLong("experiment_id"),
                        resultSet.getString("name"),
                        resultSet.getString("operator_username")
                );
                runs.put(run.getId(), run);
            }
        }

        return runs;
    }
}

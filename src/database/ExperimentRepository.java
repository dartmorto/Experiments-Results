package database;

import domain.Experiment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class ExperimentRepository {

    public void deleteAll(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLLoader.load("experiments/delete_all.sql"))) {
            statement.executeUpdate();
        }
    }

    public void save(Connection connection, Experiment experiment) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLLoader.load("experiments/insert.sql"))) {
            statement.setLong(1, experiment.getId());
            statement.setString(2, experiment.getName());
            statement.setString(3, experiment.getDescription());
            statement.setString(4, experiment.getOwnerUsername());
            statement.executeUpdate();
        }
    }

    public Map<Long, Experiment> findAll(Connection connection) throws SQLException {
        Map<Long, Experiment> experiments = new TreeMap<>();

        try (PreparedStatement statement = connection.prepareStatement(SQLLoader.load("experiments/find_all.sql"));
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Experiment experiment = new Experiment(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("owner_username")
                );
                experiments.put(experiment.getId(), experiment);
            }
        }

        return experiments;
    }
}

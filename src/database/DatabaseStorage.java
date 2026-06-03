package database;

import domain.Experiment;
import domain.Result;
import domain.Run;
import manager.CollectionManager;
import user.User;
import user.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseStorage {
    private final UserDbRepository userDbRepository = new UserDbRepository();
    private final ExperimentRepository experimentRepository = new ExperimentRepository();
    private final RunRepository runRepository = new RunRepository();
    private final ResultRepository resultRepository = new ResultRepository();

    public void save(CollectionManager manager, UserRepository userRepository) {
        DatabaseInitialization.init();

        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try {
                resultRepository.deleteAll(connection);
                runRepository.deleteAll(connection);
                experimentRepository.deleteAll(connection);

                userDbRepository.saveAll(connection, userRepository.getAllUsers());
                saveMissingUsers(connection, manager);

                for (Experiment experiment : manager.getAllExperiments().values()) {
                    experimentRepository.save(connection, experiment);
                }

                for (Run run : manager.getAllRuns().values()) {
                    runRepository.save(connection, run);
                }

                for (Result result : manager.getAllResults().values()) {
                    resultRepository.save(connection, result);
                }

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка работы с БД: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сохранения в БД: " + e.getMessage(), e);
        }
    }

    public void load(CollectionManager manager, UserRepository userRepository) {
        DatabaseInitialization.init();

        try (Connection connection = DatabaseManager.getConnection()) {
            for (User user : userDbRepository.findAll(connection)) {
                userRepository.saveOrUpdate(user);
            }

            manager.replaceData(
                    experimentRepository.findAll(connection),
                    runRepository.findAll(connection),
                    resultRepository.findAll(connection)
            );
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка работы с БД: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки из БД: " + e.getMessage(), e);
        }
    }

    private void saveMissingUsers(Connection connection, CollectionManager manager) throws SQLException {
        for (Experiment experiment : manager.getAllExperiments().values()) {
            userDbRepository.saveIfNotExists(connection, experiment.getOwnerUsername());
        }

        for (Run run : manager.getAllRuns().values()) {
            userDbRepository.saveIfNotExists(connection, run.getOperatorUsername());
        }
    }
}

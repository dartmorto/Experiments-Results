package database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitialization {

    public static void init() {
        executeSqlFile("init.sql");
        executeSqlFile("users.sql");
        executeSqlFile("experiments/exp.sql");
        executeSqlFile("runs/runs.sql");
        executeSqlFile("results/results.sql");
    }

    private static void executeSqlFile(String fileName) {
        String sql = SQLLoader.load(fileName);
        if (isEmptySql(sql)) {
            return;
        }

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка выполнения SQL-файла: " + fileName, e);
        }
    }

    private static boolean isEmptySql(String sql) {
        for (String line : sql.split("\\R")) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                return false;
            }
        }
        return true;
    }
}

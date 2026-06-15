package database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitialization {

    public static void init() {
        executeSqlFile("init.sql");
        executeSqlFile("users.sql");
        executeSqlFile("users/migrate.sql");
        executeSqlFile("experiments/exp.sql");
        executeSqlFile("experiments/migrate.sql");
        executeSqlFile("runs/runs.sql");
        executeSqlFile("runs/migrate.sql");
        executeSqlFile("results/results.sql");
        executeSqlFile("results/migrate.sql");
    }

    private static void executeSqlFile(String fileName) {
        String sql = SQLLoader.load(fileName);
        if (isEmptySql(sql)) {
            return;
        }

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {

            for (String command : splitSql(sql)) {
                if (!command.isBlank()) {
                    statement.execute(command);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка выполнения SQL-файла: " + fileName, e);
        }
    }

    private static java.util.List<String> splitSql(String sql) {
        java.util.List<String> commands = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean dollarBlock = false;

        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);

            if (ch == '$' && i + 1 < sql.length() && sql.charAt(i + 1) == '$') {
                dollarBlock = !dollarBlock;
                current.append("$$");
                i++;
                continue;
            }

            if (ch == ';' && !dollarBlock) {
                commands.add(current.toString().trim());
                current.setLength(0);
                continue;
            }

            current.append(ch);
        }

        if (!current.toString().isBlank()) {
            commands.add(current.toString().trim());
        }

        return commands;
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

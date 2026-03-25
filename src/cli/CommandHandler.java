package cli;

import manager.CollectionManager;

import java.util.Scanner;

/**
 * Класс обработки команд пользователя.
 * Отвечает за взаимодействие с пользователем:
 * чтение команд
 * обработку ввода
 * вывод результатов
 */
public class CommandHandler {

    private final CollectionManager manager;
    private final Scanner scanner;

    /**
     * Создает обработчик команд.
     *
     * @param manager сервис управления коллекциями
     */
    public CommandHandler(CollectionManager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает цикл обработки команд.
     */
    public void start() {

        System.out.println("Система управления экспериментами");
        System.out.println("Введите 'help' для списка команд");

        while (true) {
            try {
                System.out.print("> ");
                String command = scanner.nextLine().trim();

                if (command.isEmpty()) {
                    continue;
                }

                handleCommand(command);

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Обрабатывает введенную команду.
     *
     * @param command строка команды
     */
    private void handleCommand(String command) {

        switch (command) {
            case "help" -> printHelp();
            case "create_experiment" -> createExperiment();
            case "create_run" -> createRun();
            case "create_result" -> createResult();
            case "exit" -> exit();
            default -> System.out.println("Неизвестная команда. Введите 'help'");
        }
    }

    /**
     * Выводит список доступных команд.
     */
    private void printHelp() {
        System.out.println("""
                Доступные команды:
                create_experiment - создать эксперимент
                create_run        - создать запуск
                create_result     - создать результат
                exit              - выход
                """);
    }

    /**
     * Создает эксперимент.
     */
    private void createExperiment() {

        String name = readString("Название");
        String description = readString("Описание");
        String owner = readString("Владелец");

        manager.createExperiment(name, description, owner);

        System.out.println("Эксперимент успешно создан");
    }

    /**
     * Создает запуск.
     */
    private void createRun() {

        long experimentId = readLong("ID эксперимента");
        String name = readString("Название запуска");
        String operator = readString("Оператор");

        manager.createRun(experimentId, name, operator);

        System.out.println("Запуск успешно создан");
    }

    /**
     * Создает результат.
     */
    private void createResult() {

        long runId = readLong("ID запуска");
        String unit = readString("Единица измерения");
        String comment = readOptionalString("Комментарий");

        System.out.println("Введите параметр (например TEMPERATURE):");
        String paramInput = scanner.nextLine().trim();

        var param = Enum.valueOf(model.MeasurementParam.class, paramInput);

        System.out.println("Введите значение:");
        double value = Double.parseDouble(scanner.nextLine());

        manager.createResult(runId, param, value, unit, comment);

        System.out.println("Результат успешно создан");
    }

    /**
     * Завершает работу программы.
     */
    private void exit() {
        System.out.println("Завершение работы...");
        System.exit(0);
    }

    /**
     * Считывает обязательную строку.
     */
    private String readString(String fieldName) {
        while (true) {
            System.out.print(fieldName + ": ");
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(fieldName + " не может быть пустым");
        }
    }

    /**
     * Считывает необязательную строку.
     */
    private String readOptionalString(String fieldName) {
        System.out.print(fieldName + " (можно пусто): ");
        return scanner.nextLine().trim();
    }

    /**
     * Считывает long с валидацией.
     */
    private long readLong(String fieldName) {
        while (true) {
            try {
                System.out.print(fieldName + ": ");
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число");
            }
        }
    }
}
package cli;

import manager.CollectionManager;
import cli.commands.*;

import java.util.*;
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
    public final Map<String, Command> commands = new HashMap<>();

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

            System.out.print("> ");

            String line = scanner.nextLine().trim();

            if (line.isEmpty()) continue;

            String[] parts = line.split(" ");
            String commandName = parts[0];

            Command command = commands.get(commandName);

            if (command == null) {
                System.out.println("Неизвестная команда");
                continue;
            }

            try {
                command.execute(parts);
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

}
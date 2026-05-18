package cli.commands;

import domain.Run;
import manager.CollectionManager;

import java.util.Scanner;

/**
 * Команда создания запуска эксперимента.
 * Создает запуск, связанный с существующим экспериментом.
 */

public class RunCreateCommand extends Command {

    public RunCreateCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String name() {
        return "create_run";
    }

    @Override
    public void execute(String[] args) {

        long experimentId;
        while (true) {
            System.out.print("ID эксперимента: ");
            String input = scanner.nextLine();
            cancelIfCancelled(input);
            try {
                experimentId = Long.parseLong(input);
                if (experimentId <= 0) {
                    System.out.println("Ошибка: ID должен быть положительным");
                    continue;
                }

                if (manager.getAllExperiments().containsKey(experimentId)) {
                    break;
                }
                System.out.println("Ошибка: эксперимент не найден");
            } catch (NumberFormatException e) {
                // сработает и если ввод не число: повторно проверяем отмену
                // (ошибка порядка вызовов/старая сборка не должны мешать выйти из команды)
                cancelIfCancelled(input);
                System.out.println("Ошибка: введите число");
            }
        }

        String name;
        while (true) {
            System.out.print("Название запуска: ");
            name = scanner.nextLine();
            cancelIfCancelled(name);
            if (!name.isBlank()) break;
            System.out.println("Ошибка: имя запуска не может быть пустым");
        }

        String operator;
        while (true) {
            System.out.print("Оператор: ");
            operator = scanner.nextLine();
            cancelIfCancelled(operator);
            if (!operator.isBlank()) break;
            System.out.println("Ошибка: оператор не может быть пустым");
        }

        Run run = manager.createRun(experimentId, name, operator);

        System.out.println("Добавлен запуск. ID: " + run.getId() + " ID эксперимента: " + experimentId);
    }
}

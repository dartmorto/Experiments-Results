package cli.commands;

import domain.Result;
import manager.CollectionManager;

import java.util.Scanner;

/**
 * Команда вывода списка всех результатов измерений.
 * Отображает ID результата, ID запуска, параметр и значение.
 */
public class ResultListCommand extends Command {

    public ResultListCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    /**
     * Имя команды CLI.
     *
     * @return строка команды
     */
    @Override
    public String name() {
        return "res_list";
    }

    /**
     * Выполнение команды.
     * Выводит все результаты измерений.
     *
     * @param args аргументы команды
     */
    @Override
    public void execute(String[] args) {

        if (manager.getAllResults().isEmpty()) {
            System.out.println("Результатов нет.");
            return;
        }

        for (Result result : manager.getAllResults().values()) {
            System.out.println(
                    "ID: " + result.getId() +
                    " | Run ID: " + result.getRunId() +
                    " | Param: " + result.getParam() +
                    " | Value: " + result.getValue()
            );
        }
    }

    /**
     * Регистрация команды.
     *
     * @param commands карта команд
     */
}


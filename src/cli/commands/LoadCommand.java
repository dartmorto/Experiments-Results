package cli.commands;

import manager.*;

import java.util.*;

/**
 * Команда загрузки данных из файла.
 */
public class LoadCommand extends Command {

    public LoadCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String name() {
        return "load";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Команда load отключена: данные должны загружаться из БД при запуске.");
    }
}

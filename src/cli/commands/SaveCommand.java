package cli.commands;

import manager.*;

import java.util.*;

/**
 * Команда сохранения текущих данных в файл.
 */
public class SaveCommand extends Command {

    public SaveCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String name() {
        return "save";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Команда save отключена: данные должны сохраняться через БД при изменении.");
    }
}

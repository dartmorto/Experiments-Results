package cli.commands;

import domain.Result;
import manager.CollectionManager;

import java.util.*;

/**
 * Команда отображения результата измерения по ID.
 */

public class ResultShowCommand extends Command implements Registry{
    public ResultShowCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String Name() {
        return "res_show";
    }

    @Override
    public void execute(String[] parts) {
        long resId;
        if (parts.length < 2) {
            System.out.print("ID результата: ");
            String input = scanner.nextLine();
            cancelIfCancelled(input);
            resId = parseId(input);
        } else {
            resId = parseId(parts[1]);
        }
        Result result = manager.getResultById(resId);
        System.out.println("ID: " + result.id);
        System.out.println("ID запуска: " + result.runId);
        System.out.println("Параметр: " + result.param);
        System.out.println("Значение: " + result.value);
        System.out.println("Единицы измерения: " + result.unit);
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }
}

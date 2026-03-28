package cli.commands;

import domain.Result;
import manager.CollectionManager;

import java.util.*;

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
        if (parts.length < 2) {
            throw new IllegalArgumentException("Укажите ID результата");
        }
        long resId = parseId(parts[1]);
        Result result = manager.getResultById(resId);
        System.out.println("ID" + result.id);
        System.out.println("ID Пробега" + result.runId);
        System.out.println("Параметр" + result.param);
        System.out.println("Значение" + result.value);
        System.out.println("Единицы Измерения" + result.unit);
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }
}

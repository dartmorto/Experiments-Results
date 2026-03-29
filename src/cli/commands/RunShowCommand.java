package cli.commands;

import domain.*;
import manager.CollectionManager;

import java.util.*;

/**
 * Команда отображения информации о запуске по ID.
 */

public class RunShowCommand extends Command implements Registry {

    public RunShowCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String Name() {
        return "run_show";
    }

    @Override
    public void execute(String[] args) {
        System.out.print("ID запуска: ");
        String idLine = scanner.nextLine();
        cancelIfCancelled(idLine);
        long id = parseId(idLine);

        Run run = manager.getRunById(id);

        if (run == null) {
            System.out.println("Запуск не найден");
            return;
        }

        System.out.println("ID: " + run.getId());
        System.out.println("Experiment ID: " + run.getExperimentId());
        System.out.println("Название: " + run.name);
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }
}
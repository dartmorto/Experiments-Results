package cli.commands;

import domain.*;
import manager.CollectionManager;

import java.util.*;

/**
 * Команда обновления данных эксперимента.
 */

public class ExpUpdateCommand extends Command implements Registry {

    public ExpUpdateCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String Name() {
        return "exp_update";
    }

    @Override
    public void execute(String[] args) {

        System.out.print("ID эксперимента: ");
        String idLine = scanner.nextLine();
        cancelIfCancelled(idLine);
        long id = parseId(idLine);

        final Experiment old;
        try {
            old = manager.getById(id);
        } catch (IllegalArgumentException e) {
            System.out.println("Эксперимент не найден");
            return;
        }

        System.out.print("Новое название: ");
        String name = scanner.nextLine();
        cancelIfCancelled(name);

        System.out.print("Новое описание: ");
        String description = scanner.nextLine();
        cancelIfCancelled(description);

        Experiment updated = new Experiment(id, name, description, old.getOwner());

        manager.updateExperiment(id, updated);

        System.out.println("Эксперимент обновлен");
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }
}

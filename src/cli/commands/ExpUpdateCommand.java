package cli.commands;

import domain.Experiment;
import manager.CollectionManager;

import java.util.Scanner;

/**
 * Команда обновления данных эксперимента.
 */

public class ExpUpdateCommand extends Command {

    public ExpUpdateCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String name() {
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
}

package cli.commands;

import domain.*;
import manager.CollectionManager;

import java.util.*;

/**
 * Команда отображения информации об эксперименте по ID.
 */

public class ExpShowCommand extends Command implements Registry {

    public ExpShowCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String Name() {
        return "exp_show";
    }

    @Override
    public void execute(String[] args) {
        System.out.print("ID эксперимента: ");
        String idLine = scanner.nextLine();
        cancelIfCancelled(idLine);
        long id = parseId(idLine);

        try {
            Experiment exp = manager.getById(id);
            System.out.println("ID: " + exp.getId());
            System.out.println("Название: " + exp.getName());
            System.out.println("Владелец: " + exp.getOwner());
        } catch (IllegalArgumentException e) {
            System.out.println("Эксперимент не найден");
        }
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }
}

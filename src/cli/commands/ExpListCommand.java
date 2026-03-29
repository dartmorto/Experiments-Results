package cli.commands;
import cli.commands.Command;
import manager.CollectionManager;
import domain.Experiment;
import java.util.*;

/**
 * Команда вывода списка всех экспериментов.
 */

public class ExpListCommand extends Command implements Registry {

    public ExpListCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String Name() {
        return "exp_list";
    }

    @Override
    public void execute(String[] args) {

        if (manager.getAllExperiments().isEmpty()) {
            System.out.println("Экспериментов нет.");
            return;
        }

        System.out.println("ID  Name");

        for (Experiment e : manager.getAllExperiments().values()) {
            System.out.println(e.id + "  " + e.name);
        }


    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }
}
package cli.commands;
import cli.commands.Command;
import manager.CollectionManager;
import domain.Run;

import java.util.*;
import java.util.Scanner;





public class RunListCommand extends Command implements Registry {

    public RunListCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String Name() {
        return "run_list";
    }

    @Override
    public void execute(String[] args) {

        if (manager.getAllRuns().isEmpty()) {
            System.out.println("Пробегов нет.");
            return;
        }

        System.out.println("ID  Name");

        for (Run r : manager.getAllRuns().values()) {
            System.out.println(r.id + "  " + r.experimentId + " " + r.name);
        }
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }
}
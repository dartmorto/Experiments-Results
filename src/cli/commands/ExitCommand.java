package cli.commands;

import manager.CollectionManager;

import java.util.*;

/**
 * Команда завершения работы приложения.
 */

public class ExitCommand extends Command implements Registry{
    public ExitCommand(CollectionManager manager, Scanner scanner) {super(manager, scanner);}

    @Override
    public String Name() {
        return "exit";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Выход из программы...");
        System.exit(0);
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }
}

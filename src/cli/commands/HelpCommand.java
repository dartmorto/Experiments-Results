package cli.commands;
import cli.commands.Command;
import manager.CollectionManager;

import java.util.*;


public class HelpCommand extends Command implements Registry {

    public HelpCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }
    @Override
    public String Name() {

        return "help";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("""
                Доступные команды:
                create_experiment - создать эксперимент
                create_run        - создать запуск
                create_result     - создать результат
                exit              - выход
                """);
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }


}

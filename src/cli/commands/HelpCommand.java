package cli.commands;
import cli.commands.Command;
import manager.CollectionManager;

import java.util.*;

/**
 * Команда вывода списка доступных команд.
 */


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
                exp_update        - обновить запись об эксперименте
                exp_show          - показать эксперимент
                run_show          - показать запуск
                res_show          - показать результат
                exp_list          - показать все эксперименты
                run_list          - показать все запуски
                exp_summary       - сводка по эксперименту
                exit              - выход
                Во время пошагового ввода: cancel, отмена или q — прервать команду

                """);
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }


}

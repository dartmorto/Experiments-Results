package cli.commands;

import manager.CollectionManager;

import java.util.Scanner;

/**
 * Команда вывода списка доступных команд.
 */


public class HelpCommand extends Command {

    public HelpCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }
    @Override
    public String name() {

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
                res_list          - показать все результаты
                exp_summary       - сводка по эксперименту
                save <path>       - сохранить данные в файл
                load <path>       - загрузить данные из файла
                history           - история команд
                exit              - выход
                Во время пошагового ввода: cancel, отмена или q — прервать команду

                """);
    }


}

package cli.commands;

import cli.commands.Command;
import manager.CollectionManager;
import domain.*;

import java.util.*;
import java.util.Scanner;

/**
 * Команда создания эксперимента.
 * Запрашивает у пользователя данные и добавляет
 * новый эксперимент в коллекцию.
 */

public class ExpCreateCommand extends Command implements Registry {

    public ExpCreateCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String Name() {
        return "create_experiment";
    }

    @Override
    public void execute(String[] args) {
        
        String name;
    while (true) {
        System.out.print("Название: ");
        name = scanner.nextLine();
        cancelIfCancelled(name);
        if (!name.isBlank()) break;
        System.out.println("Ошибка: имя не может быть пустым");
    }

        System.out.print("Описание (можно пусто): ");
        String description = scanner.nextLine();
        cancelIfCancelled(description);

        String owner;
        while (true) {
            System.out.print("Владелец: ");
            owner = scanner.nextLine();
            cancelIfCancelled(owner);
            if (!owner.isBlank()) {
                break;
            }
            System.out.println("Ошибка: имя владельца не может быть пустым");
        }

        Long expId = manager.generateExperimentId();

        domain.Experiment exp = new Experiment(expId, name, description, owner);

        manager.addExperiment(exp);


        System.out.println("Добавлен экперимент. ID: " + expId + " Название: " + name);


    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }


}

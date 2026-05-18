package cli.commands;

import domain.Experiment;
import manager.CollectionManager;

import java.util.Scanner;

/**
 * Команда создания эксперимента.
 * Запрашивает у пользователя данные и добавляет
 * новый эксперимент в коллекцию.
 */

public class ExpCreateCommand extends Command {

    public ExpCreateCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String name() {
        return "create_experiment";
    }

    @Override
    public void execute(String[] args) {
        
        String name;
        while (true) {
            System.out.print("Название: ");
            name = scanner.nextLine();
            cancelIfCancelled(name);
            if (!name.isBlank()) {
                break;
            }
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

        Experiment exp = manager.createExperiment(name, description, owner);
        long expId = exp.getId();

        System.out.println("Добавлен эксперимент. ID: " + expId + " Название: " + name);


    }


}

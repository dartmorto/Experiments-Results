package cli.commands;

import domain.Experiment;
import manager.CollectionManager;
import user.AuthService;

import java.util.Scanner;

/**
 * Команда создания эксперимента.
 * Запрашивает у пользователя данные и добавляет
 * новый эксперимент в коллекцию.
 */

public class ExpCreateCommand extends Command {

    public ExpCreateCommand(CollectionManager manager, Scanner scanner, AuthService authService) {
        super(manager, scanner, authService);
    }

    @Override
    public String name() {
        return "create_experiment";
    }

    @Override
    public void execute(String[] args) {
        requireLogin();
        
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

        System.out.print("Целевое вещество: ");
        String targetSubstance = scanner.nextLine();
        cancelIfCancelled(targetSubstance);

        while (targetSubstance.trim().isEmpty()) {
            System.out.println("Ошибка: целевое вещество не может быть пустым");
            System.out.print("Целевое вещество: ");
            targetSubstance = scanner.nextLine();
            cancelIfCancelled(targetSubstance);
        }

        Experiment exp = manager.createExperiment(name, description, currentUsername(), targetSubstance);
        long expId = exp.getId();


        System.out.println("Добавлен эксперимент. ID: " + expId + " Название: " + name);


    }


}

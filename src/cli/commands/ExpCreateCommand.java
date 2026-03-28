package cli.commands;

import cli.commands.Command;
import manager.CollectionManager;
import domain.*;

import java.util.*;
import java.util.Scanner;

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
        System.out.print("Название: ");
        String name = scanner.nextLine();

        if (name.isBlank()) {
            System.out.println("Ошибка: имя не может быть пустым");
            return;
        }

        System.out.print("Описание (можно пусто): ");
        String description = scanner.nextLine();

        System.out.print("Владелец: ");
        String owner = scanner.nextLine();
        Long expId = manager.generateExperimentId();

        domain.Experiment exp = new Experiment(expId, name, description, owner);


        System.out.println("OK experiment_id=" + expId);


    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }


}

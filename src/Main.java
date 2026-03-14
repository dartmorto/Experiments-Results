
import manager.CollectionManager;
import model.Experiment;
import cli.CLI;

import java.io.IOException;
public class Main {
public static void main(String[] args) {


    CollectionManager manager = new CollectionManager();

    CLI cli = new CLI(manager);
    cli.start();



    // работа менеджера

    try {
            // пример добавления экспериментов
            manager.createExperiment( "Test 1", "First experiment", "John");
            manager.createExperiment("Test 2", "Second experiment", "Jane");

        manager.printStats();

    } catch (Exception e) {
        System.err.println("Ошибка при работе с данными: " + e.getMessage());
        e.printStackTrace();
    }




    System.out.println("Программа завершена.");
}
}

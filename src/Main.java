import manager.FileStorageManager;
import manager.CollectionManager;
import model.Experiment;

import java.io.IOException;

public static void main(String[] args) {
    // получаем имя файла из переменной окружения
    String filename = System.getenv("EXPERIMENTS_DATA_FILE");

    if (filename == null) {
        System.err.println("Ошибка: Переменная окружения 'EXPERIMENTS_DATA_FILE' не установлена");
        System.err.println("  Установите переменную перед запуском:");
        System.exit(1);
    }

    CollectionManager manager = new CollectionManager();

    // загружаем данные из файла при старте
    try {
        FileStorageManager.loadFromFile(manager, filename);
    } catch (IOException e) {
        System.err.println(" Ошибка при загрузке данных. Программа продолжит работу с пустыми коллекциями.");
        e.printStackTrace();
    // выводим статистику
    manager.printStats();

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

    // сохраняем данные перед выходом
    try {
        FileStorageManager.saveToFile(manager, filename);
    } catch (IOException e) {
        System.err.println("Ошибка при сохранении данных: " + e.getMessage());
        e.printStackTrace();  // выводим трассировку стека
    }

    System.out.println("Программа завершена.");
}
}
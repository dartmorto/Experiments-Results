import manager.CollectionManager;
import manager.FileStorageManager;
import model.*;

import java.io.IOException;

/**
 * Главный класс приложения для управления экспериментами.
 * Загружает данные из файла при запуске и сохраняет перед выходом.
 */
public class Main {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        // Получаем имя файла из переменной окружения
        String filename = System.getenv("EXPERIMENTS_DATA_FILE");

        if (filename == null) {
            System.err.println("✗ Ошибка: Переменная окружения 'EXPERIMENTS_DATA_FILE' не установлена");
            System.err.println("  Установите переменную перед запуском:");
            System.exit(1);
        }

        CollectionManager manager = new CollectionManager();

        // Загружаем данные из файла при старте
        try {
            FileStorageManager.loadFromFile(manager, filename);
        } catch (IOException e) {
            System.err.println("✗ Ошибка при загрузке данных. Программа продолжит работу с пустыми коллекциями.");
        }

        // Выводим статистику
        manager.printStats();

        // === ВАШ КОД ДЛЯ РАБОТЫ С МЕНЕДЖЕРОМ ===

        try {
            // Пример добавления экспериментов
            manager.addExperiment(new Experiment(1, "Test 1", "First experiment", "John"));
            manager.addExperiment(new Experiment(2, "Test 2", "Second experiment", "Jane"));

            manager.printStats();

        } catch (Exception e) {
            System.err.println("✗ Ошибка при работе с данными: " + e.getMessage());
            e.printStackTrace();
        }

        // Сохраняем данные перед выходом
        try {
            FileStorageManager.saveToFile(manager, filename);
        } catch (IOException e) {
            System.err.println("✗ Ошибка при сохранении данных: " + e.getMessage());
        }

        System.out.println("Программа завершена.");
    }
}
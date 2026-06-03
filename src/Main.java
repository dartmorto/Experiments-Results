/**
 * Запуск приложения.
 * Инициализирует основные компоненты системы и запускает CLI-интерфейс.
 */
import cli.*;
import manager.*;
import database.DatabaseInitialization;


public class Main {

    /**
     * Главный метод приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {

        try {
            DatabaseInitialization.init();
        } catch (RuntimeException e) {
            System.out.println("БД пока недоступна: " + e.getMessage());
            System.out.println("Приложение запущено в режиме памяти.");
        }

        CollectionManager manager = new CollectionManager();
        CommandHandler commandHandler = new CommandHandler(manager);

        System.out.println("Приложение запущено");
        commandHandler.start();
    }
}

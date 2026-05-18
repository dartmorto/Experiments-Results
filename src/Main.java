/**
 * Запуск приложения.
 * Инициализирует основные компоненты системы и запускает CLI-интерфейс.
 */
import cli.CommandHandler;
import manager.CollectionManager;
import storage.FileStorage;

public class Main {

    /**
     * Главный метод приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {

        CollectionManager manager = new CollectionManager();
        FileStorage storage = new FileStorage();

        if (args.length > 0) {
            String path = String.join(" ", args);
            try {
                storage.load(manager, path);
                System.out.println("Данные загружены из файла: " + path);
            } catch (Exception e) {
                System.out.println("Ошибка загрузки при запуске: " + e.getMessage());
            }
        }

        CommandHandler cli = new CommandHandler(manager, storage);

        cli.start();
    }
}


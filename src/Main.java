/**
 * Запуск приложения.
 * Инициализирует основные компоненты системы и запускает CLI-интерфейс.
 */
import cli.CommandHandler;
import manager.CollectionManager;

public class Main {

    /**
     * Главный метод приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {

        CollectionManager manager = new CollectionManager();
        CommandHandler cli = new CommandHandler(manager);

        cli.start();
    }
}


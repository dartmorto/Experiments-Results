package cli.commands;

import manager.CollectionManager;
import storage.FileStorage;

import java.util.Objects;
import java.util.Scanner;

/**
 * Команда сохранения текущих данных в файл.
 */
public class SaveCommand extends Command {

    private final FileStorage storage;

    public SaveCommand(CollectionManager manager, Scanner scanner, FileStorage storage) {
        super(manager, scanner);
        this.storage = Objects.requireNonNull(storage, "storage");
    }

    @Override
    public String name() {
        return "save";
    }

    @Override
    public void execute(String[] args) {
        String path = readPath(args, "Путь для сохранения: ");

        try {
            storage.save(manager, path);
            System.out.println("Данные сохранены и объединены: " + path);
        } catch (Exception e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    private String readPath(String[] args, String prompt) {
        if (args.length > 1) {
            return normalizePath(String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length)));
        }

        System.out.print(prompt);
        String input = scanner.nextLine();
        cancelIfCancelled(input);
        return normalizePath(input);
    }

    private String normalizePath(String path) {
        String result = path.trim();
        if (result.length() >= 2 && result.startsWith("\"") && result.endsWith("\"")) {
            return result.substring(1, result.length() - 1);
        }
        return result;
    }
}

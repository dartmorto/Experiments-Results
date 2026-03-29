package cli.commands;

import domain.*;
import manager.CollectionManager;

import java.util.*;

/**
 * Команда создания результата измерения.
 * Добавляет измерение к конкретному запуску.
 */

public class ResultCreateCommand extends Command implements Registry {
    public ResultCreateCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }


    @Override
    public String Name() {
        return "create_result";
    }

    @Override
    public void execute(String[] parts) {
        long runId;
        if (parts.length < 2) {
            System.out.print("ID запуска: ");
            String input = scanner.nextLine();
            cancelIfCancelled(input);
            runId = parseId(input);
        } else {
            runId = parseId(parts[1]);
        }
        System.out.println("Комментарий(по желанию)");
        String comment = scanner.nextLine();
        cancelIfCancelled(comment);

        MeasurementParam param;

        String paramInput;
        while (true) {
            System.out.println("Введите параметр (например TEMPERATURE):");
            paramInput = scanner.nextLine().trim().toUpperCase();
            cancelIfCancelled(paramInput);

            try {
                param = MeasurementParam.valueOf(paramInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: неизвестный параметр. Попробуйте снова.");
                System.out.println("Доступные параметры:");
                for (MeasurementParam p : MeasurementParam.values()) {
                    System.out.println("- " + p);
                }

            }
        }

        double value;
        while (true) {
            System.out.println("Введите значение:");
            String valueInput = scanner.nextLine().trim();
            try {
                value = Double.parseDouble(valueInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число");
            }
        }

        System.out.println("Единица измерения");
        String unit = scanner.nextLine();
        cancelIfCancelled(unit);

        Long resId = manager.generateResultId();
        Result result = new Result(resId, runId, comment, value, unit, java.time.Instant.now(), param);
        manager.addResult(result);
        System.out.println("Результат успешно создан");
    }
}
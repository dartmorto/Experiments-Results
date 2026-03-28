package cli.commands;

import domain.*;
import manager.CollectionManager;

import java.util.*;

public class ResultCreateCommand extends Command implements Registry{
    public ResultCreateCommand(CollectionManager manager, Scanner scanner) {
        super(manager, scanner);
    }

    @Override
    public String Name() {return "create_result";}

    @Override
    public void execute(String[] parts) {
        long runId = parseId(parts[1]);
        System.out.println("Единица измерения");
        String unit = scanner.nextLine();
        System.out.println("Комментарий(по желанию)");
        String comment = scanner.nextLine();

        System.out.println("Введите параметр (например TEMPERATURE):");
        String paramInput = scanner.nextLine();

        var param = Enum.valueOf(MeasurementParam.class, paramInput);

        System.out.println("Введите значение:");
        double value = Double.parseDouble(scanner.nextLine());

        Long resId = manager.generateResultId();

        manager.createResult(resId, runId, param, value, unit, comment);

        System.out.println("Результат успешно создан");
    }

    @Override
    public void register(Map<String, Command> commands) {
        commands.put(Name(), this);
    }
}

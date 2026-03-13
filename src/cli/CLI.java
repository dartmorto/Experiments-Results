package cli;

import manager.CollectionManager;
import model.*;

import java.util.Scanner;

public class CLI {

    private final CollectionManager manager;
    private final Scanner scanner = new Scanner(System.in);

    private long expId = 1;
    private long runId = 1;
    private long resId = 1;

    public CLI(CollectionManager manager) {
        this.manager = manager;
    }

    public void start() {

        System.out.println("Введите команду");

        while (true) {

            System.out.print("> ");
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) continue;

            String[] parts = line.split(" ");
            String cmd = parts[0];

            try {

                switch (cmd) {

                    case "help":
                        help();
                        break;

                    case "exit":
                        System.out.println("Завершение программы.");
                        return;

                    case "exp_create":
                        expCreate();
                        break;

                    case "exp_list":
                        expList();
                        break;

                    case "exp_show":
                        expShow(parts);
                        break;

                    case "run_add":
                        runAdd(parts);
                        break;

                    case "run_list":
                        runList(parts);
                        break;

                    case "res_add":
                        resAdd(parts);
                        break;

                    case "res_list":
                        resList(parts);
                        break;

                    case "stats":
                        manager.printStats();
                        break;

                    default:
                        System.out.println("Неизвестная команда, введите help для списка команд");
                }

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void help() {

        System.out.println("""
Доступные команды:

help
exit

exp_create
exp_list
exp_show <experiment_id>

run_add <experiment_id>
run_list <experiment_id>

res_add <run_id>
res_list

stats
""");
    }

    private void expCreate() {

        System.out.print("Название: ");
        String name = scanner.nextLine();

        if (name.isBlank()) {
            System.out.println("Ошибка: имя не может быть пустым");
            return;
        }

        System.out.print("Описание (можно пусто): ");
        String description = scanner.nextLine();

        System.out.println("Владелец: ");
        String owner = scanner.nextLine();

        Experiment exp = new Experiment(expId, name, description, owner);

        manager.addExperiment(exp);

        System.out.println("OK experiment_id=" + expId);

        expId++;
    }

    private void expList() {

        if (manager.getAllExperiments().isEmpty()) {
            System.out.println("Экспериментов нет.");
            return;
        }

        System.out.println("ID  Name");

        for (Experiment e : manager.getAllExperiments()) {
            System.out.println(e.id + "  " + e.name);
        }
    }

    private void expShow(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Использование: exp_show <experiment_id>");
            return;
        }

        long id = Long.parseLong(parts[1]);

        Experiment exp = manager.getExperiment(id);

        if (exp == null) {
            System.out.println("Эксперимент не найден");
            return;
        }



        System.out.println("Номер эксперимента " + exp.id);
        System.out.println("Название: " + exp.name);

        long count = manager.getAllRuns()
                .stream()
                .filter(r -> r.experimentId == exp.id)
                .count();

        System.out.println("runs: " + count);
    }

    private void runAdd(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Использование: run_add <experiment_id>");
            return;
        }

        long expId = Long.parseLong(parts[1]);

        Experiment exp = manager.getExperiment(expId);

        if (exp == null) {
            System.out.println("Ошибка: эксперимент не найден");
            return;
        }

        System.out.print("Название: ");
        String name = scanner.nextLine();

        System.out.print("Проводящий: ");
        String operator = scanner.nextLine();

        Run run = new Run(runId, expId, name, operator);

        manager.addRun(run);

        System.out.println("OK run_id=" + runId);

        runId++;
    }

    private void runList(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Использование: run_list <experiment_id>");
            return;
        }

        long expID;

        Experiment exp = manager.getExperiment(expId);


        System.out.println("ID  Run Название  Проводящий");

        for (Run r : manager.getAllRuns()) {

            if (r.experimentId == expId) {

                System.out.println(
                        r.id + "  " +
                                r.name + "  " +
                                r.operator
                );
            }
        }
    }

    private void resAdd(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Использование: res_add <run_id>");
            return;
        }

        long runId = Long.parseLong(parts[1]);

        Run run = manager.getRun(runId);

        if (run == null) {
            System.out.println("Ошибка: run не найден");
            return;
        }

        System.out.print("Параметр: ");
        String paramInput = scanner.nextLine().toUpperCase();
        MeasurementParam param = MeasurementParam.valueOf(paramInput);

        System.out.print("Значение: ");
        double value = Double.parseDouble(scanner.nextLine());

        System.out.print("Единицы: ");
        String unit = scanner.nextLine();



        System.out.print("Комментарий (можно пусто): ");
        String comment = scanner.nextLine();

        Result result = new Result(resId, runId, param, value, unit, comment);

        manager.addResult(result);

        System.out.println("OK result_id=" + resId);

        resId++;
    }

    private void resList(String[] parts) {

        System.out.println("ID  Param  Value  Unit  Comment");

        for (Result r : manager.getAllResults()) {

            System.out.println(
                    r.id + "  " +
                            r.param + "  " +
                            r.value + "  " +
                            r.unit + "  " +
                            r.comment
            );
        }
    }
}
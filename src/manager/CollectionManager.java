package manager;

import model.*;
import java.time.Instant;
import java.util.*;

/**
 * Менеджер для управления коллекциями экспериментов, запусков и результатов.
 * Использует TreeMap для автоматической сортировки по идентификатору.
 * Предоставляет методы для создания, получения, обновления и удаления данных.
 */
public class CollectionManager {

    /** Коллекция экспериментов, отсортированная по идентификатору. */
    private final TreeMap<Long, Experiment> experiments = new TreeMap<>();

    /** Коллекция запусков, отсортированная по идентификатору. */
    private final TreeMap<Long, Run> runs = new TreeMap<>();

    /** Коллекция результатов, отсортированная по идентификатору. */
    private final TreeMap<Long, Result> results = new TreeMap<>();

    /** Счетчик для генерации уникальных идентификаторов. */
    private long currentId = 1;

    /**
     * Генерирует уникальный идентификатор.
     *
     * @return новый уникальный id
     */
    private long generateId() {
        return currentId++;
    }
    //блок про эксперименты

    /**
     * Создает новый эксперимент и добавляет его в коллекцию.
     *
     * @param name название эксперимента
     * @param description описание эксперимента
     * @param owner владелец эксперимента
     * @return созданный эксперимент
     * @throws IllegalArgumentException если название или описание пусты
     */
    public Experiment createExperiment(String name, String description, String owner) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название эксперимента не может быть пустым");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Описание эксперимента не может быть пустым");
        }
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Владелец не может быть пустым");
        }

        long id = generateId();
        Experiment experiment = new Experiment(id, name, description, owner);
        experiments.put(id, experiment);

        System.out.println("Эксперимент '" + name + "' успешно создан (ID: " + id + ")");
        return experiment;
    }

    /**
     * Получает эксперимент по идентификатору.
     *
     * @param id идентификатор эксперимента
     * @return найденный эксперимент
     * @throws IllegalArgumentException если эксперимент не найден
     */
    public Experiment getExperiment(long id) {
        Experiment experiment = experiments.get(id);
        if (experiment == null) {
            throw new IllegalArgumentException("Эксперимент с ID " + id + " не найден");
        }
        return experiment;
    }

    /**
     * Возвращает все эксперименты в отсортированном порядке.
     *
     * @return коллекция всех экспериментов
     */
    public Collection<Experiment> getAllExperiments() {
        return experiments.values();
    }

    /**
     * Обновляет эксперимент.
     *
     * @param id идентификатор эксперимента
     * @param name новое название (если не null)
     * @param description новое описание (если не null)
     * @return обновленный эксперимент
     * @throws IllegalArgumentException если эксперимент не найден
     */
    public Experiment updateExperiment(long id, String name, String description) {
        Experiment experiment = experiments.get(id);
        if (experiment == null) {
            throw new IllegalArgumentException("Эксперимент с ID " + id + " не найден");
        }

        if (name != null && !name.trim().isEmpty()) {
            experiment.name = name;
        }
        if (description != null && !description.trim().isEmpty()) {
            experiment.description = description;
        }
        experiment.updatedAt = Instant.now();

        System.out.println("✓ Эксперимент с ID " + id + " успешно обновлен");
        return experiment;
    }

    /**
     * Удаляет эксперимент по идентификатору.
     *
     * @param id идентификатор удаляемого эксперимента
     * @throws IllegalArgumentException если эксперимент не найден
     */
    public void removeExperiment(long id) {
        if (experiments.remove(id) == null) {
            throw new IllegalArgumentException("Эксперимент с ID " + id + " не найден");
        }
        System.out.println("✓ Эксперимент с ID " + id + " удален");
    }

    // блок про запуски

    /**
     * Создает новый запуск и добавляет его в коллекцию.
     *
     * @param experimentId идентификатор эксперимента
     * @param name название запуска
     * @param operator имя оператора
     * @return созданный запуск
     * @throws IllegalArgumentException если входные данные некорректны
     */
    public Run createRun(long experimentId, String name, String operator) {
        if (experimentId <= 0) {
            throw new IllegalArgumentException("ID эксперимента должен быть положительным");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название запуска не может быть пустым");
        }
        if (operator == null || operator.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя оператора не может быть пустым");
        }

        //существование эксперимента
        if (!experiments.containsKey(experimentId)) {
            throw new IllegalArgumentException("Эксперимент с ID " + experimentId + " не существует");
        }

        long id = generateId();
        Run run = new Run(id, experimentId, name, operator);
        runs.put(id, run);

        System.out.println(" Запуск '" + name + "' успешно создан (ID: " + id + ")");
        return run;
    }

    /**
     * Получает запуск по идентификатору.
     *
     * @param id идентификатор запуска
     * @return найденный запуск
     * @throws IllegalArgumentException если запуск не найден
     */
    public Run getRun(long id) {
        Run run = runs.get(id);
        if (run == null) {
            throw new IllegalArgumentException("Запуск с ID " + id + " не найден");
        }
        return run;
    }

    /**
     * Возвращает все запуски в отсортированном порядке.
     *
     * @return коллекция всех запусков
     */
    public Collection<Run> getAllRuns() {
        return runs.values();
    }

    /**
     * Удаляет запуск по идентификатору.
     *
     * @param id идентификатор удаляемого запуска
     * @throws IllegalArgumentException если запуск не найден
     */
    public void removeRun(long id) {
        if (runs.remove(id) == null) {
            throw new IllegalArgumentException("Запуск с ID " + id + " не найден");
        }
        System.out.println("✓ Запуск с ID " + id + " удален");
    }

    // блок про результаты

    /**
     * Создает новый результат и добавляет его в коллекцию.
     *
     * @param runId идентификатор запуска
     * @param param измеренный параметр
     * @param value значение параметра
     * @param unit единица измерения
     * @param comment комментарий к результату
     * @return созданный результат
     * @throws IllegalArgumentException если входные данные некорректны
     */
    public Result createResult(long runId, MeasurementParam param, double value,
                               String unit, String comment) {
        if (runId <= 0) {
            throw new IllegalArgumentException("ID запуска должен быть положительным");
        }
        if (param == null) {
            throw new IllegalArgumentException("Параметр не может быть null");
        }
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Единица измерения не может быть пустой");
        }

        //существование запуска
        if (!runs.containsKey(runId)) {
            throw new IllegalArgumentException("Запуск с ID " + runId + " не существует");
        }

        long id = generateId();
        Result result = new Result(id, runId, param, value, unit, comment != null ? comment : "");
        results.put(id, result);

        System.out.println("Результат успешно создан (ID: " + id + ")");
        return result;
    }

    /**
     * Получает результат по идентификатору.
     *
     * @param id идентификатор результата
     * @return найденный результат
     * @throws IllegalArgumentException если результат не найден
     */
    public Result getResult(long id) {
        Result result = results.get(id);
        if (result == null) {
            throw new IllegalArgumentException("Результат с ID " + id + " не найден");
        }
        return result;
    }

    /**
     * Возвращает все результаты в отсортированном порядке.
     *
     * @return коллекция всех результатов
     */
    public Collection<Result> getAllResults() {
        return results.values();
    }

    /**
     * Удаляет результат по идентификатору.
     *
     * @param id идентификатор удаляемого результата
     * @throws IllegalArgumentException если результат не найден
     */
    public void removeResult(long id) {
        if (results.remove(id) == null) {
            throw new IllegalArgumentException("Результат с ID " + id + " не найден");
        }
        System.out.println("Результат с ID " + id + " удален");
    }

    // общие методы

    /**
     * Выводит статистику по коллекциям.
     */
    public void printStats() {
        System.out.println("\nСтатистика коллекций");
        System.out.println("\nЭкспериментов: " + String.format("%2d", experiments.size()) + " ");
        System.out.println("\nЗапусков:      " + String.format("%2d", runs.size()) + " ");
        System.out.println("\nРезультатов:   " + String.format("%2d", results.size()) +" ");

    }

    /**
     * Очищает все коллекции.
     */
    public void clearAll() {
        experiments.clear();
        runs.clear();
        results.clear();
        System.out.println("Все коллекции очищены");
    }

    /**
     * Выводит сводку по экспериментам с их запусками и результатами.
     */
    public void experimentSummary() {
        System.out.println("\nСводка по экспериментам");


        if (experiments.isEmpty()) {
            System.out.println("Экспериментов не найдено.\n");
            return;
        }

        for (Experiment exp : experiments.values()) {
            System.out.println("\nЭксперимент [ID: " + exp.id + "]: " + exp.name);
            System.out.println("\nОписание: " + exp.description);
            System.out.println("\nВладелец: " + exp.owner);
            System.out.println("\nСоздан: " + exp.createdAt);

            List<Run> expRuns = runs.values().stream()
                    .filter(r -> r.experimentId == exp.id)
                    .toList();

            if (expRuns.isEmpty()) {
                System.out.println("Запусков: нет\n");
            } else {
                System.out.println("Запусков: " + expRuns.size());
                for (Run run : expRuns) {
                    System.out.println("[ID: " + run.id + "] " + run.name + " (оператор: " + run.operator + ")");

                    List<Result> runResults = results.values().stream()
                            .filter(r -> r.runId == run.id)
                            .toList();

                    System.out.println("Результатов: " + runResults.size());
                }
                System.out.println();
            }
        }
    }
}














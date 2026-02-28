package manager;

import model.*;

import java.util.Collection;
import java.util.TreeMap;
import java.util.Map;

/**
 * Менеджер для управления коллекциями экспериментов, запусков и результатов.
 * Использует TreeMap для автоматической сортировки по идентификатору.
 * Предоставляет методы для добавления, получения и вывода данных.
 */
public class CollectionManager {

    /** Коллекция экспериментов, отсортированная по идентификатору. */
    private final Map<Long, Experiment> experiments = new TreeMap<>();

    /** Коллекция запусков, отсортированная по идентификатору. */
    private final Map<Long, Run> runs = new TreeMap<>();

    /** Коллекция результатов, отсортированная по идентификатору. */
    private final Map<Long, Result> results = new TreeMap<>();

    /**
     * Конструктор для создания нового менеджера коллекций.
     * Инициализирует пустые TreeMap коллекции.
     */
    public CollectionManager() {
    }

    /**
     * Добавляет новый эксперимент в коллекцию.
     *
     * @param experiment д��бавляемый эксперимент
     * @throws IllegalArgumentException если эксперимент равен null
     */
    public void addExperiment(Experiment experiment) {
        if (experiment == null) {
            throw new IllegalArgumentException("Эксперимент не может быть null");
        }
        experiments.put(experiment.id, experiment);
    }

    /**
     * Получает эксперимент по идентификатору.
     *
     * @param id идентификатор искомого эксперимента
     * @return найденный эксперимент или null, если не найден
     */
    public Experiment getExperiment(long id) {
        return experiments.get(id);
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
     * Удаляет эксперимент по идентификатору.
     *
     * @param id идентификатор удаляемого эксперимента
     * @return true если эксперимент был удален, false если не найден
     */
    public boolean removeExperiment(long id) {
        return experiments.remove(id) != null;
    }

    /**
     * Добавляет новый запуск в коллекцию.
     *
     * @param run добавляемый запуск
     * @throws IllegalArgumentException если запуск равен null
     */
    public void addRun(Run run) {
        if (run == null) {
            throw new IllegalArgumentException("Запуск не может быть null");
        }
        runs.put(run.id, run);
    }

    /**
     * Получает запуск по идентификатору.
     *
     * @param id идентификатор искомого запуска
     * @return найденный запуск или null, если не найден
     */
    public Run getRun(long id) {
        return runs.get(id);
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
     * @return true если запуск был удален, false если не найден
     */
    public boolean removeRun(long id) {
        return runs.remove(id) != null;
    }

    /**
     * Добавляет новый результат в коллекцию.
     *
     * @param result добавляемый результат
     * @throws IllegalArgumentException если результат равен null
     */
    public void addResult(Result result) {
        if (result == null) {
            throw new IllegalArgumentException("Результат не может быть null");
        }
        results.put(result.id, result);
    }

    /**
     * Получает результат по идентификатору.
     *
     * @param id идентификатор искомого результата
     * @return найденный результат или null, если не найден
     */
    public Result getResult(long id) {
        return results.get(id);
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
     * @return true если результат был удален, false если не найден
     */
    public boolean removeResult(long id) {
        return results.remove(id) != null;
    }

    /**
     * Очищает все коллекции.
     * Удаляет все эксперименты, запуски и результаты.
     */
    public void clearAll() {
        experiments.clear();
        runs.clear();
        results.clear();
        System.out.println("✓ Все коллекции очищены.");
    }

    /**
     * Выводит статистику по коллекциям.
     * Показывает количество элементов в каждой коллекции.
     */
    public void printStats() {
        System.out.println("\nТекущая статистика");
        System.out.println("Экспериментов: " + experiments.size());
        System.out.println("Запусков:      " + runs.size());
        System.out.println("Результатов:   " + results.size());
    }
}
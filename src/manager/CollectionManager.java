/**
 * Класс управления коллекциями.
 * Отвечает за создание, хранение, получение и удаление:
 *    экспериментов
 *     их запусков
 *     результатов
 * Для хранения используется {@link TreeMap}, что обеспечивает
 * автоматическую сортировку по идентификатору.
 */
package manager;
import domain.*;
import validation.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.Optional;

public class CollectionManager {

    public CollectionManager() {
        this.currentExperimentId = 1;
        this.currentRunId = 1;
        this.currentResultId = 1;
    }

    /**
     * Коллекция экспериментов.
     */
    private final Map<Long, Experiment> experiments = new TreeMap<>();;

    /**
     * Коллекция запусков.
     */
    private final Map<Long, Run> runs = new TreeMap<>();

    /**
     * Коллекция результатов.
     */
    private final Map<Long, Result> results = new TreeMap<>();

    /**
     * Счетчик идентификаторов.
     */
    private long currentExperimentId;
    private long currentRunId;
    private long currentResultId;



    /**
     * Генерирует уникальный идентификатор.
     *
     * @return новый id
     */
    public Long generateExperimentId() { return currentExperimentId++; }
    public Long generateRunId() { return currentRunId++; }
    public Long generateResultId() { return currentResultId++; }

    /**
     * Создает новый эксперимент.
     *
     * @param name название
     * @param description описание
     * @param owner владелец
     * @return созданный эксперимент
     */
    public Experiment createExperiment(String name, String description, String owner) { return null; }

    /**
     * Добавляет эксперимент в коллекцию.
     */
    public void addExperiment(Experiment experiment) {
        experiments.put(experiment.getId(), experiment);
    }

    /**
     * Заменяет эксперимент в коллекции (полная замена объекта, например после {@code exp_update}).
     */
    public void updateExperiment(long id, Experiment updated) {
        Validator.requirePositive(id, "ID");
        if (updated.getId() != id) {
            throw new IllegalArgumentException("ID не совпадает с объектом эксперимента");
        }
        if (!experiments.containsKey(id)) {
            throw new IllegalArgumentException("Эксперимент не найден");
        }
        experiments.put(id, updated);
    }

    /**
     * Возвращает эксперимент по id.
     *
     * @param id идентификатор
     * @return эксперимент
     * @throws IllegalArgumentException если не найден
     */
    public Experiment getExperiment(long id) { return null; }


     /**
     * Получает эксперимент по id.
     */
    public Experiment getById(long id) {

        Validator.requirePositive(id, "ID");

        return Optional.ofNullable(experiments.get(id))
                .orElseThrow(() ->
                        new IllegalArgumentException("Эксперимент не найден"));}

    /**
     * Возвращает все эксперименты.
     */
    public Map<Long, Experiment> getAllExperiments() { return experiments; }
        

    /**
     * Удаляет эксперимент.
     */
    public void remove(long id) {

        Validator.requirePositive(id, "ID");

        if (experiments.remove(id) == null) {
            throw new IllegalArgumentException("Эксперимент не найден");
        }
    }


    /**
     * Создает новый запуск.
     * @param id уникальный идентификатор
     * @param experimentId id эксперимента
     * @param name название
     * @param operator оператор
     * @return созданный запуск
     */
    public Run createRun(long id, long experimentId, String name, String operator) { return null; }

    /**
     * Добавляет запуск в коллекцию.
     */
    public void addRun(Run run) {
        runs.put(run.getId(), run);
    }

    /**
     * Получает запуск по id.
     */
    public Run getRunById(long id) {

        Validator.requirePositive(id, "ID");

        return Optional.ofNullable(runs.get(id))
                .orElseThrow(() ->
                        new IllegalArgumentException("Эксперимент не найден"));}
                    
    /**
     * Возвращает список запусков по id эксперимента.
     *
     * @param experimentId id эксперимента
     * @return список запусков этого эксперимента
     */
    public Map<Long, Run> getRunsByExperimentId(long experimentId) {
        Validator.requirePositive(experimentId, "ID эксперимента");

        Map<Long, Run> result = new TreeMap<>();
        for (Run run : runs.values()) {
            if (run.getExperimentId() == experimentId) {
                result.put(run.getId(), run);
            }
        }
        return result;
    }

    /**
     * Обновляет запуск.
     */
    public Run updateRun(long id, String name, String operator) {

        Validator.requirePositive(id, "ID");

        Run run = getRunById(id);

        Validator.requireNonBlank(name, "Название");
        Validator.requireNonBlank(operator, "Оператор");

        return run;
    }

    /**
     * Возвращает все запуски.
     */
    public Map<Long, Run> getAllRuns() {
        return new TreeMap<>(runs);
    }

    /**
     * Удаляет запуск.
     */
    public void removeRun(long id) {

        Validator.requirePositive(id, "ID");

        if (experiments.remove(id) == null) {
            throw new IllegalArgumentException("Эксперимент не найден");
        }
    }
    /**
     * Создает результат измерения.
     *@param id id измерений
     * @param runId id запуска
     * @param param параметр
     * @param value значение
     * @param unit единица измерения
     * @param comment комментарий
     * @return результат
     */

    
    
     public Result createResult(long resId, long runId, MeasurementParam param,
                               double value, String unit, String comment) { return null;}

    /**
     * Добавляет результат в коллекцию.
     */
    public void addResult(Result result) {
        results.put(result.getId(), result);
    }


    /**
     * Возвращает все результатов
     */                           
    public Map<Long, Result> getAllResults() {
        return new TreeMap<>(results);
    }


    /**
    Получает результаты по id
     */                         
    public Result getResultById(long id) {

        Validator.requirePositive(id, "ID");

        return Optional.ofNullable(results.get(id))
                .orElseThrow(() ->
                        new IllegalArgumentException("Результат не найден"));
    }

    

    /**
     * Получает результат по id запуска
     * @param runId
     * @return результат по запуску
     */
    public Result getResultByRunId(long runId) {
        Validator.requirePositive(runId, "ID запуска");
        for (Result result : results.values()) {
            if (result.runId == runId) {
                return result;
            }
        }
        throw new IllegalArgumentException("Результат не найден");
    }

}



    

        













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
import model.*;

import java.util.Map;
import java.util.TreeMap;

public class CollectionManager {

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
    private long currentId;



    /**
     * Генерирует уникальный идентификатор.
     *
     * @return новый id
     */
    private long generateId() { return 0; }

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
     * Возвращает эксперимент по id.
     *
     * @param id идентификатор
     * @return эксперимент
     * @throws IllegalArgumentException если не найден
     */
    public Experiment getExperiment(long id) { return null; }

    /**
     * Создает новый запуск.
     *
     * @param experimentId id эксперимента
     * @param name название
     * @param operator оператор
     * @return созданный запуск
     */
    public Run createRun(long experimentId, String name, String operator) { return null; }

    /**
     * Создает результат измерения.
     *
     * @param runId id запуска
     * @param param параметр
     * @param value значение
     * @param unit единица измерения
     * @param comment комментарий
     * @return результат
     */
    public Result createResult(long runId, MeasurementParam param,
                               double value, String unit, String comment) { return null; }
}












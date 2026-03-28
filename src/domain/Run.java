package domain;

/**
 * Класс запуск эксперимента.
 * Каждый запуск связан с конкретным экспериментом
 * через идентификатор experimentId.
 */
public class Run {

    /**
     * Уникальный идентификатор запуска.
     */
    public final long id;

    /**
     * Идентификатор эксперимента.
     */
    public final long experimentId;

    /**
     * Название запуска.
     */
    public final String name;

    /**
     * Имя оператора.
     */
    private final String operator;

    /**
     * Создает новый запуск.
     *
     * @param id уникальный идентификатор
     * @param experimentId id эксперимента
     * @param name название запуска
     * @param operator оператор
     */
    public Run(long id, long experimentId, String name, String operator) {
        this.id = id;
        this.experimentId = experimentId;
        this.name = name;
        this.operator = operator;
    }

    /**
     * Возвращает id запуска.
     *
     * @return id
     */
    public long getId() { return id; }

    /**
     * Возвращает id эксперимента.
     *
     * @return experimentId
     */
    public long getExperimentId() { return experimentId; }
}





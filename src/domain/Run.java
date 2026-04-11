package domain;
import java.time.Instant;

/**
 * Класс запуск эксперимента.
 * Каждый запуск связан с конкретным экспериментом
 * через идентификатор experimentId.
 */
public final class Run implements Comparable<Run>{

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
     * Имя оператора
     */

    public final String operator;

    /**
     * Время создания.
     */
    public final Instant createdAt;


    /**
     * Создает новый запуск.
     *
     * @param id уникальный идентификатор
     * @param experimentId id эксперимента
     * @param name название запуска
     * @param operator оператор
     */
    public Run(long id, long experimentId, String name, String operator, Instant createdAt) {
        this.id = id;
        this.experimentId = experimentId;
        this.name = name;
        this.operator = operator;
        this.createdAt = Instant.now();
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


    @Override
    public int compareTo(Run o) {
        return Long.compare(this.id, o.id);
    }
    }






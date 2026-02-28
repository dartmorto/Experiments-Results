package model;

import java.time.Instant;

/**
 * Класс, представляющий запуск эксперимента.
 * Содержит информацию о выполнении конкретного эксперимента.
 */
public class Run implements Comparable<Run>{
    /** Уникальный идентификатор запуска. */
    public long id;
    /** Идентификатор эксперимента, к которому относится этот запуск. */
    public long experimentId;
    /** Название запуска. */
    public String name;
    /** Имя оператора, выполняющего запуск. */
    public String operator;
    /** Дата и время создания запуска. */
    public Instant createdAt;

    /**
     * Конструктор для создания нового запуска эксперимента.
     * Автоматически устанавливает текущее время в createdAt.
     *
     * @param id уникальный идентификатор запуска
     * @param experimentId идентификатор эксперимента
     * @param name название запуска
     * @param operator имя оператора
     */
    public Run(long id, long experimentId, String name,
               String operator, Instant createdAt){

    this.id = id;
    this.experimentId = experimentId;
    this.name = name;
    this.operator = operator;
    this.createdAt = Instant.now();}

    /**
     * Сравнивает запуски по идентификатору для сортировки по умолчанию.
     *
     * @param o другой запуск для сравнения
     * @return отрицательное число, если id меньше; 0, если равны; положительное число, если больше
     */
    @Override
    public int compareTo(Run o) {
    return Long.compare(this.id, o.id);
}

    /**
     * Возвращает строковое представление запуска.
     *
     * @return строка с информацией о запуске
     */
    @Override
public String toString() {
    return "Result{" +
            "id=" + id +
            ", experimentId='" + experimentId + '\'' +
            ", name='" + name + '\'' +
            ", operator='" + operator + '\'' +
            ", createdAt=" + createdAt +
            '}';
}
}





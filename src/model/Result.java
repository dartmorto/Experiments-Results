package model;

import java.time.Instant;

/**
 * Класс результат измерения.
 * Содержит значения параметров и данные об измерении.
 */
public final class Result implements Comparable<Result>{
    /** Уникальный идентификатор результата. */
    public  long id;
    /** Идентификатор запуска, к которому относится этот результат. */
    public long runId;
    /** Измеренный параметр. */
    public MeasurementParam param;
    /** Значение параметра. */
    public double value;
    /** Единица измерения параметра. */
    public String unit;
    /** Комментарий к результату измерения. */
    public String comment;
    /** Дата и время проведения измерения. */
    public Instant createdAt = Instant.now();

    /**
     * Конструктор для создания нового результата.
     * Автоматически устанавливает текущее время в createdAt.
     *
     * @param id уникальный идентификатор результата
     * @param runId идентификатор запуска
     * @param param измеренный параметр
     * @param value значение параметра
     * @param unit единица измерения
     * @param comment комментарий к результату
     */
    public Result(long id, long runId, String comment, double value,
                  String unit, Instant createdAt, MeasurementParam param) {
        this.id = id;
        this.runId = runId;
        this.param = param;
        this.comment = comment;
        this.value = value;
        this.unit = unit;
        this.createdAt = Instant.now();

    }

    public Result(long id, long runId, MeasurementParam param, double value, String unit, String s) {
    }

    /**
     * Сравнивает результаты по идентификатору для сортировки по умолчанию.
     *
     * @param o другой результат для сравнения
     * @return отрицательное число, если id меньше; 0, если равны; положительное число, если больше
     */
    @Override
public int compareTo(Result o) {
    return Long.compare(this.id, o.id);
}

    /**
     * Возвращает строковое представление результата.
     *
     * @return строка с информацией о результате
     */
    @Override
public String toString() {
    return "Result{" +
            "id=" + id +
            ", runId='" + runId + '\'' +
            ", comment='" + comment + '\'' +
            ", param='" + param + '\'' +
            ", unit='" + unit + '\'' +
            ", value='" + value + '\'' +
            ", createdAt=" + createdAt +
            '}';
}
}


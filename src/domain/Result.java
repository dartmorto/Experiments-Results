package domain;

import java.time.Instant;

/**
 * Класс результат измерения.
 * Содержит значения параметров и данные об измерении.
 */
public final class Result implements Comparable<Result> {

    /**
     * Уникальный идентификатор результата.
     */
    public final long id;
    /**
     * Идентификатор запуска, к которому относится этот результат.
     */
    public final long runId;
    /**
     * Измеренный параметр.
     */
    public MeasurementParam param;
    /**
     * Значение параметра.
     */
    public double value;
    /**
     * Единица измерения параметра.
     */
    public String unit;
    /**
     * Комментарий к результату измерения.
     */
    public String comment;
    /**
     * Дата и время проведения измерения.
     */
    public final Instant createdAt;

    /**
     * Конструктор для создания нового результата.
     * Автоматически устанавливает текущее время в createdAt.
     *
     * @param id      уникальный идентификатор результата
     * @param runId   идентификатор запуска
     * @param param   измеренный параметр
     * @param value   значение параметра
     * @param unit    единица измерения
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

    public long getId() {
        return id;
    }

    /**
     * Сравнивает результаты по идентификатору для сортировки по умолчанию.
     *
     * @return отрицательное число, если id меньше; 0, если равны; положительное число, если больше
     */


    /**
     * Получает ID запуска
     * @return id запуска
     */
    public long getRunId() {
        return runId;
    }

    /**
     * Получает параметры из перечня параметров
     * @return параметры запуска
     */
    public MeasurementParam getParam() {
        return param;
    }

    /** 
     * Получает значения*/ 
    public double getValue() {
        return value;
    }


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







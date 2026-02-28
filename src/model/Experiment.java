package model;

import java.time.Instant;

/**
 * Класс, представляющий экспериментальное исследование.
 * Содержит основную информацию об эксперименте и метаданные.
 */

public final class Experiment implements Comparable<Experiment> {
    /** Уникальный идентификатор эксперимента. */
    public long id;
    /** Название эксперимента. */
    public String name;
    /** Подробное описание целей и методологии эксперимента. */
    public String description;
    /** Имя владельца эксперимента. */
    public String owner;
    /** Дата и время создания эксперимента. */
    public Instant createdAt;
    /** Дата и время последнего обновления эксперимента. */
    public Instant updatedAt;


    /**
     * Конструктор для создания нового эксперимента.
     * Автоматически устанавливает текущие дату и время для createdAt и updatedAt.
     *
     * @param id уникальный идентификатор эксперимента
     * @param name название эксперимента
     * @param description описание эксперимента
     * @param owner владелец эксперимента
     */
    public Experiment(long id, String name, String description,
                      String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Сравнивает эксперименты по идентификатору для сортировки по умолчанию.
     *
     * @param o другой эксперимент для сравнения
     * @return отрицательное число, если id меньше; 0, если равны; положительное число, если больше
     */
    @Override
    public int compareTo(Experiment o) {
        return Long.compare(this.id, o.id);
    }

    /**
     * Возвращает строковое представление эксперимента.
     *
     * @return строка с информацией об эксперименте
     */
    @Override
    public String toString() {
        return "Experiment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}




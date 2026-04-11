package domain;

import java.time.Instant;

/**
 * Класс эксперимент.
 * Эксперимент является основной сущностью системы и содержит
 * информацию о названии, описании и владельце.
 * Экземпляры класса сравниваются по идентификатору.
 */
public final class Experiment implements Comparable<Experiment> {

    /**
     * Уникальный идентификатор эксперимента.
     */
    public final long id;

    /**
     * Название эксперимента.
     */
    public String name;

    /**
     * Описание эксперимента.
     */
    public String description;

    /**
     * Владелец эксперимента.
     */
    public final String owner;

    /**
     * Время создания.
     */
    public final Instant createdAt;
    /**
     * Время последнего обновления.
     */
    public final Instant updatedAt;

    /**
     * Создает новый эксперимент.
     *
     * @param id уникальный идентификатор
     * @param name название
     * @param description описание
     * @param owner владелец
     */
    public Experiment(long id, String name, String description, String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Возвращает идентификатор эксперимента.
     * @return id
     */
    public long getId() { return id; }

    public String getName() { return name; }

    public String getOwner() { return owner; }

    public String getDescription() { return description; }


    /**
     * Сравнение экспериментов по id.
     *
     * @param o другой эксперимент
     * @return результат сравнения
     */
    @Override
    public int compareTo(Experiment o) { return 0; }

    public Instant getCreatedAt() {
        return createdAt;
    }
}



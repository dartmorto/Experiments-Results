package model;

import java.time.Instant;

/**
 * Класс эксперимент.
 * Эксперимент является основной сущностью системы и содержит
 * информацию о названии, описании и владельце.
 * Экземпляры класса сравниваются по идентификатору.
 */
public class Experiment implements Comparable<Experiment> {

    /**
     * Уникальный идентификатор эксперимента.
     */
    private final long id;

    /**
     * Название эксперимента.
     */
    private String name;

    /**
     * Описание эксперимента.
     */
    private String description;

    /**
     * Владелец эксперимента.
     */
    private final String owner;

    /**
     * Время создания.
     */
    private final Instant createdAt;

    /**
     * Время последнего обновления.
     */
    private Instant updatedAt;

    /**
     * Создает новый эксперимент.
     *
     * @param id уникальный идентификатор
     * @param name название
     * @param description описание
     * @param owner владелец
     */
    public Experiment(long id, String name, String description, String owner) { }

    /**
     * Возвращает идентификатор эксперимента.
     * @return id
     */
    public long getId() { return id; }

    /**
     * Обновляет данные эксперимента.
     *
     * @param name новое название (если не null)
     * @param description новое описание (если не null)
     */
    public void update(String name, String description) { }

    /**
     * Сравнение экспериментов по id.
     *
     * @param o другой эксперимент
     * @return результат сравнения
     */
    @Override
    public int compareTo(Experiment o) { return 0; }
}



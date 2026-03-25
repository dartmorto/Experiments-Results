package validation;

/**
 * Класс для проверки корректности входных данных.
 * Содержит статические методы для валидации значений,
 * используемых при создании и обновлении сущностей.
 * Принцип работы: при некорректных данных выбрасывается
 * {@link IllegalArgumentException}.
 * Класс не имеет состояния и не может быть инстанцирован.
 */
public final class Validator {

    /**
     * Приватный конструктор запрещает создание экземпляров.
     */
    private Validator() {}

    /**
     * Проверяет, что значение положительное.
     *
     * @param value проверяемое значение
     * @param fieldName имя поля (для сообщения об ошибке)
     * @throws IllegalArgumentException если value <= 0
     */
    public static void requirePositive(long value, String fieldName) { }

    /**
     * Проверяет, что строка не равна null и не пустая.
     *
     * @param value строка
     * @param fieldName имя поля
     * @throws IllegalArgumentException если строка пустая или null
     */
    public static void requireNonBlank(String value, String fieldName) { }

    /**
     * Проверяет, что объект не равен null.
     *
     * @param obj объект
     * @param fieldName имя поля
     * @throws IllegalArgumentException если obj == null
     */
    public static void requireNotNull(Object obj, String fieldName) { }

    /**
     * Проверяет произвольное условие.
     *
     * @param condition условие
     * @param message сообщение об ошибке
     * @throws IllegalArgumentException если condition == false
     */
    public static void requireExists(boolean condition, String message) { }
}
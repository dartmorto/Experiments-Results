package manager;

public class Validator {

    public static void validateExperiment(String name, String description, String owner) {
        validateName(name);
        validateDescription(description);
        validateOwner(owner);
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название эксперимента не может быть пустым");
        }
    }

    public static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Описание эксперимента не может быть пустым");
        }
    }

    public static void validateOwner(String owner) {
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Владелец не может быть пустым");
        }
    }
}
import java.util.*;

public class Validator {

    public static void Validate(String name, String description, String comment, String unit) {
        if ( name == null || description = null || comment = null || unit = null) {
            throw new IllegalArgumentException("Поле не может быть пустым");
        }

    }
}
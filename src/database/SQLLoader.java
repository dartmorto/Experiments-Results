package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SQLLoader {

    public static String load(String fileName) {
        try (InputStream input = findResource(fileName)) {

            if (input != null) {
                return new String(input.readAllBytes(), "UTF-8");
            }

            File file = new File("resources", fileName);
            if (file.exists()) {
                try (InputStream fileInput = new FileInputStream(file)) {
                    return new String(fileInput.readAllBytes(), "UTF-8");
                }
            }

            throw new RuntimeException("SQL file not found: " + fileName);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static InputStream findResource(String fileName) {
        ClassLoader loader = SQLLoader.class.getClassLoader();
        InputStream input = loader.getResourceAsStream("sql/" + fileName);
        if (input != null) {
            return input;
        }
        return loader.getResourceAsStream(fileName);
    }
}

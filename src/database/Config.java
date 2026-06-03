package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = openConfig()) {
            if (input != null) {
                PROPERTIES.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки конфигурации", e);
        }
    }

    public static String get(String key) {
        String envValue = System.getenv(toEnvName(key));
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return PROPERTIES.getProperty(key);
    }

    private static InputStream openConfig() throws Exception {
        InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties");
        if (input != null) {
            return input;
        }

        File file = new File("config.properties");
        if (file.exists()) {
            return new FileInputStream(file);
        }

        return null;
    }

    private static String toEnvName(String key) {
        return key.toUpperCase().replace('.', '_');
    }
}

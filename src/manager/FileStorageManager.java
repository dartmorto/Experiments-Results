package manager;

import model.Experiment;
import model.Result;
import model.Run;

import java.io.*;
import java.time.Instant;

/**
 * Менеджер для сохранения и загрузки коллекций из JSON файла.
 * Использует FileReader и FileWriter для работы с файлами.
 * Обеспечивает обработку ошибок при чтении/записи.
 */
public class FileStorageManager {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.toString()))
            .registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, typeOfT, context) ->
                    Instant.parse(json.getAsString()))
            .setPrettyPrinting()
            .create();

    /**
     * Сохраняет коллекции менеджера в JSON файл.
     *
     * @param manager менеджер коллекций с данными для сохранения
     * @param filename путь к файлу для сохранения
     * @throws IOException если произошла ошибка при записи в файл
     */
    public static void saveToFile(CollectionManager manager, String filename)
            throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        try (FileWriter writer = new FileWriter(filename)) {
            JsonObject root = new JsonObject();

            // Сериализация экспериментов
            JsonArray experimentsArray = new JsonArray();
            for (Experiment exp : manager.getAllExperiments()) {
                experimentsArray.add(gson.toJsonTree(exp));
            }
            root.add("experiments", experimentsArray);

            // Сериализация запусков
            JsonArray runsArray = new JsonArray();
            for (Run run : manager.getAllRuns()) {
                runsArray.add(gson.toJsonTree(run));
            }
            root.add("runs", runsArray);

            // Сериализация результатов
            JsonArray resultsArray = new JsonArray();
            for (Result result : manager.getAllResults()) {
                resultsArray.add(gson.toJsonTree(result));
            }
            root.add("results", resultsArray);

            writer.write(gson.toJson(root));
            System.out.println("✓ Данные успешно сохранены в файл: " + filename);

        } catch (FileNotFoundException e) {
            System.err.println("✗ Ошибка: Файл не может быть создан. Проверьте путь и права доступа: " + filename);
            throw e;
        } catch (IOException e) {
            System.err.println("✗ Ошибка при записи в файл: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Загружает коллекции из JSON файла в менеджер.
     *
     * @param manager менеджер коллекций для заполнения данными
     * @param filename путь к файлу для загрузки
     * @throws IOException если произошла ошибка при чтении файла
     */
    public static void loadFromFile(CollectionManager manager, String filename)
            throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("✗ Ошибка: Файл н�� найден: " + filename);
            throw new FileNotFoundException("Файл " + filename + " не существует");
        }

        if (!file.canRead()) {
            System.err.println("✗ Ошибка: Недостаточно прав для чтения файла: " + filename);
            throw new IOException("Нет прав на чтение файла: " + filename);
        }

        try (FileReader reader = new FileReader(filename)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (!element.isJsonObject()) {
                throw new JsonSyntaxException("Некорректный формат JSON: ожидается объект на верхнем уровне");
            }

            JsonObject root = element.getAsJsonObject();

            // Десериализация экспериментов
            if (root.has("experiments")) {
                JsonArray experimentsArray = root.getAsJsonArray("experiments");
                for (JsonElement expElement : experimentsArray) {
                    try {
                        Experiment exp = gson.fromJson(expElement, Experiment.class);
                        manager.addExperiment(exp);
                    } catch (JsonSyntaxException e) {
                        System.err.println("⚠ Предупреждение: Ошибка при парсинге эксперимента: " + e.getMessage());
                    }
                }
            }

            // Десериализация запусков
            if (root.has("runs")) {
                JsonArray runsArray = root.getAsJsonArray("runs");
                for (JsonElement runElement : runsArray) {
                    try {
                        Run run = gson.fromJson(runElement, Run.class);
                        manager.addRun(run);
                    } catch (JsonSyntaxException e) {
                        System.err.println("⚠ Предупреждение: Ошибка при парсинге запуска: " + e.getMessage());
                    }
                }
            }

            // Десериализация результатов
            if (root.has("results")) {
                JsonArray resultsArray = root.getAsJsonArray("results");
                for (JsonElement resultElement : resultsArray) {
                    try {
                        Result result = gson.fromJson(resultElement, Result.class);
                        manager.addResult(result);
                    } catch (JsonSyntaxException e) {
                        System.err.println("⚠ Предупреждение: Ошибка при парсинге результата: " + e.getMessage());
                    }
                }
            }

            System.out.println("✓ Данные успешно загружены из файла: " + filename);

        } catch (FileNotFoundException e) {
            System.err.println("✗ Ошибка: Файл не найден: " + filename);
            throw e;
        } catch (JsonSyntaxException e) {
            System.err.println("✗ Ошибка: Некорректный формат JSON: " + e.getMessage());
            throw new IOException("Ошибка парсинга JSON", e);
        } catch (IOException e) {
            System.err.println("✗ Ошибка при чтении из файла: " + e.getMessage());
            throw e;
        }
    }
}

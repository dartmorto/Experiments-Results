package manager;

import model.*;
import java.io.*;
import java.time.Instant;
import java.util.*;

/**
 * Менеджер для работы с сохранением и загрузкой данных в JSON файл.
 * Использует FileReader для чтения и FileWriter для записи.
 * Данные загружаются из переменной окружения EXPERIMENTS_DATA_FILE.
 */
public class FileStorageManager {

    /**
     * Загружает данные из JSON файла в менеджер коллекций.
     * Имя файла берется из переменной окружения EXPERIMENTS_DATA_FILE.
     *
     * @param manager менеджер коллекций для заполнения
     * @param filename имя файла для загрузки (обычно из переменной окружения)
     * @throws IOException если возникает ошибка при чтении файла
     */
    public static void loadFromFile(CollectionManager manager, String filename) throws IOException {
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("⚠ Файл '" + filename + "' не существует. Работа с пустыми коллекциями.");
            return;
        }

        try (FileReader fileReader = new FileReader(file)) {
            StringBuilder content = new StringBuilder();
            char[] buffer = new char[1024];
            int charsRead;

            while ((charsRead = fileReader.read(buffer)) != -1) {
                content.append(buffer, 0, charsRead);
            }

            String jsonContent = content.toString().trim();

            if (jsonContent.isEmpty()) {
                System.out.println("⚠ Файл '" + filename + "' пуст. Работа с пустыми коллекциями.");
                return;
            }

            parseAndLoadJson(manager, jsonContent);

            System.out.println("✓ Данные успешно загружены из файла '" + filename + "'");
            System.out.println("  Экспериментов: " + manager.getAllExperiments().size());
            System.out.println("  Запусков: " + manager.getAllRuns().size());
            System.out.println("  Результатов: " + manager.getAllResults().size());

        } catch (IOException e) {
            System.err.println("✗ Ошибка при чтении файла: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Парсит JSON содержимое и загружает данные в менеджер.
     *
     * @param manager менеджер коллекций
     * @param jsonContent содержимое JSON файла в виде строки
     */
    private static void parseAndLoadJson(CollectionManager manager, String jsonContent) {
        try {
            // простой парсинг JSON без внешних библиотек
            JsonObject root = parseJson(jsonContent);

            // загрузка экспериментов
            JsonArray experimentsArray = (JsonArray) root.get("experiments");
            if (experimentsArray != null) {
                for (Object expObj : experimentsArray.elements) {
                    JsonObject expJson = (JsonObject) expObj;
                    loadExperiment(manager, expJson);
                }
            }

            // загрузка запусков
            JsonArray runsArray = (JsonArray) root.get("runs");
            if (runsArray != null) {
                for (Object runObj : runsArray.elements) {
                    JsonObject runJson = (JsonObject) runObj;
                    loadRun(manager, runJson);
                }
            }

            // загрузка результатов
            JsonArray resultsArray = (JsonArray) root.get("results");
            if (resultsArray != null) {
                for (Object resObj : resultsArray.elements) {
                    JsonObject resJson = (JsonObject) resObj;
                    loadResult(manager, resJson);
                }
            }

            // обновление currentId
            long maxId = 0;
            for (Experiment exp : manager.getAllExperiments()) {
                maxId = Math.max(maxId, exp.id);
            }
            for (Run run : manager.getAllRuns()) {
                maxId = Math.max(maxId, run.id);
            }
            for (Result result : manager.getAllResults()) {
                maxId = Math.max(maxId, result.id);
            }
            manager.setCurrentId(maxId + 1);

        } catch (Exception e) {
            System.err.println("⚠ Ошибка при парсинге JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Загружает один эксперимент из JSON объекта.
     *
     * @param manager менеджер коллекций
     * @param expJson JSON объект с данными эксперимента
     */
    private static void loadExperiment(CollectionManager manager, JsonObject expJson) {
        long id = ((Number) expJson.get("id")).longValue();
        String name = (String) expJson.get("name");
        String description = (String) expJson.get("description");
        String owner = (String) expJson.get("owner");

        Experiment experiment = new Experiment(id, name, description, owner);

        if (expJson.containsKey("createdAt")) {
            experiment.createdAt = Instant.parse((String) expJson.get("createdAt"));
        }
        if (expJson.containsKey("updatedAt")) {
            experiment.updatedAt = Instant.parse((String) expJson.get("updatedAt"));
        }

        manager.addExperimentDirect(experiment);
    }

    /**
     * Загружает один запуск из JSON объекта.
     *
     * @param manager менеджер коллекций
     * @param runJson JSON объект с данными запуска
     */
    private static void loadRun(CollectionManager manager, JsonObject runJson) {
        long id = ((Number) runJson.get("id")).longValue();
        long experimentId = ((Number) runJson.get("experimentId")).longValue();
        String name = (String) runJson.get("name");
        String operator = (String) runJson.get("operator");

        Run run = new Run(id, experimentId, name, operator);

        if (runJson.containsKey("createdAt")) {
            run.createdAt = Instant.parse((String) runJson.get("createdAt"));
        }

        manager.addRunDirect(run);
    }

    /**
     * Загружает один результат из JSON объекта.
     *
     * @param manager менеджер коллекций
     * @param resJson JSON объект с данными результата
     */
    private static void loadResult(CollectionManager manager, JsonObject resJson) {
        long id = ((Number) resJson.get("id")).longValue();
        long runId = ((Number) resJson.get("runId")).longValue();
        MeasurementParam param = MeasurementParam.valueOf((String) resJson.get("param"));
        double value = ((Number) resJson.get("value")).doubleValue();
        String unit = (String) resJson.get("unit");
        String comment = (String) resJson.get("comment");

        Result result = new Result(id, runId, param, value, unit, comment != null ? comment : "");

        if (resJson.containsKey("createdAt")) {
            result.createdAt = Instant.parse((String) resJson.get("createdAt"));
        }

        manager.addResultDirect(result);
    }

    /**
     * Сохраняет данные из менеджера коллекций в JSON файл.
     * Использует FileWriter для записи данных.
     *
     * @param manager менеджер коллекций для сохранения
     * @param filename имя файла для сохранения
     * @throws IOException если возникает ошибка при записи в файл
     */
    public static void saveToFile(CollectionManager manager, String filename) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"experiments\": [\n");

            // Сохраняем эксперименты
            Collection<Experiment> experiments = manager.getAllExperiments();
            int expCount = 0;
            for (Experiment exp : experiments) {
                json.append("    {\n");
                json.append("      \"id\": ").append(exp.id).append(",\n");
                json.append("      \"name\": ").append(toJsonString(exp.name)).append(",\n");
                json.append("      \"description\": ").append(toJsonString(exp.description)).append(",\n");
                json.append("      \"owner\": ").append(toJsonString(exp.owner)).append(",\n");
                json.append("      \"createdAt\": ").append(toJsonString(exp.createdAt.toString())).append(",\n");
                json.append("      \"updatedAt\": ").append(toJsonString(exp.updatedAt.toString())).append("\n");
                json.append("    }");
                expCount++;
                if (expCount < experiments.size()) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("  ],\n");

            // Сохраняем запуски
            json.append("  \"runs\": [\n");
            Collection<Run> runs = manager.getAllRuns();
            int runCount = 0;
            for (Run run : runs) {
                json.append("    {\n");
                json.append("      \"id\": ").append(run.id).append(",\n");
                json.append("      \"experimentId\": ").append(run.experimentId).append(",\n");
                json.append("      \"name\": ").append(toJsonString(run.name)).append(",\n");
                json.append("      \"operator\": ").append(toJsonString(run.operator)).append(",\n");
                json.append("      \"createdAt\": ").append(toJsonString(run.createdAt.toString())).append("\n");
                json.append("    }");
                runCount++;
                if (runCount < runs.size()) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("  ],\n");

            // Сохраняем результаты
            json.append("  \"results\": [\n");
            Collection<Result> results = manager.getAllResults();
            int resCount = 0;
            for (Result result : results) {
                json.append("    {\n");
                json.append("      \"id\": ").append(result.id).append(",\n");
                json.append("      \"runId\": ").append(result.runId).append(",\n");
                json.append("      \"param\": ").append(toJsonString(result.param.toString())).append(",\n");
                json.append("      \"value\": ").append(result.value).append(",\n");
                json.append("      \"unit\": ").append(toJsonString(result.unit)).append(",\n");
                json.append("      \"comment\": ").append(toJsonString(result.comment)).append(",\n");
                json.append("      \"createdAt\": ").append(toJsonString(result.createdAt.toString())).append("\n");
                json.append("    }");
                resCount++;
                if (resCount < results.size()) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("  ]\n");
            json.append("}\n");

            fileWriter.write(json.toString());
            fileWriter.flush();

            System.out.println("✓ Данные успешно сохранены в файл '" + filename + "'");
            System.out.println("  Экспериментов: " + experiments.size());
            System.out.println("  Запусков: " + runs.size());
            System.out.println("  Результатов: " + results.size());

        } catch (IOException e) {
            System.err.println("✗ Ошибка при записи в файл: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Преобразует строку в JSON строку с экранированием специальных символов.
     *
     * @param str строка для преобразования
     * @return JSON строка (с кавычками и экранированием)
     */
    private static String toJsonString(String str) {
        if (str == null) {
            return "null";
        }
        return "\"" + str
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }

    /**
     * Простой парсер JSON для работы без внешних библиотек.
     */
    private static JsonObject parseJson(String json) {
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Некорректный JSON формат");
        }

        JsonObject result = new JsonObject();
        String content = json.substring(1, json.length() - 1);

        int depth = 0;
        StringBuilder currentKey = new StringBuilder();
        StringBuilder currentValue = new StringBuilder();
        boolean inString = false;
        boolean parsingKey = true;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == ':' && depth == 0 && parsingKey) {
                    parsingKey = false;
                    currentKey = new StringBuilder(currentKey.toString().trim().replaceAll("^\"|\"$", ""));
                    currentValue = new StringBuilder();
                    continue;
                } else if (c == ',' && depth == 0 && !parsingKey) {
                    String key = currentKey.toString().trim();
                    String value = currentValue.toString().trim();
                    result.put(key, parseValue(value));
                    currentKey = new StringBuilder();
                    currentValue = new StringBuilder();
                    parsingKey = true;
                    continue;
                }
            }

            if (parsingKey) {
                currentKey.append(c);
            } else {
                currentValue.append(c);
            }
        }

        if (currentKey.length() > 0 && currentValue.length() > 0) {
            String key = currentKey.toString().trim().replaceAll("^\"|\"$", "");
            String value = currentValue.toString().trim();
            result.put(key, parseValue(value));
        }

        return result;
    }

    /**
     * Парсит JSON значение.
     */
    private static Object parseValue(String value) {
        value = value.trim();

        if (value.equals("null")) {
            return null;
        } else if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1)
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
                    .replace("\\t", "\t")
                    .replace("\\\\", "\\");
        } else if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else if (value.startsWith("[") && value.endsWith("]")) {
            return parseArray(value);
        } else if (value.startsWith("{") && value.endsWith("}")) {
            return parseJson(value);
        } else {
            try {
                if (value.contains(".")) {
                    return Double.parseDouble(value);
                } else {
                    return Long.parseLong(value);
                }
            } catch (NumberFormatException e) {
                return value;
            }
        }
    }

    /**
     * Парсит JSON массив.
     */
    private static JsonArray parseArray(String json) {
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]")) {
            throw new IllegalArgumentException("Некорректный JSON массив");
        }

        JsonArray result = new JsonArray();
        String content = json.substring(1, json.length() - 1).trim();

        if (content.isEmpty()) {
            return result;
        }

        int depth = 0;
        StringBuilder currentElement = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == ',' && depth == 0) {
                    if (currentElement.length() > 0) {
                        result.add(parseValue(currentElement.toString().trim()));
                        currentElement = new StringBuilder();
                    }
                    continue;
                }
            }

            currentElement.append(c);
        }

        if (currentElement.length() > 0) {
            result.add(parseValue(currentElement.toString().trim()));
        }

        return result;
    }

    /**
     * Простой класс для представления JSON объекта.
     */
    private static class JsonObject {
        private final Map<String, Object> data = new LinkedHashMap<>();

        void put(String key, Object value) {
            data.put(key, value);
        }

        Object get(String key) {
            return data.get(key);
        }

        boolean containsKey(String key) {
            return data.containsKey(key);
        }
    }

    /**
     * Простой класс для представления JSON массива.
     */
    private static class JsonArray {
        private final List<Object> elements = new ArrayList<>();

        void add(Object element) {
            elements.add(element);
        }
    }
}
package storage;

import domain.Experiment;
import domain.Result;
import domain.Run;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Проверяет корректность данных, прочитанных из файла.
 */
public final class FileValidator {

    public void validate(StorageData data) {
        if (data == null) {
            throw new IllegalArgumentException("файл не содержит снимок данных");
        }
        if (data.getFormatVersion() != StorageData.FORMAT_VERSION) {
            throw new IllegalArgumentException("неподдерживаемая версия формата: " + data.getFormatVersion());
        }

        Map<Long, Experiment> experiments = requireMap(data.getExperiments(), "experiments");
        Map<Long, Run> runs = requireMap(data.getRuns(), "runs");
        Map<Long, Result> results = requireMap(data.getResults(), "results");

        validateExperiments(experiments);
        validateRuns(runs, experiments);
        validateResults(results, runs);
    }

    private <T> Map<Long, T> requireMap(Map<Long, T> map, String name) {
        if (map == null) {
            throw new IllegalArgumentException("раздел " + name + " отсутствует");
        }
        return map;
    }

    private void validateExperiments(Map<Long, Experiment> experiments) {
        Set<Long> ids = new HashSet<>();

        for (Map.Entry<Long, Experiment> entry : experiments.entrySet()) {
            Long key = entry.getKey();
            Experiment experiment = entry.getValue();

            requirePositiveKey(key, "experiment");
            if (experiment == null) {
                throw new IllegalArgumentException("experiment.id=" + key + " содержит пустой объект");
            }
            requireSameId(key, experiment.getId(), "experiment");
            requireUnique(ids, experiment.getId(), "experiment");
            requireNonBlank(experiment.getName(), "поле name пустое у experiment.id=" + experiment.getId());
            requireNotNull(experiment.getDescription(), "поле description отсутствует у experiment.id=" + experiment.getId());
            requireNonBlank(experiment.getOwner(), "поле owner пустое у experiment.id=" + experiment.getId());
            requireInstant(experiment.getCreatedAt(), "поле createdAt отсутствует у experiment.id=" + experiment.getId());
            requireInstant(experiment.getUpdatedAt(), "поле updatedAt отсутствует у experiment.id=" + experiment.getId());
        }
    }

    private void validateRuns(Map<Long, Run> runs, Map<Long, Experiment> experiments) {
        Set<Long> ids = new HashSet<>();

        for (Map.Entry<Long, Run> entry : runs.entrySet()) {
            Long key = entry.getKey();
            Run run = entry.getValue();

            requirePositiveKey(key, "run");
            if (run == null) {
                throw new IllegalArgumentException("run.id=" + key + " содержит пустой объект");
            }
            requireSameId(key, run.getId(), "run");
            requireUnique(ids, run.getId(), "run");
            requirePositive(run.getExperimentId(), "поле experimentId некорректно у run.id=" + run.getId());
            if (!experiments.containsKey(run.getExperimentId())) {
                throw new IllegalArgumentException(
                        "run.experimentId=" + run.getExperimentId()
                                + " у run.id=" + run.getId()
                                + " ссылается на несуществующий experiment"
                );
            }
            requireNonBlank(run.getName(), "поле name пустое у run.id=" + run.getId());
            requireNonBlank(run.getOperator(), "поле operator пустое у run.id=" + run.getId());
            requireInstant(run.getCreatedAt(), "поле createdAt отсутствует у run.id=" + run.getId());
        }
    }

    private void validateResults(Map<Long, Result> results, Map<Long, Run> runs) {
        Set<Long> ids = new HashSet<>();

        for (Map.Entry<Long, Result> entry : results.entrySet()) {
            Long key = entry.getKey();
            Result result = entry.getValue();

            requirePositiveKey(key, "result");
            if (result == null) {
                throw new IllegalArgumentException("result.id=" + key + " содержит пустой объект");
            }
            requireSameId(key, result.getId(), "result");
            requireUnique(ids, result.getId(), "result");
            requirePositive(result.getRunId(), "поле runId некорректно у result.id=" + result.getId());
            if (!runs.containsKey(result.getRunId())) {
                throw new IllegalArgumentException(
                        "result.runId=" + result.getRunId()
                                + " у result.id=" + result.getId()
                                + " ссылается на несуществующий run"
                );
            }
            if (result.getParam() == null) {
                throw new IllegalArgumentException("поле param отсутствует у result.id=" + result.getId());
            }
            if (!Double.isFinite(result.getValue())) {
                throw new IllegalArgumentException("поле value некорректно у result.id=" + result.getId());
            }
            requireNonBlank(result.getUnit(), "поле unit пустое у result.id=" + result.getId());
            requireNotNull(result.getComment(), "поле comment отсутствует у result.id=" + result.getId());
            requireInstant(result.getCreatedAt(), "поле createdAt отсутствует у result.id=" + result.getId());
        }
    }

    private void requirePositiveKey(Long id, String entityName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(entityName + ".id некорректен: " + id);
        }
    }

    private void requireSameId(long key, long objectId, String entityName) {
        if (key != objectId) {
            throw new IllegalArgumentException(
                    entityName + ".id=" + objectId + " не совпадает с ключом " + key
            );
        }
    }

    private void requireUnique(Set<Long> ids, long id, String entityName) {
        if (!ids.add(id)) {
            throw new IllegalArgumentException(entityName + ".id=" + id + " повторяется");
        }
    }

    private void requirePositive(long value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireInstant(Instant value, String message) {
        requireNotNull(value, message);
    }
}

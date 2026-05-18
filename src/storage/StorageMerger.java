package storage;

import domain.Experiment;
import domain.Result;
import domain.Run;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

final class StorageMerger {

    StorageData merge(StorageData fileData, StorageData localData) {
        Map<Long, Experiment> experiments = new TreeMap<>(fileData.getExperiments());
        Map<Long, Run> runs = new TreeMap<>(fileData.getRuns());
        Map<Long, Result> results = new TreeMap<>(fileData.getResults());

        Map<Long, Long> experimentIds = mergeExperiments(experiments, localData.getExperiments());
        Map<Long, Long> runIds = mergeRuns(runs, localData.getRuns(), experimentIds);
        mergeResults(results, localData.getResults(), runIds);

        return new StorageData(experiments, runs, results);
    }

    private Map<Long, Long> mergeExperiments(Map<Long, Experiment> target,
                                             Map<Long, Experiment> localExperiments) {
        Map<Long, Long> idMap = new HashMap<>();
        for (Experiment local : localExperiments.values()) {
            long targetId = local.getId();
            Experiment existing = target.get(targetId);

            if (existing == null) {
                target.put(targetId, local);
            } else if (!sameExperiment(existing, local)) {
                targetId = nextFreeId(target, "эксперимента");
                target.put(targetId, copyExperiment(local, targetId));
            }
            idMap.put(local.getId(), targetId);
        }
        return idMap;
    }

    private Map<Long, Long> mergeRuns(Map<Long, Run> target,
                                      Map<Long, Run> localRuns,
                                      Map<Long, Long> experimentIds) {
        Map<Long, Long> idMap = new HashMap<>();
        for (Run local : localRuns.values()) {
            long experimentId = experimentIds.getOrDefault(local.getExperimentId(), local.getExperimentId());
            long targetId = local.getId();
            Run candidate = copyRun(local, targetId, experimentId);
            Run existing = target.get(targetId);

            if (existing == null) {
                target.put(targetId, candidate);
            } else if (!sameRun(existing, candidate)) {
                targetId = nextFreeId(target, "запуска");
                candidate = copyRun(local, targetId, experimentId);
                target.put(targetId, candidate);
            }
            idMap.put(local.getId(), targetId);
        }
        return idMap;
    }

    private void mergeResults(Map<Long, Result> target,
                              Map<Long, Result> localResults,
                              Map<Long, Long> runIds) {
        for (Result local : localResults.values()) {
            long runId = runIds.getOrDefault(local.getRunId(), local.getRunId());
            long targetId = local.getId();
            Result candidate = copyResult(local, targetId, runId);
            Result existing = target.get(targetId);

            if (existing == null) {
                target.put(targetId, candidate);
            } else if (!sameResult(existing, candidate)) {
                targetId = nextFreeId(target, "результата");
                candidate = copyResult(local, targetId, runId);
                target.put(targetId, candidate);
            }
        }
    }

    private Experiment copyExperiment(Experiment source, long id) {
        return new Experiment(
                id,
                source.getName(),
                source.getDescription(),
                source.getOwner(),
                source.getCreatedAt(),
                source.getUpdatedAt()
        );
    }

    private Run copyRun(Run source, long id, long experimentId) {
        return new Run(id, experimentId, source.getName(), source.getOperator(), source.getCreatedAt());
    }

    private Result copyResult(Result source, long id, long runId) {
        return new Result(
                id,
                runId,
                source.getComment(),
                source.getValue(),
                source.getUnit(),
                source.getCreatedAt(),
                source.getParam()
        );
    }

    private boolean sameExperiment(Experiment left, Experiment right) {
        return Objects.equals(left.getName(), right.getName())
                && Objects.equals(left.getDescription(), right.getDescription())
                && Objects.equals(left.getOwner(), right.getOwner())
                && Objects.equals(left.getCreatedAt(), right.getCreatedAt())
                && Objects.equals(left.getUpdatedAt(), right.getUpdatedAt());
    }

    private boolean sameRun(Run left, Run right) {
        return left.getExperimentId() == right.getExperimentId()
                && Objects.equals(left.getName(), right.getName())
                && Objects.equals(left.getOperator(), right.getOperator())
                && Objects.equals(left.getCreatedAt(), right.getCreatedAt());
    }

    private boolean sameResult(Result left, Result right) {
        return left.getRunId() == right.getRunId()
                && left.getParam() == right.getParam()
                && Double.compare(left.getValue(), right.getValue()) == 0
                && Objects.equals(left.getUnit(), right.getUnit())
                && Objects.equals(left.getComment(), right.getComment())
                && Objects.equals(left.getCreatedAt(), right.getCreatedAt());
    }

    private long nextFreeId(Map<Long, ?> data, String entityName) {
        if (data.isEmpty()) {
            return 1;
        }

        long maxId = new TreeMap<>(data).lastKey();
        if (maxId == Long.MAX_VALUE) {
            throw new IllegalArgumentException("ID " + entityName + " достиг максимального значения");
        }

        long id = maxId + 1;
        while (data.containsKey(id)) {
            if (id == Long.MAX_VALUE) {
                throw new IllegalArgumentException("ID " + entityName + " достиг максимального значения");
            }
            id++;
        }
        return id;
    }
}

package storage;

import domain.Experiment;
import domain.Result;
import domain.Run;
import manager.CollectionManager;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Снимок данных приложения для сохранения в файл.
 */
public final class StorageData implements Serializable {

    public static final int FORMAT_VERSION = 1;
    private static final long serialVersionUID = 1L;

    private final int formatVersion;
    private final Map<Long, Experiment> experiments;
    private final Map<Long, Run> runs;
    private final Map<Long, Result> results;

    public StorageData(Map<Long, Experiment> experiments,
                       Map<Long, Run> runs,
                       Map<Long, Result> results) {
        this.formatVersion = FORMAT_VERSION;
        this.experiments = new TreeMap<>(Objects.requireNonNull(experiments, "experiments"));
        this.runs = new TreeMap<>(Objects.requireNonNull(runs, "runs"));
        this.results = new TreeMap<>(Objects.requireNonNull(results, "results"));
    }

    public static StorageData from(CollectionManager manager) {
        return new StorageData(
                manager.getAllExperiments(),
                manager.getAllRuns(),
                manager.getAllResults()
        );
    }

    public int getFormatVersion() {
        return formatVersion;
    }

    public Map<Long, Experiment> getExperiments() {
        if (experiments == null) {
            return null;
        }
        return new TreeMap<>(experiments);
    }

    public Map<Long, Run> getRuns() {
        if (runs == null) {
            return null;
        }
        return new TreeMap<>(runs);
    }

    public Map<Long, Result> getResults() {
        if (results == null) {
            return null;
        }
        return new TreeMap<>(results);
    }
}

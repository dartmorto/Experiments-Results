package model;

import java.time.Instant;

public final class Result implements Comparable<Result>{
    public  long id;
    public long runId;
    public MeasurementParam param;
    public double value;
    public String unit;
    public String comment;
    public Instant createdAt = Instant.now();

    public Result(long id, long runId, String comment, double value,
                  String unit, Instant createdAt, MeasurementParam param) {
        this.id = id;
        this.runId = runId;
        this.param = param;
        this.comment = comment;
        this.value = value;
        this.unit = unit;
        this.createdAt = Instant.now();

    }

    @Override
public int compareTo(Result o) {
    return Long.compare(this.id, o.id);
}

    @Override
public String toString() {
    return "Result{" +
            "id=" + id +
            ", runId='" + runId + '\'' +
            ", comment='" + comment + '\'' +
            ", param='" + param + '\'' +
            ", unit='" + unit + '\'' +
            ", value='" + value + '\'' +
            ", createdAt=" + createdAt +
            '}';
}
}


package model;

import java.time.Instant;

public class Result {
    public  long id;
    public long runId;
    public MeasurementParam param;
    public double value;
    public String unit;
    public String comment;
    public Instant createdAt;
}

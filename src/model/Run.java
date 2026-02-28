package model;

import java.time.Instant;

public class Run implements Comparable<Run>{
    public long id;
    public long experimentId;
    public String name;
    public String operator;
    public Instant createdAt;

    public Run(long id, long experimentId, String name,
               String operator, Instant createdAt){

    this.id = id;
    this.experimentId = experimentId;
    this.name = name;
    this.operator = operator;
    this.createdAt = Instant.now();}

    @Override
    public int compareTo(Run o) {
    return Long.compare(this.id, o.id);
}

    @Override
public String toString() {
    return "Result{" +
            "id=" + id +
            ", experimentId='" + experimentId + '\'' +
            ", name='" + name + '\'' +
            ", operator='" + operator + '\'' +
            ", createdAt=" + createdAt +
            '}';
}
}





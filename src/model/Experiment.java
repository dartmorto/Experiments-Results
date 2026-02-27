package model;

import java.time.Instant;

public final class Experiment implements Comparable<Experiment> {
    public long id;
    public String name;
    public String description;
    public String owner;
    public Instant createdAt;
    public Instant updatedAt;

    public Experiment(long id, String name, String description,
                      String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Override
    public int compareTo(Experiment o) {
        return Long.compare(this.id, o.id);
    }

    @Override
    public String toString() {
        return "Experiment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}




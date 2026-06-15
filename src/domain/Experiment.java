package domain;

import java.io.Serializable;
import java.time.Instant;

/**
 * Experiment is the root entity that stores name, description and owner.
 */
public final class Experiment implements Comparable<Experiment>, Serializable {

    private static final long serialVersionUID = 204678L;

    private final long id;
    private final String name;
    private final String description;
    private final String ownerUsername;
    private final Instant createdAt;
    private final Instant updatedAt;
    private String targetSubstance;

    public Experiment(long id, String name, String description, String ownerUsername, String targetSubstance) {
        this(id, name, description, ownerUsername, Instant.now(), Instant.now(), targetSubstance);
    }

    public Experiment(long id,
                      String name,
                      String description,
                      String ownerUsername,
                      Instant createdAt,
                      Instant updatedAt,
                      String targetSubstance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerUsername = ownerUsername;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
        this.targetSubstance = targetSubstance;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getOwner() {
        return ownerUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getTargetSubstance() { return targetSubstance; }

    @Override
    public int compareTo(Experiment other) {
        return Long.compare(id, other.id);
    }
}

package io.jeegit.common.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.io.Serializable;
import java.util.UUID;

/**
 * Root base class of every jeegit entity.
 *
 * <p>Uses string UUID identifiers for portability across tenants and for
 * straightforward cross-database migration. Subclasses needing a shorter
 * natural key are free to override the {@code @Id} mapping.</p>
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(length = 64, nullable = false, updatable = false)
    protected String id;

    @PrePersist
    public void prePersistEnsureId() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNew() {
        return id == null || id.isBlank();
    }
}

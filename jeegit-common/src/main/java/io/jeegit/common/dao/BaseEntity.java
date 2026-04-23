package io.jeegit.common.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.io.Serializable;
import java.util.UUID;

/**
 * 所有实体的根基类。仅承担主键与"新记录"语义。
 * 主键统一使用 UUID 字符串，方便多租户与跨库迁移；
 * 需要短主键的场景可由子类覆盖 {@code @Id} 定义。
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

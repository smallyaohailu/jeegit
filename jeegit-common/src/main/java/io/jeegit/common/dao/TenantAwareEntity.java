package io.jeegit.common.dao;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * 租户感知实体基类。每一行业务数据都必须显式归属租户，
 * 以支撑架构宪章第 7 条"多租户隔离到底"。
 *
 * {@code tenantId} 在持久化前由服务层填充（通常取自 {@link io.jeegit.common.TenantContext}），
 * 不再允许 null 值。
 */
@MappedSuperclass
public abstract class TenantAwareEntity extends AuditableEntity {

    @Column(name = "tenant_id", length = 64, nullable = false, updatable = false)
    protected String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}

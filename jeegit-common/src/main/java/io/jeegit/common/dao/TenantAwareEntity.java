package io.jeegit.common.dao;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * Base class for every business entity that is tenant-scoped. Enforces a
 * non-null, non-updatable {@code tenant_id} column so the tenant boundary
 * cannot be silently widened after a row has been persisted.
 *
 * <p>Callers populate the tenant id from {@link io.jeegit.common.TenantContext}
 * in service-layer code.</p>
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

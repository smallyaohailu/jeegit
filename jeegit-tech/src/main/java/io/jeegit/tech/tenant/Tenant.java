package io.jeegit.tech.tenant;

import io.jeegit.common.dao.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 租户（Tenant）—— 平台内所有数据的隔离边界。
 * 本身不带 tenantId（它 <i>就是</i> 租户），因此继承 AuditableEntity 而非 TenantAwareEntity。
 */
@Entity
@Table(name = "jg_tenant")
public class Tenant extends AuditableEntity {

    @Column(name = "code", length = 64, nullable = false, unique = true)
    private String code;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    public Tenant() {
    }

    public Tenant(String id, String code, String name, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

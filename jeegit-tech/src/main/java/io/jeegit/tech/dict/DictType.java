package io.jeegit.tech.dict;

import io.jeegit.common.dao.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Dictionary type — a named catalogue (e.g. {@code MATTER_DISPATCH_RULE},
 * {@code DATA_SCOPE}). Used as the single home for drop-down options, rule
 * tables, and agent-visible configuration values.
 */
@Entity
@Table(name = "jg_dict_type",
        indexes = @Index(name = "uk_dict_type_code", columnList = "tenant_id,code", unique = true))
public class DictType extends TenantAwareEntity {

    @Column(name = "code", length = 64, nullable = false)
    private String code;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "system_flag", nullable = false)
    private boolean systemFlag;

    public DictType() {
    }

    public DictType(String code, String name, String description, boolean systemFlag) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.systemFlag = systemFlag;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isSystemFlag() { return systemFlag; }
    public void setSystemFlag(boolean systemFlag) { this.systemFlag = systemFlag; }
}

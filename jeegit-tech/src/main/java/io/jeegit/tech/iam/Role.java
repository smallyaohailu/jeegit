package io.jeegit.tech.iam;

import io.jeegit.common.dao.DataScope;
import io.jeegit.common.dao.TenantAwareEntity;
import jakarta.persistence.*;

/**
 * 角色（Role）—— 功能权限 + 数据权限的载体。
 * <p>
 * 与传统 RBAC 不同，本平台把"数据权限范围"声明在 {@link DataScope} 字段上，
 * 服务层在构造查询时据此生成 Specification，而不是在各处散落手写的 SQL 片段。
 * </p>
 */
@Entity
@Table(name = "jg_role",
        indexes = @Index(name = "uk_role_code", columnList = "tenant_id,code", unique = true))
public class Role extends TenantAwareEntity {

    @Column(name = "code", length = 64, nullable = false)
    private String code;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_scope", length = 32, nullable = false)
    private DataScope dataScope = DataScope.SELF;

    @Column(name = "custom_org_ids", length = 2000)
    private String customOrgIds;

    public Role() {
    }

    public Role(String code, String name, DataScope scope) {
        this.code = code;
        this.name = name;
        this.dataScope = scope;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DataScope getDataScope() { return dataScope; }
    public void setDataScope(DataScope dataScope) { this.dataScope = dataScope; }
    public String getCustomOrgIds() { return customOrgIds; }
    public void setCustomOrgIds(String customOrgIds) { this.customOrgIds = customOrgIds; }
}

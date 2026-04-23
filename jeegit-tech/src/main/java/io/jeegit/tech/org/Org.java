package io.jeegit.tech.org;

import io.jeegit.common.dao.TreeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 组织（Organization）—— 统一承载"公司 / 部门 / 团队"三种常见结构。
 * 通过 {@link #getType()} 区分，避免在代码里出现多棵并行的树。
 */
@Entity
@Table(name = "jg_org",
        indexes = {
                @Index(name = "idx_org_tenant", columnList = "tenant_id"),
                @Index(name = "idx_org_code", columnList = "tenant_id,code", unique = true),
                @Index(name = "idx_org_parent", columnList = "parent_id")
        })
public class Org extends TreeEntity {

    public enum Type {
        COMPANY,
        DEPARTMENT,
        TEAM
    }

    @Column(name = "code", length = 64, nullable = false)
    private String code;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "type", length = 32, nullable = false)
    @jakarta.persistence.Enumerated(jakarta.persistence.EnumType.STRING)
    private Type type = Type.DEPARTMENT;

    public Org() {
    }

    public Org(String code, String name, Type type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
}

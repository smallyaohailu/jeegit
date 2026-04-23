package io.jeegit.tech.iam;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * 用户（内部主体）。角色与权限走字符串集合，避免 MVP 阶段引入额外联表复杂度；
 * 后续可演进为独立的角色聚合根。
 */
@Entity
@Table(name = "jeegit_user", indexes = @Index(name = "idx_user_tenant", columnList = "tenantId"))
public class User {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, length = 64)
    private String tenantId;

    @Column(nullable = false, length = 128)
    private String username;

    @Column(length = 256)
    private String displayName;

    @Column(length = 256)
    private String department;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public User() {
    }

    public User(String id, String tenantId, String username, String displayName, String department) {
        this.id = id;
        this.tenantId = tenantId;
        this.username = username;
        this.displayName = displayName;
        this.department = department;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Instant getCreatedAt() { return createdAt; }
}

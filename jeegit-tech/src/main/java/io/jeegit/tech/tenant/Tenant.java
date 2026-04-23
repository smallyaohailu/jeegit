package io.jeegit.tech.tenant;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * 租户（Tenant）—— 平台内所有数据的隔离边界。
 * 架构宪章第 7 条：数据/模型配置/知识库/日志均按租户隔离。
 */
@Entity
@Table(name = "jeegit_tenant")
public class Tenant {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Tenant() {
    }

    public Tenant(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public Instant getCreatedAt() { return createdAt; }
}

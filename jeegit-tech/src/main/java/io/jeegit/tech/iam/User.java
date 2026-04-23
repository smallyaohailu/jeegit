package io.jeegit.tech.iam;

import io.jeegit.common.dao.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 用户（User）—— 平台身份主体。
 * {@code orgId} 指向用户所属的组织节点（见 {@link io.jeegit.tech.org.Org}），
 * 供数据权限计算使用。
 */
@Entity
@Table(name = "jg_user",
        indexes = {
                @Index(name = "uk_user_name", columnList = "tenant_id,username", unique = true),
                @Index(name = "idx_user_org", columnList = "tenant_id,org_id")
        })
public class User extends TenantAwareEntity {

    @Column(name = "username", length = 128, nullable = false)
    private String username;

    @Column(name = "display_name", length = 256)
    private String displayName;

    @Column(name = "org_id", length = 64)
    private String orgId;

    @Column(name = "email", length = 256)
    private String email;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    public User() {
    }

    public User(String username, String displayName, String orgId) {
        this.username = username;
        this.displayName = displayName;
        this.orgId = orgId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

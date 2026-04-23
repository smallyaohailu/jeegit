package io.jeegit.openapi.apikey;

import io.jeegit.common.dao.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * API key issued to a partner application on the Open Platform.
 *
 * <p>The raw secret is never persisted — only a {@code sha256} digest is. Partners identify
 * themselves with the {@code X-API-Key} header; the server hashes the presented value and looks it
 * up against this table.
 */
@Entity
@Table(
    name = "jg_api_key",
    indexes = {
      @Index(name = "uk_api_key_digest", columnList = "key_digest", unique = true),
      @Index(name = "idx_api_key_tenant", columnList = "tenant_id")
    })
public class ApiKey extends TenantAwareEntity {

  @Column(name = "key_digest", length = 128, nullable = false, unique = true)
  private String keyDigest;

  @Column(name = "name", length = 200, nullable = false)
  private String name;

  @Column(name = "owner", length = 128)
  private String owner;

  @Column(name = "enabled", nullable = false)
  private boolean enabled = true;

  public ApiKey() {}

  public ApiKey(String keyDigest, String name, String owner) {
    this.keyDigest = keyDigest;
    this.name = name;
    this.owner = owner;
  }

  public String getKeyDigest() {
    return keyDigest;
  }

  public void setKeyDigest(String keyDigest) {
    this.keyDigest = keyDigest;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}

package io.jeegit.ai.prompt;

import io.jeegit.common.dao.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Versioned prompt template.
 *
 * <p>AI_GOVERNANCE.md §6 mandates that prompts are versioned assets, not string literals. Each
 * (tenant, code, version) is immutable; callers resolve by {@code code} (latest) or by {@code code
 * + version} to pin to a specific release.
 */
@Entity
@Table(
    name = "jg_prompt_template",
    indexes = {
      @Index(
          name = "uk_prompt_tenant_code_ver",
          columnList = "tenant_id,code,version",
          unique = true),
      @Index(name = "idx_prompt_tenant_code", columnList = "tenant_id,code")
    })
public class PromptTemplate extends TenantAwareEntity {

  @Column(name = "code", length = 128, nullable = false)
  private String code;

  @Column(name = "version", nullable = false)
  private int version;

  @Column(name = "template", length = 8000, nullable = false)
  private String template;

  @Column(name = "model_key", length = 128)
  private String modelKey;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "published", nullable = false)
  private boolean published = false;

  public PromptTemplate() {}

  public PromptTemplate(String code, int version, String template) {
    this.code = code;
    this.version = version;
    this.template = template;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getModelKey() {
    return modelKey;
  }

  public void setModelKey(String modelKey) {
    this.modelKey = modelKey;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }
}

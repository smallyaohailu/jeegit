package io.jeegit.common.dao;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entity base class that adds:
 *
 * <ul>
 *   <li>{@link DeleteFlag} logical-delete marker
 *   <li>{@link RecordStatus} business status
 *   <li>Free-form {@code remarks}
 *   <li>Spring Data JPA auditing fields: created-by / created-at / updated-by / updated-at
 * </ul>
 *
 * The audit fields are populated automatically from an {@code AuditorAware<String>} bean that reads
 * the current actor from {@link io.jeegit.common.TenantContext}.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "del_flag", length = 16, nullable = false)
  protected DeleteFlag delFlag = DeleteFlag.NORMAL;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 16, nullable = false)
  protected RecordStatus status = RecordStatus.NORMAL;

  @Column(length = 500)
  protected String remarks;

  @CreatedBy
  @Column(name = "created_by", length = 128, updatable = false)
  protected String createdBy;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  protected Instant createdAt;

  @LastModifiedBy
  @Column(name = "updated_by", length = 128)
  protected String updatedBy;

  @LastModifiedDate
  @Column(name = "updated_at")
  protected Instant updatedAt;

  public DeleteFlag getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(DeleteFlag delFlag) {
    this.delFlag = delFlag;
  }

  public RecordStatus getStatus() {
    return status;
  }

  public void setStatus(RecordStatus status) {
    this.status = status;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public boolean isDeleted() {
    return delFlag == DeleteFlag.DELETED;
  }
}

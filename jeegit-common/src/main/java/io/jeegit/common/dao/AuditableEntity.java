package io.jeegit.common.dao;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * 数据实体基类。扩展自 {@link BaseEntity}，增加：
 * <ul>
 *   <li>逻辑删除标志 {@link DeleteFlag}</li>
 *   <li>记录状态 {@link RecordStatus}</li>
 *   <li>备注 {@code remarks}</li>
 *   <li>Spring Data JPA 审计字段：创建人 / 创建时间 / 更新人 / 更新时间</li>
 * </ul>
 * 审计字段由 {@code AuditorAware<String>} 自动填充（见 JpaAuditingConfig）。
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

    public DeleteFlag getDelFlag() { return delFlag; }
    public void setDelFlag(DeleteFlag delFlag) { this.delFlag = delFlag; }

    public RecordStatus getStatus() { return status; }
    public void setStatus(RecordStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public String getUpdatedBy() { return updatedBy; }
    public Instant getUpdatedAt() { return updatedAt; }

    public boolean isDeleted() {
        return delFlag == DeleteFlag.DELETED;
    }
}

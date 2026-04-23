package io.jeegit.business.matter;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * 事项（Matter）—— 政务事项受理的核心聚合根。
 * 同样可被中小企业复用为"内部事项/工单"。
 */
@Entity
@Table(name = "jeegit_matter",
        indexes = {
                @Index(name = "idx_matter_tenant", columnList = "tenantId"),
                @Index(name = "idx_matter_status", columnList = "status")
        })
public class Matter {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, length = 64)
    private String tenantId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 64)
    private String category;

    @Column(length = 4000)
    private String description;

    @Column(length = 128)
    private String applicantId;

    @Column(length = 64)
    private String assignedDepartment;

    /** DRAFT / SUBMITTED / DISPATCHED / PENDING_APPROVAL / APPROVED / REJECTED / CLOSED */
    @Column(nullable = false, length = 32)
    private String status = "DRAFT";

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    public Matter() {
    }

    public Matter(String id, String tenantId, String title, String category,
                  String description, String applicantId) {
        this.id = id;
        this.tenantId = tenantId;
        this.title = title;
        this.category = category;
        this.description = description;
        this.applicantId = applicantId;
        this.status = "SUBMITTED";
    }

    @PreUpdate
    public void touch() {
        this.updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getApplicantId() { return applicantId; }
    public void setApplicantId(String applicantId) { this.applicantId = applicantId; }
    public String getAssignedDepartment() { return assignedDepartment; }
    public void setAssignedDepartment(String assignedDepartment) { this.assignedDepartment = assignedDepartment; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}

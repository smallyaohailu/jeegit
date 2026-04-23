package io.jeegit.business.matter;

import io.jeegit.common.dao.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Matter — the generic "intake / approval / routing" aggregate root. Fits both e-government intake
 * cases and internal corporate tickets.
 */
@Entity
@Table(
    name = "jg_matter",
    indexes = {
      @Index(name = "idx_matter_tenant", columnList = "tenant_id"),
      @Index(name = "idx_matter_status", columnList = "matter_status"),
      @Index(name = "idx_matter_org", columnList = "tenant_id,org_id")
    })
public class Matter extends TenantAwareEntity {

  public enum MatterStatus {
    DRAFT,
    SUBMITTED,
    DISPATCHED,
    PENDING_APPROVAL,
    APPROVED,
    REJECTED,
    CLOSED
  }

  @Column(name = "title", length = 200, nullable = false)
  private String title;

  @Column(name = "category", length = 64)
  private String category;

  @Column(name = "description", length = 4000)
  private String description;

  @Column(name = "applicant_id", length = 128)
  private String applicantId;

  @Column(name = "org_id", length = 64)
  private String orgId;

  @Column(name = "assigned_department", length = 128)
  private String assignedDepartment;

  @Enumerated(EnumType.STRING)
  @Column(name = "matter_status", length = 32, nullable = false)
  private MatterStatus matterStatus = MatterStatus.DRAFT;

  public Matter() {}

  public Matter(String title, String category, String description, String applicantId) {
    this.title = title;
    this.category = category;
    this.description = description;
    this.applicantId = applicantId;
    this.matterStatus = MatterStatus.SUBMITTED;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getApplicantId() {
    return applicantId;
  }

  public void setApplicantId(String applicantId) {
    this.applicantId = applicantId;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getAssignedDepartment() {
    return assignedDepartment;
  }

  public void setAssignedDepartment(String assignedDepartment) {
    this.assignedDepartment = assignedDepartment;
  }

  public MatterStatus getMatterStatus() {
    return matterStatus;
  }

  public void setMatterStatus(MatterStatus matterStatus) {
    this.matterStatus = matterStatus;
  }
}

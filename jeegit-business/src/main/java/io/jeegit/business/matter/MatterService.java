package io.jeegit.business.matter;

import io.jeegit.common.TenantContext;
import io.jeegit.common.dao.DataScope;
import io.jeegit.common.event.DomainEvent;
import io.jeegit.common.event.EventBus;
import io.jeegit.tech.iam.DataScopeSpecifications;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatterService {

  public static final String TOPIC_MATTER_SUBMITTED = "jeegit.matter.submitted.v1";
  public static final String TOPIC_MATTER_DISPATCHED = "jeegit.matter.dispatched.v1";

  private final MatterRepository repository;
  private final DataScopeSpecifications dataScopeSpecs;
  private final EventBus eventBus;

  public MatterService(
      MatterRepository repository, DataScopeSpecifications dataScopeSpecs, EventBus eventBus) {
    this.repository = repository;
    this.dataScopeSpecs = dataScopeSpecs;
    this.eventBus = eventBus;
  }

  @Transactional
  public Matter submit(String title, String category, String description, String applicantId) {
    Matter m = new Matter(title, category, description, applicantId);
    m.setTenantId(TenantContext.tenant());
    Matter saved = repository.save(m);
    eventBus.publish(
        DomainEvent.of(
            TOPIC_MATTER_SUBMITTED,
            saved.getTenantId(),
            Map.of(
                "matterId",
                saved.getId(),
                "title",
                saved.getTitle(),
                "category",
                saved.getCategory() == null ? "" : saved.getCategory(),
                "applicantId",
                saved.getApplicantId() == null ? "" : saved.getApplicantId())));
    return saved;
  }

  @Transactional(readOnly = true)
  public Matter get(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("matter not found: " + id));
  }

  @Transactional(readOnly = true)
  public List<Matter> listForCurrentTenant() {
    return repository.findByTenantIdOrderByCreatedAtDesc(TenantContext.tenant());
  }

  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<Matter> pageForCurrentTenant(
      org.springframework.data.domain.Pageable pageable) {
    return repository.findByTenantId(TenantContext.tenant(), pageable);
  }

  /**
   * List matters visible to the caller under the supplied data scope. The caller (REST layer or
   * agent) decides which {@code scope} / {@code anchorOrgId} apply; the service does not silently
   * narrow them.
   */
  @Transactional(readOnly = true)
  public List<Matter> listInScope(
      String currentUserId, String currentUserOrgId, DataScope scope, String customOrgIds) {
    Specification<Matter> spec =
        dataScopeSpecs.build(
            TenantContext.tenant(), currentUserId, currentUserOrgId, scope, customOrgIds);
    return repository.findAll(spec);
  }

  @Transactional
  public Matter assignDepartment(String id, String department, Matter.MatterStatus nextStatus) {
    Matter m = get(id);
    m.setAssignedDepartment(department);
    if (nextStatus != null) {
      m.setMatterStatus(nextStatus);
    }
    Matter saved = repository.save(m);
    if (nextStatus == Matter.MatterStatus.DISPATCHED) {
      eventBus.publish(
          DomainEvent.of(
              TOPIC_MATTER_DISPATCHED,
              saved.getTenantId(),
              Map.of(
                  "matterId",
                  saved.getId(),
                  "department",
                  saved.getAssignedDepartment() == null ? "" : saved.getAssignedDepartment())));
    }
    return saved;
  }
}

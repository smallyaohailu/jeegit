package io.jeegit.business.matter;

import io.jeegit.common.TenantContext;
import io.jeegit.common.dao.DataScope;
import io.jeegit.tech.iam.DataScopeSpecifications;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatterService {

  private final MatterRepository repository;
  private final DataScopeSpecifications dataScopeSpecs;

  public MatterService(MatterRepository repository, DataScopeSpecifications dataScopeSpecs) {
    this.repository = repository;
    this.dataScopeSpecs = dataScopeSpecs;
  }

  @Transactional
  public Matter submit(String title, String category, String description, String applicantId) {
    Matter m = new Matter(title, category, description, applicantId);
    m.setTenantId(TenantContext.tenant());
    return repository.save(m);
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
    return repository.save(m);
  }
}

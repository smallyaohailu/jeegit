package io.jeegit.tech.audit;

import io.jeegit.common.TenantContext;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Write gateway for audit entries. Intentionally exposes only {@code record} / query methods —
 * there is no public {@code update} or {@code delete} operation.
 */
@Service
public class AuditService {

  private final AuditLogRepository repository;

  public AuditService(AuditLogRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public AuditLog record(AuditLog log) {
    if (log.getId() == null) {
      log.setId(UUID.randomUUID().toString());
    }
    if (log.getTenantId() == null) {
      log.setTenantId(TenantContext.tenant());
    }
    if (log.getActorId() == null) {
      log.setActorId(TenantContext.actor());
    }
    return repository.save(log);
  }

  @Transactional(readOnly = true)
  public List<AuditLog> listForTenant(String tenantId) {
    return repository.findByTenantIdOrderByOccurredAtDesc(tenantId);
  }

  @Transactional(readOnly = true)
  public List<AuditLog> listByTrace(String traceId) {
    return repository.findByTraceIdOrderByOccurredAtAsc(traceId);
  }
}

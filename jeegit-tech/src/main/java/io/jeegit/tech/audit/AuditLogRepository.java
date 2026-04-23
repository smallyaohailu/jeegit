package io.jeegit.tech.audit;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
  List<AuditLog> findByTenantIdOrderByOccurredAtDesc(String tenantId);

  List<AuditLog> findByTraceIdOrderByOccurredAtAsc(String traceId);
}

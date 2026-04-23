package io.jeegit.tech.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
    List<AuditLog> findByTenantIdOrderByOccurredAtDesc(String tenantId);
    List<AuditLog> findByTraceIdOrderByOccurredAtAsc(String traceId);
}

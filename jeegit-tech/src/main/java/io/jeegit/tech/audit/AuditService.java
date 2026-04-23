package io.jeegit.tech.audit;

import io.jeegit.common.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 审计服务。平台内所有需要留痕的动作都必须通过本服务写入。
 * 实现上严格遵循 append-only：只提供 write/query，没有 update/delete。
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

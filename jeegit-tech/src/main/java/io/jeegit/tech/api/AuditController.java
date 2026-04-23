package io.jeegit.tech.api;

import io.jeegit.common.ApiResponse;
import io.jeegit.tech.audit.AuditLog;
import io.jeegit.tech.audit.AuditService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {

  private final AuditService auditService;

  public AuditController(AuditService auditService) {
    this.auditService = auditService;
  }

  @GetMapping("/tenants/{tenantId}")
  public ApiResponse<List<AuditLog>> listForTenant(@PathVariable String tenantId) {
    return ApiResponse.ok(auditService.listForTenant(tenantId));
  }

  @GetMapping("/traces/{traceId}")
  public ApiResponse<List<AuditLog>> listByTrace(@PathVariable String traceId) {
    return ApiResponse.ok(auditService.listByTrace(traceId));
  }
}

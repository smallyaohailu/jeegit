package io.jeegit.business.matter;

import io.jeegit.ai.tool.Tool;
import io.jeegit.common.TenantContext;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * {@code matter.approve} tool — records an approval decision on a matter. Unlike {@link
 * MatterDispatchTool}, approval is declared HIGH-risk so the HITL guard kicks in and the runtime
 * will refuse automatic execution.
 */
@Component
public class MatterApprovalTool implements Tool {

  private final MatterService matterService;

  public MatterApprovalTool(MatterService matterService) {
    this.matterService = matterService;
  }

  @Override
  public String name() {
    return "matter.approve";
  }

  @Override
  public String description() {
    return "Record an approval decision on a matter (HIGH-risk — human-in-the-loop).";
  }

  @Override
  public String riskLevel() {
    return "HIGH";
  }

  @Override
  public Map<String, Object> execute(Map<String, Object> params) {
    String matterId = (String) params.get("matterId");
    boolean approved = Boolean.TRUE.equals(params.get("approved"));
    String department = (String) params.getOrDefault("department", "");
    if (matterId == null) {
      throw new IllegalArgumentException("matter.approve requires matterId");
    }
    String tenantId = (String) params.getOrDefault("tenantId", TenantContext.tenant());
    TenantContext.setTenant(tenantId);

    Matter.MatterStatus next =
        approved ? Matter.MatterStatus.APPROVED : Matter.MatterStatus.REJECTED;
    String targetDept = department.isEmpty() ? null : department;
    Matter updated = matterService.assignDepartment(matterId, targetDept, next);
    return Map.of(
        "matterId", updated.getId(),
        "status", updated.getMatterStatus().name(),
        "department",
            updated.getAssignedDepartment() == null ? "" : updated.getAssignedDepartment());
  }
}

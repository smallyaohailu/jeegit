package io.jeegit.business.matter;

import io.jeegit.ai.tool.Tool;
import io.jeegit.common.TenantContext;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * {@code matter.dispatch} tool — the only way an agent can change how a matter is routed.
 * Cross-module writes therefore always flow through the documented application service
 * (Architecture Charter §6) with an explicit risk declaration (AI_GOVERNANCE.md §2).
 */
@Component
public class MatterDispatchTool implements Tool {

  private final MatterService matterService;

  public MatterDispatchTool(MatterService matterService) {
    this.matterService = matterService;
  }

  @Override
  public String name() {
    return "matter.dispatch";
  }

  @Override
  public String description() {
    return "Route a matter to a named department and update its status. "
        + "Callable only by agents that hold this tool on their allow-list.";
  }

  @Override
  public String riskLevel() {
    return "MEDIUM";
  }

  @Override
  public Map<String, Object> execute(Map<String, Object> params) {
    String matterId = (String) params.get("matterId");
    String department = (String) params.get("department");
    String nextRaw = (String) params.getOrDefault("status", Matter.MatterStatus.DISPATCHED.name());
    if (matterId == null || department == null) {
      throw new IllegalArgumentException("matter.dispatch requires matterId and department");
    }
    String tenantId = (String) params.getOrDefault("tenantId", TenantContext.tenant());
    TenantContext.setTenant(tenantId);

    Matter.MatterStatus next;
    try {
      next = Matter.MatterStatus.valueOf(nextRaw);
    } catch (IllegalArgumentException ex) {
      next = Matter.MatterStatus.DISPATCHED;
    }
    Matter updated = matterService.assignDepartment(matterId, department, next);
    return Map.of(
        "matterId", updated.getId(),
        "department", updated.getAssignedDepartment(),
        "status", updated.getMatterStatus().name());
  }
}

package io.jeegit.business.matter;

import io.jeegit.ai.tool.Tool;
import io.jeegit.common.TenantContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * matter.dispatch 工具 —— Agent 唯一能修改事项分派结果的入口。
 * 架构宪章第 6 条：跨模块调用走"应用服务接口"。
 * AI_GOVERNANCE.md §2：工具白名单授权 + 风险等级声明。
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
        return "将事项分派给指定部门并更新状态（仅 Agent 通过白名单授权后可调用）";
    }

    @Override
    public String riskLevel() {
        return "MEDIUM";
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String matterId = (String) params.get("matterId");
        String department = (String) params.get("department");
        String nextStatus = (String) params.getOrDefault("status", "DISPATCHED");
        if (matterId == null || department == null) {
            throw new IllegalArgumentException("matter.dispatch requires matterId and department");
        }
        String tenantId = (String) params.getOrDefault("tenantId", TenantContext.tenant());
        TenantContext.setTenant(tenantId);

        Matter updated = matterService.assignDepartment(matterId, department, nextStatus);
        return Map.of(
                "matterId", updated.getId(),
                "department", updated.getAssignedDepartment(),
                "status", updated.getStatus()
        );
    }
}

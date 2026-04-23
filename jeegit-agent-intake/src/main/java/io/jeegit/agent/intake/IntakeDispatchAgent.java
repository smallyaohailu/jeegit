package io.jeegit.agent.intake;

import io.jeegit.ai.agent.Agent;
import io.jeegit.ai.agent.AgentDefinition;
import io.jeegit.ai.agent.AgentRequest;
import io.jeegit.ai.agent.AgentResponse;
import io.jeegit.ai.hitl.HitlPolicy;
import io.jeegit.ai.model.ModelGateway;
import io.jeegit.ai.model.ModelRequest;
import io.jeegit.ai.model.ModelResponse;
import io.jeegit.ai.tool.Tool;
import io.jeegit.ai.tool.ToolRegistry;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 受理分派 Agent（示范）。
 * 职责：根据事项标题/分类/描述，结合规则 + 模型提示，推荐分派部门，
 *       并通过 {@code matter.dispatch} 工具完成分派。
 *
 * 遵循：
 *   - AI_GOVERNANCE §2 权限最小化：只能调用白名单中的工具
 *   - AI_GOVERNANCE §3 风险分级：MEDIUM，默认策略 ON_HIGH_RISK
 *   - MVP_SCOPE §4 验收项：返回可解释推理 + 审计日志 ID
 */
@Component
public class IntakeDispatchAgent implements Agent {

    public static final String AGENT_ID = "agent.intake.dispatch";
    private static final Set<String> ALLOWED_TOOLS = Set.of("matter.dispatch");

    private final ToolRegistry toolRegistry;
    private final ModelGateway modelGateway;

    public IntakeDispatchAgent(ToolRegistry toolRegistry, ModelGateway modelGateway) {
        this.toolRegistry = toolRegistry;
        this.modelGateway = modelGateway;
    }

    @Override
    public AgentDefinition definition() {
        return new AgentDefinition(
                AGENT_ID,
                "default",
                "system",
                "政务事项受理分派 Agent：根据事项内容推荐部门并完成分派",
                Set.of("ROLE_DISPATCHER"),
                ALLOWED_TOOLS,
                "MEDIUM",
                HitlPolicy.ON_HIGH_RISK
        );
    }

    @Override
    public AgentResponse handle(AgentRequest request) {
        String matterId = str(request.input().get("matterId"));
        String title = str(request.input().get("title"));
        String category = str(request.input().get("category"));
        String description = str(request.input().get("description"));

        if (matterId == null || matterId.isBlank()) {
            return AgentResponse.denied("缺少 matterId，无法分派。", null);
        }

        String department = decideDepartment(category, title, description);
        String ruleExplanation = "规则引擎：基于分类 '" + category + "' 与关键词命中 → " + department;

        ModelResponse llm = modelGateway.invoke(ModelRequest.of(
                request.tenantId(),
                "default",
                "事项标题：" + title + "\n分类：" + category
                        + "\n请给出一句话的分派理由，保持中立与可审计。"
        ));

        Tool tool = toolRegistry.find("matter.dispatch")
                .orElseThrow(() -> new IllegalStateException("matter.dispatch tool not available"));

        if (!ALLOWED_TOOLS.contains(tool.name())) {
            return AgentResponse.denied("Agent 未被授予工具 " + tool.name(), null);
        }

        Map<String, Object> toolOut = tool.execute(Map.of(
                "matterId", matterId,
                "department", department,
                "tenantId", request.tenantId(),
                "status", "DISPATCHED"
        ));

        Map<String, Object> decision = new LinkedHashMap<>();
        decision.put("matterId", matterId);
        decision.put("department", department);
        decision.put("toolResult", toolOut);

        String reasoning = ruleExplanation + " | 模型补充理由：" + llm.content();
        return AgentResponse.executed(decision, reasoning, null);
    }

    private String decideDepartment(String category, String title, String description) {
        String text = String.join(" ",
                n(category), n(title), n(description)).toLowerCase();
        if (text.contains("税") || text.contains("tax")) return "税务局";
        if (text.contains("社保") || text.contains("医保") || text.contains("social")) return "社保局";
        if (text.contains("工商") || text.contains("营业执照") || text.contains("business")) return "市场监督管理局";
        if (text.contains("户籍") || text.contains("身份证") || text.contains("civil")) return "公安局户政科";
        if (text.contains("投诉") || text.contains("complaint")) return "信访办";
        return "综合受理窗口";
    }

    private String str(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private String n(String s) {
        return s == null ? "" : s;
    }
}

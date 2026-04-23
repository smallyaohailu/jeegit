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
import io.jeegit.common.TenantContext;
import io.jeegit.tech.dict.DictItem;
import io.jeegit.tech.dict.DictService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 受理分派 Agent（示范实现）。
 *
 * 决策过程：
 *   1) 从字典 {@code MATTER_DISPATCH_RULE} 读取规则条目
 *      item_key   = 目标部门
 *      item_value = 关键词列表（逗号分隔）
 *   2) 依次匹配标题 / 分类 / 描述，命中即锁定目标部门
 *   3) 无命中则走默认 "综合受理窗口"
 *   4) 调 Model Gateway 生成可审计的一句话理由
 *   5) 通过 {@code matter.dispatch} 工具完成分派
 *
 * 运营可在不重启应用的前提下通过字典 API 调整规则——规则即数据。
 */
@Component
public class IntakeDispatchAgent implements Agent {

    public static final String AGENT_ID = "agent.intake.dispatch";
    public static final String RULE_DICT_CODE = "MATTER_DISPATCH_RULE";
    public static final String DEFAULT_DEPARTMENT = "综合受理窗口";
    private static final Set<String> ALLOWED_TOOLS = Set.of("matter.dispatch");

    private final ToolRegistry toolRegistry;
    private final ModelGateway modelGateway;
    private final DictService dictService;

    public IntakeDispatchAgent(ToolRegistry toolRegistry,
                               ModelGateway modelGateway,
                               DictService dictService) {
        this.toolRegistry = toolRegistry;
        this.modelGateway = modelGateway;
        this.dictService = dictService;
    }

    @Override
    public AgentDefinition definition() {
        return new AgentDefinition(
                AGENT_ID,
                "default",
                "system",
                "政务事项受理分派 Agent：按字典规则 + 模型辅助推理完成部门分派",
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

        String previousTenant = TenantContext.tenant();
        TenantContext.setTenant(request.tenantId());
        Match match;
        try {
            match = resolveDepartment(category, title, description);
        } finally {
            TenantContext.setTenant(previousTenant);
        }

        ModelResponse llm = modelGateway.invoke(ModelRequest.of(
                request.tenantId(),
                "default",
                "事项标题：" + n(title)
                        + "\n分类：" + n(category)
                        + "\n已选定部门：" + match.department
                        + "\n请用一句中立、可审计的中文说明此分派理由。"
        ));

        Tool tool = toolRegistry.find("matter.dispatch")
                .orElseThrow(() -> new IllegalStateException("matter.dispatch tool not available"));
        if (!ALLOWED_TOOLS.contains(tool.name())) {
            return AgentResponse.denied("Agent 未被授予工具 " + tool.name(), null);
        }

        Map<String, Object> toolOut = tool.execute(Map.of(
                "matterId", matterId,
                "department", match.department,
                "tenantId", request.tenantId(),
                "status", "DISPATCHED"
        ));

        Map<String, Object> decision = new LinkedHashMap<>();
        decision.put("matterId", matterId);
        decision.put("department", match.department);
        decision.put("ruleSource", match.source);
        decision.put("matchedKeyword", match.matchedKeyword);
        decision.put("toolResult", toolOut);

        String reasoning = "规则来源：" + match.source
                + "；命中关键词：" + (match.matchedKeyword == null ? "(无)" : match.matchedKeyword)
                + "；目标部门：" + match.department
                + "。模型补充：" + llm.content();
        return AgentResponse.executed(decision, reasoning, null);
    }

    private Match resolveDepartment(String category, String title, String description) {
        String text = (n(category) + " " + n(title) + " " + n(description)).toLowerCase();

        List<DictItem> rules = dictService.listItems(RULE_DICT_CODE);
        for (DictItem rule : rules) {
            String keywords = rule.getItemValue();
            if (keywords == null || keywords.isBlank()) continue;
            for (String kw : keywords.split(",")) {
                String k = kw.trim().toLowerCase();
                if (!k.isEmpty() && text.contains(k)) {
                    return new Match(rule.getItemKey(), "dict:" + RULE_DICT_CODE, k);
                }
            }
        }
        return new Match(DEFAULT_DEPARTMENT, "fallback:default", null);
    }

    private record Match(String department, String source, String matchedKeyword) {
    }

    private String str(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private String n(String s) {
        return s == null ? "" : s;
    }
}

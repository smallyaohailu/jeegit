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
import io.jeegit.common.i18n.I18n;
import io.jeegit.tech.dict.DictItem;
import io.jeegit.tech.dict.DictService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Reference intake / dispatch agent.
 *
 * <p>Decision flow: 1. Read the {@code MATTER_DISPATCH_RULE} dictionary. item_key = target
 * department item_value = comma-separated keyword list 2. Match title / category / description
 * against each rule; first hit wins. 3. Fall back to the "general intake window" when nothing
 * matches. 4. Ask the Model Gateway for a neutral, auditable rationale. 5. Update state through the
 * {@code matter.dispatch} tool.
 *
 * <p>The user-facing reasoning summary is rendered in the caller's locale (driven by the incoming
 * Accept-Language header); the prompt sent to the model is kept in English to maximize cross-model
 * compatibility.
 */
@Component
public class IntakeDispatchAgent implements Agent {

  public static final String AGENT_ID = "agent.intake.dispatch";
  public static final String RULE_DICT_CODE = "MATTER_DISPATCH_RULE";
  public static final String DEFAULT_DEPARTMENT_KEY = "org.default.general_window";
  private static final Set<String> ALLOWED_TOOLS = Set.of("matter.dispatch");

  private final ToolRegistry toolRegistry;
  private final ModelGateway modelGateway;
  private final DictService dictService;
  private final I18n i18n;

  public IntakeDispatchAgent(
      ToolRegistry toolRegistry, ModelGateway modelGateway, DictService dictService, I18n i18n) {
    this.toolRegistry = toolRegistry;
    this.modelGateway = modelGateway;
    this.dictService = dictService;
    this.i18n = i18n;
  }

  @Override
  public AgentDefinition definition() {
    return new AgentDefinition(
        AGENT_ID,
        "default",
        "system",
        i18n.t("agent.intake.description"),
        Set.of("ROLE_DISPATCHER"),
        ALLOWED_TOOLS,
        "MEDIUM",
        HitlPolicy.ON_HIGH_RISK);
  }

  @Override
  public AgentResponse handle(AgentRequest request) {
    String matterId = str(request.input().get("matterId"));
    String title = str(request.input().get("title"));
    String category = str(request.input().get("category"));
    String description = str(request.input().get("description"));

    if (matterId == null || matterId.isBlank()) {
      return AgentResponse.denied(i18n.t("agent.error.matter_id_required"), null);
    }

    String previousTenant = TenantContext.tenant();
    TenantContext.setTenant(request.tenantId());
    Match match;
    try {
      match = resolveDepartment(category, title, description);
    } finally {
      TenantContext.setTenant(previousTenant);
    }

    ModelResponse llm =
        modelGateway.invoke(
            ModelRequest.of(
                request.tenantId(),
                "default",
                "Matter title: "
                    + n(title)
                    + "\nCategory: "
                    + n(category)
                    + "\nSelected department: "
                    + match.department
                    + "\nPlease produce a neutral, auditable one-sentence "
                    + "rationale justifying this routing decision."));

    Tool tool =
        toolRegistry
            .find("matter.dispatch")
            .orElseThrow(() -> new IllegalStateException("matter.dispatch tool not available"));
    if (!ALLOWED_TOOLS.contains(tool.name())) {
      return AgentResponse.denied(i18n.t("agent.error.tool_denied", AGENT_ID, tool.name()), null);
    }

    Map<String, Object> toolOut =
        tool.execute(
            Map.of(
                "matterId",
                matterId,
                "department",
                match.department,
                "tenantId",
                request.tenantId(),
                "status",
                "DISPATCHED"));

    Map<String, Object> decision = new LinkedHashMap<>();
    decision.put("matterId", matterId);
    decision.put("department", match.department);
    decision.put("ruleSource", match.source);
    decision.put("matchedKeyword", match.matchedKeyword);
    decision.put("toolResult", toolOut);

    String ruleLine =
        match.matchedKeyword != null
            ? i18n.t(
                "agent.intake.reasoning.rule", match.source, match.matchedKeyword, match.department)
            : i18n.t("agent.intake.reasoning.fallback");
    String reasoning = ruleLine + " " + i18n.t("agent.intake.reasoning.model", llm.content());
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
    return new Match(i18n.t(DEFAULT_DEPARTMENT_KEY), "fallback:default", null);
  }

  private record Match(String department, String source, String matchedKeyword) {}

  private String str(Object o) {
    return o == null ? null : String.valueOf(o);
  }

  private String n(String s) {
    return s == null ? "" : s;
  }
}

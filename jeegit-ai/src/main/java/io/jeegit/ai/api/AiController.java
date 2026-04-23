package io.jeegit.ai.api;

import io.jeegit.ai.agent.AgentDefinition;
import io.jeegit.ai.agent.AgentRequest;
import io.jeegit.ai.agent.AgentResponse;
import io.jeegit.ai.agent.AgentRuntime;
import io.jeegit.ai.tool.ToolRegistry;
import io.jeegit.common.ApiResponse;
import io.jeegit.common.TenantContext;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

  private final AgentRuntime agentRuntime;
  private final ToolRegistry toolRegistry;

  public AiController(AgentRuntime agentRuntime, ToolRegistry toolRegistry) {
    this.agentRuntime = agentRuntime;
    this.toolRegistry = toolRegistry;
  }

  @GetMapping("/agents")
  public ApiResponse<List<AgentDefinition>> listAgents() {
    return ApiResponse.ok(agentRuntime.list());
  }

  @GetMapping("/tools")
  public ApiResponse<List<Map<String, String>>> listTools() {
    List<Map<String, String>> data =
        toolRegistry.list().stream()
            .map(
                t ->
                    Map.of(
                        "name", t.name(),
                        "description", t.description(),
                        "riskLevel", t.riskLevel()))
            .toList();
    return ApiResponse.ok(data);
  }

  @PostMapping("/agents/{agentId}/invoke")
  public ApiResponse<AgentResponse> invoke(
      @PathVariable String agentId, @RequestBody Map<String, Object> payload) {
    @SuppressWarnings("unchecked")
    Map<String, Object> input = (Map<String, Object>) payload.getOrDefault("input", Map.of());
    AgentRequest req =
        new AgentRequest(
            agentId,
            (String) payload.get("traceId"),
            (String) payload.getOrDefault("tenantId", TenantContext.tenant()),
            (String) payload.getOrDefault("actorId", TenantContext.actor()),
            input);
    return ApiResponse.ok(agentRuntime.invoke(req));
  }
}

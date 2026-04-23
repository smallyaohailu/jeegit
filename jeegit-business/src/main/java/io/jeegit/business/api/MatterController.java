package io.jeegit.business.api;

import io.jeegit.ai.agent.AgentRequest;
import io.jeegit.ai.agent.AgentResponse;
import io.jeegit.ai.agent.AgentRuntime;
import io.jeegit.business.matter.Matter;
import io.jeegit.business.matter.MatterService;
import io.jeegit.common.ApiResponse;
import io.jeegit.common.TenantContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/matters")
public class MatterController {

    private static final String INTAKE_AGENT_ID = "agent.intake.dispatch";

    private final MatterService matterService;
    private final AgentRuntime agentRuntime;

    public MatterController(MatterService matterService, AgentRuntime agentRuntime) {
        this.matterService = matterService;
        this.agentRuntime = agentRuntime;
    }

    @PostMapping
    public ApiResponse<Matter> create(@RequestBody Map<String, Object> body) {
        Matter m = matterService.submit(
                (String) body.get("title"),
                (String) body.getOrDefault("category", "default"),
                (String) body.getOrDefault("description", ""),
                (String) body.getOrDefault("applicantId", "anonymous")
        );
        return ApiResponse.ok(m);
    }

    @GetMapping
    public ApiResponse<List<Matter>> list() {
        return ApiResponse.ok(matterService.listForCurrentTenant());
    }

    @GetMapping("/{id}")
    public ApiResponse<Matter> get(@PathVariable String id) {
        return ApiResponse.ok(matterService.get(id));
    }

    @PostMapping("/{id}/dispatch")
    public ApiResponse<AgentResponse> dispatch(@PathVariable String id) {
        Matter m = matterService.get(id);
        AgentRequest req = new AgentRequest(
                INTAKE_AGENT_ID,
                null,
                m.getTenantId(),
                TenantContext.actor(),
                Map.of(
                        "matterId", m.getId(),
                        "title", m.getTitle(),
                        "category", m.getCategory(),
                        "description", m.getDescription() == null ? "" : m.getDescription(),
                        "applicantId", m.getApplicantId() == null ? "" : m.getApplicantId(),
                        "orgId", m.getOrgId() == null ? "" : m.getOrgId()
                )
        );
        return ApiResponse.ok(agentRuntime.invoke(req));
    }
}

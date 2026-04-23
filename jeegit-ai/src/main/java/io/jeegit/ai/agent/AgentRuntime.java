package io.jeegit.ai.agent;

import io.jeegit.ai.hitl.HitlGuard;
import io.jeegit.common.TenantContext;
import io.jeegit.tech.audit.AuditLog;
import io.jeegit.tech.audit.AuditService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Runtime for every agent invocation. Responsibilities:
 *
 * <ul>
 *   <li>Registration / discovery of {@link Agent} implementations
 *   <li>Uniform audit entries before and after execution
 *   <li>Uniform HITL gate enforcement
 *   <li>Trace-id generation for downstream OpenTelemetry integration
 * </ul>
 *
 * Contract: business code only talks to the runtime, never directly to an {@code Agent} instance.
 */
@Component
public class AgentRuntime {

  private final Map<String, Agent> agents = new ConcurrentHashMap<>();
  private final AuditService auditService;
  private final HitlGuard hitlGuard;

  public AgentRuntime(List<Agent> discovered, AuditService auditService, HitlGuard hitlGuard) {
    this.auditService = auditService;
    this.hitlGuard = hitlGuard;
    for (Agent a : discovered) {
      agents.put(a.definition().agentId(), a);
    }
  }

  public Optional<Agent> find(String agentId) {
    return Optional.ofNullable(agents.get(agentId));
  }

  public List<AgentDefinition> list() {
    return agents.values().stream().map(Agent::definition).toList();
  }

  public AgentResponse invoke(AgentRequest request) {
    Agent agent = agents.get(request.agentId());
    if (agent == null) {
      return AgentResponse.denied("agent not found: " + request.agentId(), null);
    }

    String traceId = request.traceId() == null ? UUID.randomUUID().toString() : request.traceId();
    String tenantId = request.tenantId() == null ? TenantContext.tenant() : request.tenantId();
    AgentDefinition def = agent.definition();

    HitlGuard.Decision gate = hitlGuard.evaluate(def.hitlPolicy(), def.riskLevel());
    if (gate == HitlGuard.Decision.PENDING_APPROVAL) {
      String auditId =
          writeAudit(
              tenantId,
              traceId,
              def.agentId(),
              "AGENT_GATE",
              def.riskLevel(),
              "PENDING_APPROVAL",
              "Agent " + def.agentId() + " blocked by HITL policy=" + def.hitlPolicy(),
              String.valueOf(request.input()),
              null,
              0);
      return AgentResponse.pending(
          Map.of("reason", "HITL required"),
          "Blocked by HITL policy "
              + def.hitlPolicy()
              + ": the action requires human approval before execution.",
          auditId);
    }

    long start = System.currentTimeMillis();
    AgentResponse response;
    try {
      response =
          agent.handle(
              new AgentRequest(
                  def.agentId(), traceId, tenantId, request.actorId(), request.input()));
    } catch (RuntimeException ex) {
      String auditId =
          writeAudit(
              tenantId,
              traceId,
              def.agentId(),
              "AGENT_ERROR",
              def.riskLevel(),
              "DENIED",
              "Agent threw exception: " + ex.getClass().getSimpleName() + ": " + ex.getMessage(),
              String.valueOf(request.input()),
              null,
              System.currentTimeMillis() - start);
      return AgentResponse.denied(ex.getMessage(), auditId);
    }

    String auditId =
        writeAudit(
            tenantId,
            traceId,
            def.agentId(),
            "AGENT_RUN",
            def.riskLevel(),
            response.status(),
            response.reasoningSummary(),
            String.valueOf(request.input()),
            String.valueOf(response.decision()),
            System.currentTimeMillis() - start);

    if (response.auditLogId() == null) {
      return new AgentResponse(
          response.status(), response.decision(), response.reasoningSummary(), auditId);
    }
    return response;
  }

  private String writeAudit(
      String tenantId,
      String traceId,
      String agentId,
      String action,
      String riskLevel,
      String decision,
      String reasoning,
      String input,
      String output,
      long latency) {
    AuditLog log = new AuditLog();
    log.setTenantId(tenantId);
    log.setTraceId(traceId);
    log.setAgentId(agentId);
    log.setAction(action);
    log.setRiskLevel(riskLevel);
    log.setDecision(decision);
    log.setReasoningSummary(reasoning);
    log.setInputDigest(abbreviate(input));
    log.setOutputDigest(abbreviate(output));
    log.setLatencyMs(latency);
    return auditService.record(log).getId();
  }

  private String abbreviate(String s) {
    if (s == null) return null;
    return s.length() > 1500 ? s.substring(0, 1500) + "..." : s;
  }
}

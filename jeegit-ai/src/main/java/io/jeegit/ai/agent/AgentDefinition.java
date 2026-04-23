package io.jeegit.ai.agent;

import io.jeegit.ai.hitl.HitlPolicy;
import java.util.Set;

/**
 * Static, data-only description of an agent — maps directly onto the permission model in
 * AI_GOVERNANCE.md §2.
 */
public record AgentDefinition(
    String agentId,
    String tenantId,
    String ownerId,
    String description,
    Set<String> roles,
    Set<String> allowedTools,
    String riskLevel,
    HitlPolicy hitlPolicy) {}

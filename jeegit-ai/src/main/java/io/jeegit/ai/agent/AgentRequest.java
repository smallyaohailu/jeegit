package io.jeegit.ai.agent;

import java.util.Map;

/**
 * An invocation request handed to the Agent Runtime, which in turn dispatches it to a concrete
 * {@link Agent} implementation.
 */
public record AgentRequest(
    String agentId, String traceId, String tenantId, String actorId, Map<String, Object> input) {}

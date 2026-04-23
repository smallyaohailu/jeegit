package io.jeegit.ai.agent;

import java.util.Map;

/**
 * Structured result of an agent invocation.
 *
 * @param status           {@code EXECUTED}, {@code PENDING_APPROVAL}, or {@code DENIED}
 * @param decision         structured decision payload (e.g. target department,
 *                         recommended action)
 * @param reasoningSummary human-readable rationale — the direct artifact of the
 *                         "explainable" commitment
 * @param auditLogId       id of the audit entry written for this invocation
 */
public record AgentResponse(
        String status,
        Map<String, Object> decision,
        String reasoningSummary,
        String auditLogId
) {
    public static AgentResponse executed(Map<String, Object> decision, String reasoning, String auditLogId) {
        return new AgentResponse("EXECUTED", decision, reasoning, auditLogId);
    }

    public static AgentResponse pending(Map<String, Object> proposal, String reasoning, String auditLogId) {
        return new AgentResponse("PENDING_APPROVAL", proposal, reasoning, auditLogId);
    }

    public static AgentResponse denied(String reasoning, String auditLogId) {
        return new AgentResponse("DENIED", Map.of(), reasoning, auditLogId);
    }
}

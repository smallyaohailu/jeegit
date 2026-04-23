package io.jeegit.ai.agent;

import java.util.Map;

/**
 * Agent 的执行结果。
 *
 * @param status   EXECUTED / PENDING_APPROVAL / DENIED
 * @param decision 结构化决策（例如分派到哪个部门、推荐的处置方式）
 * @param reasoningSummary 人类可读的推理摘要——"可解释"承诺的直接载体
 * @param auditLogId 审计日志 ID，便于回放
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

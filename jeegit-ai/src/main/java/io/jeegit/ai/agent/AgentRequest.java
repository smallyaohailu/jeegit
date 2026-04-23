package io.jeegit.ai.agent;

import java.util.Map;

/**
 * Agent 调用请求。Agent Runtime 据此将任务派发给具体 Agent 实现。
 */
public record AgentRequest(
        String agentId,
        String traceId,
        String tenantId,
        String actorId,
        Map<String, Object> input
) {
}

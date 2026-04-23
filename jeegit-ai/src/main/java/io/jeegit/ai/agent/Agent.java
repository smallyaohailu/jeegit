package io.jeegit.ai.agent;

/**
 * Agent 实现契约。任何 Agent（内置或插件）都必须实现本接口。
 * Agent 不得直接访问业务模块实体；只能通过注入的 Tool 调用业务能力。
 */
public interface Agent {

    /**
     * Agent 的静态定义（含权限、白名单、HITL 策略）。
     */
    AgentDefinition definition();

    /**
     * 执行一次任务。Runtime 会在前后做权限/HITL/审计处理。
     */
    AgentResponse handle(AgentRequest request);
}

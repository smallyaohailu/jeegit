package io.jeegit.ai.hitl;

import org.springframework.stereotype.Component;

/**
 * HITL 拦截器。AgentRuntime 在执行 HIGH 风险动作前必须调用本组件决策。
 * 当前实现为进程内策略判定；后续可对接人工审批队列。
 */
@Component
public class HitlGuard {

    public enum Decision {
        /** 允许自动执行。 */
        ALLOW,
        /** 阻塞，必须等待人工审批。 */
        PENDING_APPROVAL
    }

    public Decision evaluate(HitlPolicy policy, String riskLevel) {
        if (policy == null) {
            policy = HitlPolicy.ON_HIGH_RISK;
        }
        return switch (policy) {
            case NONE -> Decision.ALLOW;
            case ALWAYS -> Decision.PENDING_APPROVAL;
            case ON_HIGH_RISK -> "HIGH".equalsIgnoreCase(riskLevel)
                    ? Decision.PENDING_APPROVAL
                    : Decision.ALLOW;
        };
    }
}

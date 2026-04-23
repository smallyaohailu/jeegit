package io.jeegit.ai.hitl;

import org.springframework.stereotype.Component;

/**
 * Evaluates the HITL policy before the AgentRuntime executes a tool call. The preview
 * implementation is in-process; production deployments typically replace the {@link
 * Decision#PENDING_APPROVAL} path with a real approval queue (task system, chatops bot, ticket,
 * etc.).
 */
@Component
public class HitlGuard {

  public enum Decision {
    /** The action may run automatically. */
    ALLOW,
    /** The action is blocked until a human explicitly approves it. */
    PENDING_APPROVAL
  }

  public Decision evaluate(HitlPolicy policy, String riskLevel) {
    if (policy == null) {
      policy = HitlPolicy.ON_HIGH_RISK;
    }
    return switch (policy) {
      case NONE -> Decision.ALLOW;
      case ALWAYS -> Decision.PENDING_APPROVAL;
      case ON_HIGH_RISK ->
          "HIGH".equalsIgnoreCase(riskLevel) ? Decision.PENDING_APPROVAL : Decision.ALLOW;
    };
  }
}

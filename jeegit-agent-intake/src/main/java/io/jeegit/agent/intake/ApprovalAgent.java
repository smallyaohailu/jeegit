package io.jeegit.agent.intake;

import io.jeegit.ai.agent.Agent;
import io.jeegit.ai.agent.AgentDefinition;
import io.jeegit.ai.agent.AgentRequest;
import io.jeegit.ai.agent.AgentResponse;
import io.jeegit.ai.hitl.HitlPolicy;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Approval agent definition. It is registered with HITL={@code ALWAYS} so the runtime blocks every
 * invocation and returns a {@code PENDING_APPROVAL} response — which is the desired audit trail for
 * "an agent proposed an approval; a human must confirm".
 */
@Component
public class ApprovalAgent implements Agent {

  public static final String AGENT_ID = "agent.approval";

  @Override
  public AgentDefinition definition() {
    return new AgentDefinition(
        AGENT_ID,
        "default",
        "system",
        "Proposes an approval decision on a matter. Always blocked by HITL pending human review.",
        Set.of("ROLE_APPROVER"),
        Set.of("matter.approve", "notify.send"),
        "HIGH",
        HitlPolicy.ALWAYS);
  }

  @Override
  public AgentResponse handle(AgentRequest request) {
    // Never reached — AgentRuntime returns PENDING_APPROVAL before calling us because of the
    // ALWAYS policy. The method is still implemented to satisfy the contract if a production
    // deployment relaxes the policy.
    throw new IllegalStateException(
        "ApprovalAgent must not be executed automatically; check your HITL configuration");
  }
}

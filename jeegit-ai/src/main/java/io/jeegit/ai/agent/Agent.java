package io.jeegit.ai.agent;

/**
 * Contract every agent implementation (built-in or plug-in) must satisfy. Agents never access
 * business repositories directly; they manipulate the outside world through the tools enumerated on
 * their definition.
 */
public interface Agent {

  /** Static agent description (permissions, allow-list, HITL policy). */
  AgentDefinition definition();

  /** Handle a single task. The runtime wraps this call with governance. */
  AgentResponse handle(AgentRequest request);
}

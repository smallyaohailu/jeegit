package io.jeegit.ai.tool;

import java.util.Map;

/**
 * Tool contract — the sole way for an agent to reach outside the model and
 * mutate business or external state. Per AI_GOVERNANCE.md §2 every tool is
 * granted to an agent via an explicit allow-list.
 */
public interface Tool {

    /**
     * Globally unique tool name; the convention is {@code domain.action},
     * e.g. {@code matter.dispatch}.
     */
    String name();

    /**
     * One-line, human-readable description that is written into audit trails
     * and surfaced in the Open Platform API catalogue.
     */
    String description();

    /**
     * Risk classification: {@code LOW}, {@code MEDIUM}, or {@code HIGH}.
     * {@code HIGH} tools trigger the HITL guard by default.
     */
    String riskLevel();

    /**
     * Execute the tool. The {@code params} map is required to carry the
     * active {@code tenantId}.
     */
    Map<String, Object> execute(Map<String, Object> params);
}

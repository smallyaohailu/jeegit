/**
 * jeegit Agent Intake module — reference agents that ship with the preview.
 *
 * <p>Home of:
 *
 * <ul>
 *   <li>{@link io.jeegit.agent.intake.IntakeDispatchAgent} — takes an intake matter, consults the
 *       {@code MATTER_DISPATCH_RULE} dictionary, asks the model gateway for a neutral rationale,
 *       then calls {@code matter.dispatch}. Risk MEDIUM, HITL {@code ON_HIGH_RISK}.
 *   <li>{@link io.jeegit.agent.intake.ApprovalAgent} — registered with HITL {@code ALWAYS}; the
 *       runtime returns {@code PENDING_APPROVAL} without calling the agent's {@code handle} —
 *       exactly the audit trail we want when a human must approve.
 * </ul>
 *
 * <p>These agents are templates. Downstream teams typically create their own modules with the same
 * shape (a {@code @Component} class that implements {@link io.jeegit.ai.agent.Agent}), and jeegit's
 * runtime picks them up automatically.
 */
package io.jeegit.agent.intake;

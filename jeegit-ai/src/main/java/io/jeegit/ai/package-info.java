/**
 * jeegit AI Core — the "intelligence layer" the platform stays honest about.
 *
 * <p>This module owns the choke points the architecture charter and AI governance document make
 * non-negotiable:
 *
 * <ul>
 *   <li>{@link io.jeegit.ai.model.ModelGateway} — the single path every LLM call flows through.
 *       Implementations: {@link io.jeegit.ai.model.EchoModelGateway} (network-free demo) and {@link
 *       io.jeegit.ai.model.OpenAICompatibleModelGateway} (production).
 *   <li>{@link io.jeegit.ai.agent.Agent} + {@link io.jeegit.ai.agent.AgentRuntime} — registered
 *       agents with permission envelopes (roles, allowed-tool allow-list, risk level, HITL policy).
 *       Every invocation produces an audit row.
 *   <li>{@link io.jeegit.ai.tool.Tool} + {@link io.jeegit.ai.tool.ToolRegistry} — the one way an
 *       agent can mutate state outside the model.
 *   <li>{@link io.jeegit.ai.hitl.HitlGuard} / {@link io.jeegit.ai.hitl.HitlPolicy} — the
 *       Human-in-the-Loop block for high-risk actions.
 *   <li>{@link io.jeegit.ai.prompt.PromptTemplate} and {@link
 *       io.jeegit.ai.prompt.PromptTemplateService} — versioned, publishable prompt assets with
 *       {@code {{placeholder}}} rendering.
 *   <li>{@link io.jeegit.ai.rag.KnowledgeService} + {@link
 *       io.jeegit.ai.rag.PgVectorKnowledgeService} — retrieval contract and a tenant-scoped
 *       substring / pgvector implementation.
 *   <li>{@link io.jeegit.ai.eval.EvaluationService} + {@link io.jeegit.ai.eval.EvaluationHarness} —
 *       release-gate evaluation for agents (driven by {@code POST /api/v1/ai/eval/{agentId}/run}).
 * </ul>
 *
 * <p>Contract: business code never calls a vendor SDK directly. If you need a new provider,
 * implement {@link io.jeegit.ai.model.ModelGateway} behind a Spring profile and register it via the
 * existing {@link io.jeegit.ai.AiCoreAutoConfiguration}.
 */
package io.jeegit.ai;

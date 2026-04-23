/**
 * jeegit Business mid-platform — shared enterprise workflow domain.
 *
 * <p>Today this module owns the "matter" aggregate — the generic intake / routing / approval unit
 * used by e-government and internal ticketing alike. It also declares the integration point for a
 * real BPMN engine:
 *
 * <ul>
 *   <li>{@link io.jeegit.business.matter.Matter} — tenant-aware aggregate with a typed {@link
 *       io.jeegit.business.matter.Matter.MatterStatus} state machine.
 *   <li>{@link io.jeegit.business.matter.MatterDispatchTool} and {@link
 *       io.jeegit.business.matter.MatterApprovalTool} — the tools an agent may be granted to mutate
 *       matter state (MEDIUM and HIGH risk respectively).
 *   <li>{@link io.jeegit.business.workflow.WorkflowEngine} — the contract Flowable / Camunda
 *       implementations satisfy in production; a stub is provided for the preview.
 *   <li>{@link io.jeegit.business.api.MatterController} — REST surface for matter submission,
 *       listing, pagination, and agent-driven dispatch.
 * </ul>
 *
 * <p>Emits domain events on the common {@link io.jeegit.common.event.EventBus}: {@code
 * jeegit.matter.submitted.v1}, {@code jeegit.matter.dispatched.v1}.
 */
package io.jeegit.business;

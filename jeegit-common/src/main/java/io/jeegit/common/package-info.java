/**
 * jeegit common library — shared base classes used by every other module.
 *
 * <p>This module is deliberately small and has no domain knowledge. It contains:
 *
 * <ul>
 *   <li>{@link io.jeegit.common.JeegitConstants} — platform-wide constants (name, version, default
 *       tenant, system actor).
 *   <li>{@link io.jeegit.common.TenantContext} — thread-local tenant + actor propagation used by
 *       Spring Data JPA auditing.
 *   <li>{@link io.jeegit.common.ApiResponse} / {@link io.jeegit.common.PagedResponse} — the unified
 *       response envelopes described in {@code docs/engineering/api-design-style.md}.
 *   <li>{@link io.jeegit.common.dao} — JPA {@code @MappedSuperclass} hierarchy (BaseEntity →
 *       AuditableEntity → TenantAwareEntity → TreeEntity) and the DataScope enum.
 *   <li>{@link io.jeegit.common.event} — in-process domain event bus ({@code EventBus}, {@code
 *       DomainEvent}).
 *   <li>{@link io.jeegit.common.i18n} — locale negotiation (12-locale contract) and {@link
 *       io.jeegit.common.i18n.I18n} translator helper.
 *   <li>{@link io.jeegit.common.web} — rate limiter, global exception handler.
 * </ul>
 *
 * <p>Rules for this module (from the architecture charter):
 *
 * <ol>
 *   <li>No dependency on any other jeegit module.
 *   <li>No domain logic. If it has a business meaning, it belongs in {@code jeegit-business}.
 *   <li>Every new helper must come with a unit test; see {@code
 *       docs/engineering/testing-strategy.md} for the sizing rules.
 * </ol>
 */
package io.jeegit.common;

/**
 * jeegit Tech mid-platform — identity, organization, dictionary, audit.
 *
 * <p>Everything that a multi-tenant enterprise application needs before it can even ship its first
 * business feature. Sub-packages:
 *
 * <ul>
 *   <li>{@link io.jeegit.tech.tenant} — the tenant aggregate (the isolation boundary for every
 *       other row in the platform).
 *   <li>{@link io.jeegit.tech.org} — the single organization tree hosting {@code COMPANY}, {@code
 *       DEPARTMENT}, and {@code TEAM} node types; materialized-path descendant lookup.
 *   <li>{@link io.jeegit.tech.iam} — {@code User}, {@code Role}, and the {@code
 *       DataScopeSpecifications} factory that turns a {@code DataScope} enum into a portable JPA
 *       {@code Specification}.
 *   <li>{@link io.jeegit.tech.dict} — the dictionary service (operator-editable rule tables
 *       consumed by both business code and AI agents).
 *   <li>{@link io.jeegit.tech.audit} — append-only {@code AuditLog} with trace id, decision, and
 *       reasoning summary; the backing store for the governance commitments described in {@code
 *       docs/AI_GOVERNANCE.md}.
 *   <li>{@link io.jeegit.tech.api} — admin REST controllers for dictionary, organization, and
 *       audit.
 * </ul>
 *
 * <p>The mid-platform is consumed by {@code jeegit-business} and {@code jeegit-ai} through the
 * services declared here; no public access to the entities themselves.
 */
package io.jeegit.tech;

/**
 * jeegit Open Platform — the REST / API-key surface partner applications talk to.
 *
 * <ul>
 *   <li>{@link io.jeegit.openapi.PlatformController} — {@code /api/v1/platform/*} endpoints:
 *       metadata, locale catalogue.
 *   <li>{@link io.jeegit.openapi.apikey.ApiKey} + {@link io.jeegit.openapi.apikey.ApiKeyService} —
 *       SHA-256-digest-only storage; plaintext returned once at issue.
 *   <li>{@link io.jeegit.openapi.apikey.TenantResolverFilter} — turns {@code X-API-Key} and {@code
 *       X-Tenant-Id} headers into the right tenant/actor context.
 * </ul>
 *
 * <p>See {@code docs/engineering/api-design-style.md} for the contract partners are expected to
 * build against (versioning, pagination, error envelope, BCP-47 locale).
 */
package io.jeegit.openapi;

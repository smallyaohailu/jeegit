/**
 * jeegit bootstrap module — the Spring Boot assembly.
 *
 * <p>This module does no domain work. It exists solely to wire every other module into a runnable
 * application:
 *
 * <ul>
 *   <li>{@link io.jeegit.bootstrap.JeegitApplication} — the Spring Boot entry point.
 *   <li>{@link io.jeegit.bootstrap.SecurityConfig} — stateless HTTP Basic auth for administrators,
 *       API-key auth for partners, public reads.
 *   <li>{@link io.jeegit.bootstrap.ApiKeyAuthenticationFilter} — converts a valid {@code X-API-Key}
 *       into a Spring Security principal.
 *   <li>{@link io.jeegit.bootstrap.DemoDataSeed} — idempotent seed data for first run.
 *   <li>{@link io.jeegit.bootstrap.OpenApiConfig} — springdoc metadata.
 * </ul>
 *
 * <p>Two Spring profiles matter:
 *
 * <ul>
 *   <li><strong>default</strong> — in-memory H2, zero external dependencies (preview).
 *   <li><strong>postgres</strong> — Flyway-managed PostgreSQL schema, recommended for any
 *       deployment that outlives a laptop. {@code pgvector} composes on top if the extension is
 *       enabled in the database.
 * </ul>
 */
package io.jeegit.bootstrap;

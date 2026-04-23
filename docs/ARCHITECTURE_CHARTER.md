# Architecture Charter

> This document captures **non-negotiable architecture principles** for jeegit.
> Every PR / RFC must state compliance with this charter; breaking changes go
> through the RFC process.

---

## 1. Shape: 1 + 4 + 1

- **1 AI Core** — model gateway, agent runtime, tool registry, RAG, evaluation,
  human-in-the-loop, governance.
- **4 Mid-Platforms** — data, business, tech, application.
- **1 Open Platform** — API + event bus exposed to partners and in-house builders.

> Tagline: *stable core + replaceable intelligence layer + solution templates.*

## 2. Technology baseline (stable for three years)

| Area           | Choice                                                              |
| -------------- | ------------------------------------------------------------------- |
| JDK            | **Java 21 LTS**                                                     |
| Framework      | **Spring Boot 3.x** + Spring Modulith + Spring Security             |
| ORM            | **JPA (Hibernate)**; Specifications / QueryDSL for complex reads    |
| RDBMS          | **PostgreSQL** primary, MySQL supported, Flyway for migrations      |
| Cache / MQ     | Redis; Kafka or RocketMQ                                            |
| Vector search  | **Dedicated subsystem** (pgvector / Milvus / ES-vector) — not JPA   |
| API standards  | **OpenAPI + AsyncAPI** dual-track                                   |
| Observability  | OpenTelemetry + Prometheus + Grafana                                |
| I18n           | **First-party translations for 12 locales** (see LANGUAGES.md)      |
| Primary language | **English-first** for code, comments, identifiers, documentation  |
| License        | **Apache-2.0**                                                      |

## 3. Architecture principles (code of conduct)

1. **Stable core, replaceable intelligence layer** — model, vector store, and
   orchestration framework can all be swapped.
2. **SLM-first, LLM-as-fallback** — enterprise workloads default to small
   models; large models handle the hard tail.
3. **Standards first** — OpenAPI / AsyncAPI / OIDC / OAuth2; reserve adapter
   layers for A2A and MCP.
4. **Policy as code** — permissions, compliance, and risk controls are versioned
   and auditable.
5. **Audit by default** — every critical action produces a tamper-evident
   record.
6. **Eval as gate** — no AI capability is released without a passing evaluation
   suite.
7. **Tenant isolation end-to-end** — data, model configuration, knowledge base,
   and logs are partitioned per tenant.
8. **Human-in-the-loop by default** — high-risk actions require human approval;
   everything is reversible and accountable.
9. **English-first, multilingual ready** — English is authoritative; twelve
   locales ship with the platform.
10. **Offline-, private-, sovereign-deploy-ready** — no dependency on internet
    connectivity is ever baked into the core.
11. **Modular monolith first, distribute when needed** — enforce module
    boundaries with Spring Modulith before splitting into services.

## 4. Business core vs. intelligence layer

```
┌───────── Intelligence Layer (replaceable) ─────────┐
│ Prompts · Agents · Tools · RAG · Eval · Routing    │
└───────────────────────┬────────────────────────────┘
                        │ (stable API contracts)
┌───────────────────────▼────────────────────────────┐
│           Business Core (JPA · tx · domain)        │
│     IAM · Tenant · Workflow · Audit · Services     │
└────────────────────────────────────────────────────┘
```

The business core changes **slowly**; the intelligence layer changes **often**.
They communicate through stable API contracts — the intelligence layer never
touches business entities directly.

## 5. JPA ground rules

- Transactional core domains (IAM, organizations, workflow, audit, business
  entities) **must** use JPA.
- JPA **does not** own full-text search, vector search, or large-scale analytics.
- Complex aggregations use `@Query` / Specification; no raw SQL that breaks the
  abstraction.
- Every aggregate root carries a {@code tenantId} so the tenant boundary is
  enforced at the entity base class.

## 6. Module boundaries (Modulith)

- A module only exposes types from its public `api` sub-package.
- Cross-module calls go through application-service interfaces and domain
  events — no direct access to another module's entities.
- The AI module must not depend on concrete business modules; business modules
  call into the AI module through its public API.

## 7. API contract rules

- URL shape: `/api/v{major}/{domain}/{resource}`; a major bump never breaks
  the previous version.
- Every public API ships an OpenAPI description for SDK generation by the Open
  Platform.
- Event contracts are described with AsyncAPI; topic naming is
  `jeegit.{domain}.{event}.v1`.

## 8. Breaking-change process

1. Submit an RFC at `docs/rfcs/NNNN-<slug>.md`.
2. Maintainer review plus community discussion.
3. When accepted, land in the next major release with a deprecation window of
   at least one minor release and a documented migration guide.

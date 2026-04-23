# Release notes

## 1.1.0 — follow-through on the roadmap

Ten additional self-iterated rounds after the 1.0.0 preview. Highlights:

- **CI**: GitHub Actions workflow enforcing Google Java Style via Spotless, running
  `mvn verify`, and exercising the multi-stage Docker build.
- **OpenAI-compatible Model Gateway**: production implementation supporting OpenAI, Azure, DeepSeek,
  Kimi, Qwen-API, Together.ai, Ollama, vLLM, and similar servers. Activated with
  `jeegit.ai.model.provider=openai-compat`; falls back to the Echo gateway otherwise.
- **pgvector-ready KnowledgeService**: a `jg_knowledge_chunk` table and a tenant-scoped search
  service bound under the `pgvector` and `postgres` profiles. A documented ALTER TABLE recipe
  lets operators enable pgvector when they want HNSW retrieval.
- **Evaluation harness**: `EvaluationHarness` + `POST /api/v1/ai/eval/{agentId}/run` for release
  gates. Case inputs are key=value lines; the expected substring is matched against the agent's
  reasoning.
- **Domain event bus**: in-process `EventBus` with wildcard subscriptions; `matter.submitted` and
  `matter.dispatched` events fire from `MatterService`.
- **Tenant & API-key authentication**: `X-API-Key` header resolves against the `jg_api_key` table,
  promotes the caller to a Spring Security principal (authority `ROLE_PARTNER`) and adopts the
  key's tenant for the rest of the request.
- **Service-layer coverage**: new `@DataJpaTest` slices for `OrgService` (tree invariants,
  descendants, missing-parent handling) and `DictService` (upsert idempotency, sort order).
- **Console pages**: API keys, Prompt templates, Agents — all Material 3, all i18n-aware across
  the 12 shipped locales.
- **Internationalized documentation**: `docs/i18n/<locale>/README.md` landing pages across all
  twelve languages, linked from the main README.
- **Governance documents**: `CONTRIBUTING.md`, `CODE_OF_CONDUCT.md`, `SECURITY.md`.

Test suite: 43 green (25 common + 5 tech + 3 ai + 10 bootstrap). New contracts added:
- `io.jeegit.ai.model.OpenAICompatibleModelGatewayTest` — stub HTTP server verifies Bearer auth,
  `X-Jeegit-Tenant` header, request body, parsed response, audit log.
- `io.jeegit.common.event.EventBusTest` — exact / wildcard routing, failure isolation.
- `io.jeegit.tech.dict.DictServiceTest`, `io.jeegit.tech.org.OrgServiceTest`.

## 1.0.0 — production-grade preview

After twenty auto-iterations, the preview has matured into a coherent,
production-ready platform release. Highlights:

### Platform + DAO
- `BaseEntity` → `AuditableEntity` → `TenantAwareEntity` → `TreeEntity`
  hierarchy backed by Spring Data JPA auditing (created-by / created-at /
  updated-by / updated-at populated from `TenantContext`).
- Organization tree (COMPANY / DEPARTMENT / TEAM) with materialized-path
  descendant lookups.
- Dictionary service (`DictType` / `DictItem`) — operators can hot-edit
  rules without redeploy.
- Role + declarative `DataScope` turned into JPA Specifications.
- Append-only `AuditLog` with trace-id, risk level, decision, rationale.

### AI Core
- Model Gateway, Agent Runtime, Tool Registry, Knowledge / RAG contract,
  Evaluation contract, Human-in-the-Loop guard.
- Versioned `PromptTemplate` registry with `{{placeholder}}` renderer.
- Reference agents: **Intake Dispatch** (MEDIUM, ON_HIGH_RISK) and
  **Approval** (HIGH, ALWAYS — blocked until a human approves).
- Tools: `matter.dispatch`, `matter.approve`, `notify.send`.

### Open Platform
- Spring-docs OpenAPI 3 + Swagger UI at `/swagger-ui.html`.
- API keys stored as SHA-256 digests — plaintext shown only at issue.
- Per-tenant sliding-window rate limiting on `/api/v1/**`.

### Security & observability
- Stateless HTTP Basic auth on writes; public reads.
- Global exception handler returns localized `ApiResponse` envelopes with
  stable error codes.
- Prometheus metrics at `/actuator/prometheus` (98+ series).
- OpenTelemetry tracing bridge at 100 % sampling (tune for production).

### Internationalization
- Twelve first-party locales: en, zh-CN, zh-TW, ja, ko, es, fr, de,
  pt-BR, ru, it, ar. Arabic is RTL-aware end-to-end.
- Locale negotiation: cookie > `Accept-Language` > default; `?lang=xx`
  override for testing. `ApiResponse.meta.locale` is always present.

### Frontend (Material Design 3)
- Single-page console served from `/` by Spring Boot.
- Material 3 token-based stylesheet (light + dark), Google Roboto +
  Material Symbols.
- Routes: **Dashboard**, **Matters**, **Audit trail**, **Organizations**.
- Twelve bundled JSON translation files, hash-routed, RTL-safe.

### Ops & persistence
- PostgreSQL profile (`SPRING_PROFILES_ACTIVE=postgres`) with Flyway
  `V1__baseline.sql` covering every shipped entity.
- Multi-stage Dockerfile (non-root runtime, healthcheck).
- `docker-compose.yml` for local full-stack dev: jeegit + Postgres 16.

### Code quality
- Spotless + `google-java-format` enforced in the build.
- 30 tests (20 unit + 10 Spring-Boot integration) green.

### Governance documents
`docs/PRODUCT_CHARTER.md`, `docs/ARCHITECTURE_CHARTER.md`,
`docs/AI_GOVERNANCE.md`, `docs/MVP_SCOPE.md`, `docs/AI_SAFETY_POLICY.md`,
`docs/DATA_POLICY.md`, `docs/MODEL_LICENSES.md`,
`docs/i18n/LANGUAGES.md`.

## 0.3.0 — twelve locales

First-party translations for twelve locales aligned with the LangChain
ecosystem; English-first codebase and documentation.

## 0.2.0 — DAO foundation

Entity hierarchy, organization tree, dictionary service, role +
data-scope, reference intake/dispatch agent.

## 0.1.0 — initial foundation

Module scaffolding, governance charters, reference intake agent.

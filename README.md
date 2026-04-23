# jeegit

**AI-Native application framework for the enterprise.** Java 21 · Spring Boot 3 · JPA ·
Apache License 2.0 · Material Design 3 console · 12 locales.

> jeegit helps teams compose auditable, multi-tenant, AI-capable business applications —
> from internal workflow tools to mission-critical e-government services — on a single,
> coherent stack.

> 🌍 **Also available in:** [简体中文](docs/i18n/zh-CN/README.md) · [繁體中文](docs/i18n/zh-TW/README.md) · [日本語](docs/i18n/ja/README.md) · [한국어](docs/i18n/ko/README.md) · [Español](docs/i18n/es/README.md) · [Français](docs/i18n/fr/README.md) · [Deutsch](docs/i18n/de/README.md) · [Português (Brasil)](docs/i18n/pt-BR/README.md) · [Русский](docs/i18n/ru/README.md) · [Italiano](docs/i18n/it/README.md) · [العربية](docs/i18n/ar/README.md)

[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-21-orange.svg)](#)
[![Spring Boot](https://img.shields.io/badge/spring--boot-3.3-brightgreen.svg)](#)
[![Locales](https://img.shields.io/badge/i18n-12%20locales-blueviolet.svg)](docs/i18n/LANGUAGES.md)
[![Style](https://img.shields.io/badge/style-google--java--format-4285F4.svg)](https://google.github.io/styleguide/javaguide.html)
[![CI](https://img.shields.io/badge/ci-GitHub%20Actions-24292e.svg)](.github/workflows/ci.yml)

---

## Why jeegit

Modern enterprise and government applications need two things that rarely sit well together: the
**stability** of a platform (multi-tenant isolation, audit, identity, workflow) and the
**agility** of an AI-native stack (agents, tools, retrieval, evaluation). Most teams end up
stitching the two from scratch and rewriting the glue every quarter.

jeegit gives you both in one opinionated framework, operated under the
[Google engineering methodology](docs/adr/0001-adopt-google-engineering-methodology.md):
Google Java Style, AIP-aligned APIs, SRE SLO/error-budget loops, PRD + OKR product cadence,
blameless postmortems.

## Quick start

### Preview (H2, zero dependencies)

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# open http://localhost:8080/
```

### Production-style (PostgreSQL + Flyway)

```bash
docker compose up --build
```

### Configure a real model provider

```bash
export JEEGIT_AI_MODEL_PROVIDER=openai-compat
export JEEGIT_AI_MODEL_BASE_URL=https://api.openai.com
export JEEGIT_AI_MODEL_API_KEY=sk-...
export JEEGIT_AI_MODEL_DEFAULT_MODEL=gpt-4o-mini
```

### Exercise the reference flow

```bash
# Create a matter
MATTER=$(curl -s -u admin:admin -X POST -H 'Content-Type: application/json' \
  -d '{"title":"Business tax filing question","category":"tax","applicantId":"user-1"}' \
  http://localhost:8080/api/v1/matters)
ID=$(echo "$MATTER" | jq -r .data.id)

# Let the agent dispatch it
curl -s -u admin:admin -X POST http://localhost:8080/api/v1/matters/$ID/dispatch | jq

# Inspect the audit trail
curl -s http://localhost:8080/api/v1/audit/tenants/default | jq
```

### Switch the console language

```bash
curl -s -H 'Accept-Language: zh-CN' http://localhost:8080/api/v1/platform/info | jq .data.tagline
curl -s -H 'Accept-Language: ja'    http://localhost:8080/api/v1/platform/info | jq .data.tagline
curl -s 'http://localhost:8080/api/v1/platform/info?lang=ar' | jq '.data.tagline, .meta.locale'
curl -s http://localhost:8080/api/v1/platform/locales | jq
```

## Architecture in one picture

```
              ┌───── Open Platform ─────┐
              │ API + Console + API keys│
              └─────────────────────────┘
┌──── Data ──┬── Business ──┬── Tech ──┬── App ────┐
│  (stub)    │  Matter +    │  IAM /   │  (stub)   │
│            │  Workflow    │  Org /   │           │
│            │  integration │  Dict /  │           │
│            │              │  Audit   │           │
└────────────┴──────────────┴──────────┴───────────┘
              ┌──────── AI Core ────────┐
              │ Model · Agent · Tool ·  │
              │ RAG · Eval · HITL · Prompt │
              └─────────────────────────┘
```

See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for the module → package → governance doc
map.

## Feature highlights

### Platform core

- Multi-tenant data isolation (every aggregate carries `tenantId`, enforced by the
  `TenantAwareEntity` base class).
- Materialized-path organization tree; role + declarative **Data Scope**
  (`ALL / COMPANY(_AND_CHILD) / DEPARTMENT(_AND_CHILD) / SELF / CUSTOM`).
- Hot-editable dictionary service (rules live in the database, not in code).
- Append-only audit log with trace id, risk level, decision, and reasoning summary.
- Per-tenant sliding-window rate limiter with a unified 429 envelope.
- Spring Security stateless HTTP Basic + partner API keys (SHA-256-digest-only storage).

### AI Core

- **Model Gateway** — the one allowed path for every LLM call. Ships with an
  OpenAI-compatible implementation (OpenAI, Azure, DeepSeek, Kimi, Qwen-API, Together.ai,
  Ollama, vLLM) alongside a network-free Echo gateway.
- **Agent Runtime** — registers agents, enforces tool allow-lists, runs the HITL policy, writes
  audit rows. Ships with two reference agents: `agent.intake.dispatch` (MEDIUM / ON_HIGH_RISK)
  and `agent.approval` (HIGH / ALWAYS — blocked until a human approves).
- **Tool Registry** — `matter.dispatch`, `matter.approve`, `notify.send`.
- **Knowledge / RAG** — tenant-scoped chunks; substring fallback today, pgvector-ready once the
  extension is enabled.
- **Evaluation harness** — `POST /api/v1/ai/eval/{agentId}/run` returns `total`, `passed`,
  `passRate`, per-case verdicts — use it as a release gate.
- **Prompt-as-Asset** — versioned, publishable `PromptTemplate` with `{{placeholder}}`
  rendering.

### Open Platform

- OpenAPI 3 at `/v3/api-docs`; Swagger UI at `/swagger-ui.html`.
- 24 REST endpoints in the current release. API style follows
  [Google AIP](docs/engineering/api-design-style.md).

### Console (Material Design 3)

- SPA served from `/` by Spring Boot, Roboto + Material Symbols.
- Routes: **Dashboard**, **Matters**, **Audit trail**, **Organizations**, **API keys**,
  **Prompts**, **Agents**.
- Locale negotiation + `?lang=xx` override + RTL-safe layout for Arabic.

### Ops

- PostgreSQL profile with `V1__baseline.sql` + `V2__knowledge_chunk.sql` Flyway migrations.
- Multi-stage Docker image (non-root runtime, HEALTHCHECK) + docker-compose.
- Prometheus metrics + OpenTelemetry tracing bridge.
- GitHub Actions CI: Spotless gate · `mvn verify` · Docker image build.

## Documentation map

### Governance charters (stable, change-controlled)

- [`docs/PRODUCT_CHARTER.md`](docs/PRODUCT_CHARTER.md) — scope, audience, non-goals, commitments.
- [`docs/ARCHITECTURE_CHARTER.md`](docs/ARCHITECTURE_CHARTER.md) — invariant architecture principles.
- [`docs/AI_GOVERNANCE.md`](docs/AI_GOVERNANCE.md) — agent permissions, audit, HITL, eval-as-gate.
- [`docs/AI_SAFETY_POLICY.md`](docs/AI_SAFETY_POLICY.md) — threat model and built-in mitigations.
- [`docs/DATA_POLICY.md`](docs/DATA_POLICY.md) — data classification and handling.
- [`docs/MODEL_LICENSES.md`](docs/MODEL_LICENSES.md) — model-license posture.

### Architecture & internals

- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — the one "where do I look?" page.
- [`docs/MVP_SCOPE.md`](docs/MVP_SCOPE.md) — scope of the preview.
- [`docs/i18n/LANGUAGES.md`](docs/i18n/LANGUAGES.md) — the 12-locale contract.

### Engineering (Google-aligned)

- [`docs/engineering/README.md`](docs/engineering/README.md) — index.
- [`docs/engineering/design-doc-template.md`](docs/engineering/design-doc-template.md)
- [`docs/engineering/api-design-style.md`](docs/engineering/api-design-style.md) — Google AIP subset.
- [`docs/engineering/testing-strategy.md`](docs/engineering/testing-strategy.md) — Small /
  Medium / Large / Enormous test sizes.
- [`docs/engineering/code-review.md`](docs/engineering/code-review.md) — adapted from the
  Google Code Review Developer Guide.
- [`docs/engineering/release-engineering.md`](docs/engineering/release-engineering.md).

### SRE

- [`docs/sre/README.md`](docs/sre/README.md) — index.
- [`docs/sre/slo.md`](docs/sre/slo.md)
- [`docs/sre/error-budget-policy.md`](docs/sre/error-budget-policy.md)
- [`docs/sre/incident-response.md`](docs/sre/incident-response.md)
- [`docs/sre/postmortem-template.md`](docs/sre/postmortem-template.md)
- [`docs/sre/on-call.md`](docs/sre/on-call.md)

### Product & business

- [`docs/product/README.md`](docs/product/README.md) — index.
- [`docs/product/prd-template.md`](docs/product/prd-template.md)
- [`docs/product/okrs.md`](docs/product/okrs.md)
- [`docs/product/launch-checklist.md`](docs/product/launch-checklist.md)
- [`docs/product/business-model.md`](docs/product/business-model.md)

### Decision records

- [`docs/adr/README.md`](docs/adr/README.md)
- [`docs/adr/0001-adopt-google-engineering-methodology.md`](docs/adr/0001-adopt-google-engineering-methodology.md)

### Contributor-facing

- [`CONTRIBUTING.md`](CONTRIBUTING.md)
- [`CODE_OF_CONDUCT.md`](CODE_OF_CONDUCT.md)
- [`SECURITY.md`](SECURITY.md)
- [`RELEASE_NOTES.md`](RELEASE_NOTES.md)

## License

Apache License 2.0. See [`LICENSE`](LICENSE). Commercial strategy is transparent: the code
stays open, the market is in implementation, training, and managed runtimes — see
[`docs/product/business-model.md`](docs/product/business-model.md).

# jeegit

**AI-Native application framework for the enterprise.** Built on Java 21, Spring Boot 3 and JPA.
Released under the Apache License 2.0.

> jeegit helps teams compose auditable, multi-tenant, AI-capable business applications —
> from internal workflow tools to mission-critical e-government services — on a single, coherent stack.

> 🌍 **Also available in:** [简体中文](docs/i18n/zh-CN/README.md) · [繁體中文](docs/i18n/zh-TW/README.md) · [日本語](docs/i18n/ja/README.md) · [한국어](docs/i18n/ko/README.md) · [Español](docs/i18n/es/README.md) · [Français](docs/i18n/fr/README.md) · [Deutsch](docs/i18n/de/README.md) · [Português (Brasil)](docs/i18n/pt-BR/README.md) · [Русский](docs/i18n/ru/README.md) · [Italiano](docs/i18n/it/README.md) · [العربية](docs/i18n/ar/README.md)

[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-21-orange.svg)](#)
[![Spring Boot](https://img.shields.io/badge/spring--boot-3.3-brightgreen.svg)](#)
[![Locales](https://img.shields.io/badge/i18n-12%20locales-blueviolet.svg)](docs/i18n/LANGUAGES.md)
[![Status](https://img.shields.io/badge/status-preview-yellow.svg)](#)

---

## Why jeegit

Modern applications need two things that rarely sit well together: the **stability** of an enterprise
platform (multi-tenant isolation, audit, identity, workflow) and the **agility** of an AI-native
stack (agents, tools, retrieval, evaluation). Most teams end up stitching these layers together from
scratch, then rewriting the glue every quarter.

jeegit gives you both in one opinionated framework:

- A **stable core** — IAM, multi-tenancy, append-only audit, organization tree, dictionary, role &
  data-scope model, workflow integration point — that does not change at the whim of every new model.
- A **replaceable intelligence layer** — model gateway, agent runtime, tool registry, knowledge
  service, evaluation — with clear contracts so any vendor or in-house model can be plugged in.
- **Reference business modules and agents** that demonstrate the intended shape of a solution.

## Feature overview

### Platform core
- Multi-tenant data isolation (every aggregate carries `tenantId`, enforced at the entity base class).
- JPA-based DAO foundation: `BaseEntity` → `AuditableEntity` → `TenantAwareEntity` → `TreeEntity`.
- Unified audit log (append-only) with tracing fields.
- Organization tree with materialized-path support for fast ancestor queries.
- Role model with a declarative **Data Scope** enum (ALL / COMPANY / COMPANY_AND_CHILD / DEPARTMENT /
  DEPARTMENT_AND_CHILD / SELF / CUSTOM).
- Dictionary service — runtime-editable key/value catalogs used by both business code and agents.
- Workflow engine integration point (pluggable; default stub ships out of the box).
- **Built-in internationalization** — first-party translations for 12 locales
  (see [`docs/i18n/LANGUAGES.md`](docs/i18n/LANGUAGES.md)), negotiated per
  request via `Accept-Language`, with a `?lang=xx` override for quick testing.

### AI-Native layer
- **Model Gateway** — single choke point for every LLM call; handles auth, quota, cost, audit, PII
  redaction. Drop-in replacements for OpenAI-compatible / local SLM / vendor SDKs.
- **Agent Runtime** — registers agents, enforces tool whitelists, runs the HITL policy, writes an
  explainable audit record for every invocation.
- **Tool Registry** — a tool is the only way an agent can mutate business state; each tool declares
  a risk level and is scoped to an explicit allow-list per agent.
- **Knowledge Service (RAG)** — retrieval contract with citation support; swap in pgvector / Milvus /
  Elasticsearch without touching business code.
- **Evaluation Service** — "eval-as-gate" primitive; any agent / prompt / knowledge change can be
  gated by an evaluation suite before release.
- **Human-in-the-Loop guard** — declarative policies (`NONE` / `ON_HIGH_RISK` / `ALWAYS`) that block
  risky actions until a human approves.

### Reference solution — intake & dispatch
A ready-to-run example showing how the platform composes:

1. `POST /api/v1/matters` creates an intake record.
2. `POST /api/v1/matters/{id}/dispatch` invokes the **Intake Dispatch Agent**, which consults the
   `MATTER_DISPATCH_RULE` dictionary, calls the Model Gateway for a human-readable rationale, then
   uses the `matter.dispatch` tool to update the record.
3. The full decision (rule hit + model rationale + audit id) is returned.

## Architecture (1 + 4 + 1)

```
┌──────────────────────── Open Platform ─────────────────────────┐
│   API Gateway · Developer Portal · API Catalog · SDK           │
└────────────────────────────────────────────────────────────────┘
┌───────────┬───────────┬───────────┬─────────────────────────────┐
│   Data    │ Business  │   Tech    │         Application         │
│  Platform │ Platform  │ Platform  │          Platform           │
└───────────┴───────────┴───────────┴─────────────────────────────┘
┌────────────────────────── AI Core ─────────────────────────────┐
│ Model Gateway · Agent Runtime · Tool Registry · Knowledge /    │
│ RAG · Evaluation · Human-in-the-Loop · AI Governance           │
└────────────────────────────────────────────────────────────────┘
```

## Module layout

| Module                  | Role                                                                 |
| ----------------------- | -------------------------------------------------------------------- |
| `jeegit-common`         | DAO base classes, tenant context, API response, shared enums          |
| `jeegit-tech`           | Tech Platform — tenant / org / user / role / dictionary / audit       |
| `jeegit-data`           | Data Platform — master data, metrics, tags (scaffold)                 |
| `jeegit-ai`             | AI Core — Model Gateway, Agent Runtime, Tool Registry, RAG, Eval, HITL|
| `jeegit-business`       | Business Platform — matters, workflow integration point               |
| `jeegit-app`            | Application Platform — portal, BFF (scaffold)                         |
| `jeegit-openapi`        | Open Platform — API surface, developer-facing endpoints               |
| `jeegit-agent-intake`   | Reference agent — intake & dispatch                                   |
| `jeegit-bootstrap`      | Spring Boot 3 runnable assembly                                       |

## Quick start

```bash
# Build everything
mvn -q -DskipTests package

# Run the reference assembly (H2 in-memory; Postgres in production)
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar

# Verify
curl http://127.0.0.1:8080/actuator/health
curl http://127.0.0.1:8080/api/v1/platform/info
```

### Try the reference flow

```bash
# Create an intake
MATTER=$(curl -s -X POST -H 'Content-Type: application/json' \
  -d '{"title":"Business tax filing question","category":"tax","description":"need help","applicantId":"user-1"}' \
  http://127.0.0.1:8080/api/v1/matters)

ID=$(echo "$MATTER" | jq -r .data.id)

# Let the agent dispatch it
curl -s -X POST http://127.0.0.1:8080/api/v1/matters/$ID/dispatch | jq

# Inspect the audit trail
curl -s http://127.0.0.1:8080/api/v1/audit/tenants/default | jq
```

### Switch languages on the fly

```bash
# Negotiate via Accept-Language
curl -s -H 'Accept-Language: zh-CN' http://127.0.0.1:8080/api/v1/platform/info | jq .data.tagline
curl -s -H 'Accept-Language: ja'    http://127.0.0.1:8080/api/v1/platform/info | jq .data.tagline
curl -s -H 'Accept-Language: ar'    http://127.0.0.1:8080/api/v1/platform/info | jq .data.tagline

# Or override with ?lang= for testing
curl -s 'http://127.0.0.1:8080/api/v1/platform/info?lang=fr' | jq .data.tagline

# List every supported locale
curl -s http://127.0.0.1:8080/api/v1/platform/locales | jq
```

## Governance documents

| Document | Purpose |
| --- | --- |
| [`docs/PRODUCT_CHARTER.md`](docs/PRODUCT_CHARTER.md) | Product charter — scope, audience, commitments |
| [`docs/ARCHITECTURE_CHARTER.md`](docs/ARCHITECTURE_CHARTER.md) | Architecture principles that do not change lightly |
| [`docs/AI_GOVERNANCE.md`](docs/AI_GOVERNANCE.md) | Agent permissions, audit, HITL, eval-as-gate |
| [`docs/MODEL_LICENSES.md`](docs/MODEL_LICENSES.md) | Model-license posture |
| [`docs/DATA_POLICY.md`](docs/DATA_POLICY.md) | Data classification and handling |
| [`docs/AI_SAFETY_POLICY.md`](docs/AI_SAFETY_POLICY.md) | Threat model and built-in mitigations |
| [`docs/MVP_SCOPE.md`](docs/MVP_SCOPE.md) | Scope of the current preview |
| [`docs/i18n/LANGUAGES.md`](docs/i18n/LANGUAGES.md) | Supported locales and localization process |

## Roadmap

- Persistent RDBMS profile (PostgreSQL + Flyway migrations)
- BPMN engine integration (Flowable / Camunda)
- OpenAI-compatible model gateway implementation (plus local SLM connector)
- pgvector knowledge provider
- Evaluation harness with a CI gate
- Developer portal UI for the Open Platform

## License

Apache License 2.0. See [`LICENSE`](LICENSE).

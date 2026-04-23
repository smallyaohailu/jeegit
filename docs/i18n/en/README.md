# jeegit · Overview (English)

[简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

**jeegit** is an AI-Native application framework built on Java 21, Spring Boot 3 and JPA, and
released under the Apache License 2.0. It is designed as a coherent, production-ready substrate
for building multi-tenant, auditable, AI-capable business applications — from internal workflow
tools to mission-critical e-government services.

## What you get, out of the box

- **Platform core** — multi-tenancy, organization tree, dictionary service, role + data scope,
  append-only audit, rate limiting.
- **AI Core** — Model Gateway (OpenAI-compatible implementation bundled), Agent Runtime, Tool
  Registry, Human-in-the-Loop guard, Prompt Template versioning, pgvector-ready Knowledge Service,
  Evaluation harness.
- **Open Platform** — REST + OpenAPI 3, Swagger UI, SHA-256-digest API keys, partner
  authentication filter.
- **Console** — Material Design 3 single-page app served from Spring Boot, with twelve bundled
  translations (RTL-safe for Arabic).
- **Ops** — PostgreSQL + Flyway, Docker multi-stage image, docker-compose, Prometheus metrics,
  OpenTelemetry tracing, GitHub Actions CI.

## Quick start

```bash
# Preview on H2 (no dependencies)
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar

# Production-style (PostgreSQL + Flyway via docker-compose)
docker compose up --build
```

Then open <http://localhost:8080/>. The console negotiates the client locale via
`Accept-Language` and accepts a `?lang=xx` override for testing; every REST response carries
the resolved locale in `meta.locale`.

## Further reading

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md) — product scope, audience, non-goals
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md) — invariant architecture principles
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md) — permissions, audit, HITL, eval-as-gate
- [`LANGUAGES.md`](../LANGUAGES.md) — twelve-locale contract
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)

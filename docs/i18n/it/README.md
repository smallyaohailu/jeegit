# jeegit · Panoramica (Italiano)

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [العربية](../ar/README.md)

**jeegit** è un **framework applicativo AI-nativo** basato su Java 21, Spring Boot 3 e JPA,
pubblicato con licenza Apache-2.0. Fornisce una base coerente per applicazioni enterprise
multi-tenant, auditabili e dotate di capacità AI — dai tool di workflow interni ai sistemi di
pubblica amministrazione mission-critical.

## Incluso out-of-the-box

- **Core di piattaforma**: multi-tenant, albero organizzativo, dizionari, ruoli + scope dati,
  audit append-only, limitazione del traffico.
- **Core AI**: Model Gateway (implementazione OpenAI-compatibile inclusa), Agent Runtime, Tool
  Registry, HITL Guard, versionamento dei modelli di prompt, knowledge service pronto per
  pgvector, harness di valutazione.
- **Piattaforma aperta**: REST + OpenAPI 3, Swagger UI, chiavi API con digest SHA-256, filtro di
  autenticazione partner.
- **Console**: SPA Material Design 3 servita da Spring Boot, 12 lingue integrate (arabo in RTL).
- **Operatività**: PostgreSQL + Flyway, Dockerfile multi-stage, docker-compose, metriche
  Prometheus, tracing OpenTelemetry, CI GitHub Actions.

## Avvio rapido

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# oppure avvia l'intero stack con PostgreSQL tramite docker compose
docker compose up --build
```

Apri <http://localhost:8080/>. La console negozia la lingua via `Accept-Language` e accetta
l'override `?lang=xx`; ogni risposta REST riporta la lingua scelta in `meta.locale`.

## Altre letture

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)

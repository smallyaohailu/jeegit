# jeegit · Überblick (Deutsch)

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

**jeegit** ist ein **KI-natives Anwendungs-Framework** auf Basis von Java 21, Spring Boot 3 und JPA,
veröffentlicht unter Apache-2.0. Es bietet eine kohärente Grundlage für mandantenfähige,
auditfähige und KI-fähige Geschäftsanwendungen — von internen Workflow-Tools bis zu
geschäftskritischen E-Government-Systemen.

## Von Haus aus dabei

- **Plattform-Kern**: Mandantenfähigkeit, Organisationsbaum, Wörterbuch, Rollen + Datensichtbarkeit,
  Append-Only-Audit, Ratenbegrenzung.
- **KI-Kern**: Model Gateway (OpenAI-kompatible Implementierung inklusive), Agent-Runtime,
  Tool-Register, HITL-Guard, Versionierung von Prompt-Vorlagen, pgvector-fähiger Knowledge-Service,
  Evaluierungs-Harness.
- **Offene Plattform**: REST + OpenAPI 3, Swagger UI, API-Keys mit SHA-256-Digest,
  Partner-Authentifizierungsfilter.
- **Konsole**: Material-Design-3-SPA, ausgeliefert von Spring Boot, 12 Sprachen integriert
  (Arabisch mit RTL).
- **Betrieb**: PostgreSQL + Flyway, Multi-Stage-Dockerfile, docker-compose, Prometheus-Metriken,
  OpenTelemetry-Tracing, GitHub-Actions-CI.

## Schnellstart

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# oder den vollen Stack mit PostgreSQL per docker compose
docker compose up --build
```

<http://localhost:8080/> öffnen. Die Konsole verhandelt die Sprache über `Accept-Language` und
unterstützt `?lang=xx` als Override. Jede REST-Antwort gibt die gewählte Sprache in `meta.locale`
zurück.

## Weiterführende Dokumente

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)

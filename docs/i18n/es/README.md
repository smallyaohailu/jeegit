# jeegit · Resumen (Español)

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

**jeegit** es un **framework de aplicaciones nativo en IA** construido sobre Java 21, Spring Boot 3 y JPA,
publicado bajo licencia Apache-2.0. Ofrece una base coherente para aplicaciones empresariales y de
gobierno multi-inquilino, auditables y con capacidades de IA — desde herramientas internas hasta
sistemas de administración electrónica críticos.

## Incluido de serie

- **Núcleo de la plataforma**: multi-tenant, árbol de organizaciones, diccionario,
  roles + ámbito de datos, auditoría append-only, limitación de tasa.
- **Núcleo de IA**: Model Gateway (implementación compatible con OpenAI incluida), Agent Runtime,
  Tool Registry, HITL Guard, versión de plantillas de prompt, servicio de conocimiento listo para
  pgvector, harness de evaluación.
- **Plataforma abierta**: REST + OpenAPI 3, Swagger UI, claves API con digest SHA-256, filtro de
  autenticación para socios.
- **Consola**: SPA con Material Design 3 servida por Spring Boot, 12 idiomas integrados
  (árabe en RTL).
- **Operaciones**: PostgreSQL + Flyway, Dockerfile multi-stage, docker-compose, métricas Prometheus,
  trazas OpenTelemetry, CI de GitHub Actions.

## Inicio rápido

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# o levanta toda la pila con PostgreSQL usando docker compose
docker compose up --build
```

Abre <http://localhost:8080/>. La consola negocia el idioma vía `Accept-Language` y admite el
sobreescrito `?lang=xx`. Cada respuesta REST reporta el idioma resuelto en `meta.locale`.

## Lecturas adicionales

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)

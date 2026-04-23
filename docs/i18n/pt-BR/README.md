# jeegit · Visão geral (Português Brasil)

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

O **jeegit** é um **framework de aplicações nativo em IA** construído em Java 21, Spring Boot 3 e
JPA, publicado sob Apache-2.0. Ele oferece uma base coerente para aplicações multi-tenant,
auditáveis e com capacidades de IA — de ferramentas internas a sistemas críticos de governo
digital.

## Já vem incluso

- **Núcleo da plataforma**: multi-tenant, árvore de organizações, dicionário, papéis + escopo de
  dados, auditoria append-only, limitação de taxa.
- **Núcleo de IA**: Model Gateway (implementação compatível com OpenAI embutida), Agent Runtime,
  Tool Registry, guarda HITL, versões de templates de prompt, serviço de conhecimento pronto para
  pgvector, harness de avaliação.
- **Plataforma aberta**: REST + OpenAPI 3, Swagger UI, chaves de API com digest SHA-256, filtro de
  autenticação para parceiros.
- **Console**: SPA Material Design 3 servido pelo Spring Boot, 12 idiomas integrados (árabe com
  RTL).
- **Operação**: PostgreSQL + Flyway, Docker multi-stage, docker-compose, métricas Prometheus,
  tracing OpenTelemetry, CI no GitHub Actions.

## Início rápido

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# ou suba toda a pilha com PostgreSQL via docker compose
docker compose up --build
```

Abra <http://localhost:8080/>. O console negocia o idioma por `Accept-Language` e aceita o
parâmetro `?lang=xx` para testes; todas as respostas REST informam o idioma resolvido em
`meta.locale`.

## Leitura adicional

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)

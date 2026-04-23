# jeegit · Обзор (Русский)

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Français](../fr/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

**jeegit** — это **AI-ориентированный фреймворк корпоративных приложений** на Java 21, Spring Boot 3
и JPA, выпускаемый под лицензией Apache-2.0. Он служит единой основой для построения
мультиарендных, аудируемых и AI-ориентированных приложений — от внутренних рабочих процессов до
критических систем электронного правительства.

## Поставляется сразу

- **Ядро платформы**: мультиарендность, дерево организаций, словари, роли + область данных,
  журнал аудита (append-only), ограничение частоты запросов.
- **AI-ядро**: Model Gateway (в комплекте — OpenAI-совместимая реализация), Agent Runtime,
  реестр инструментов, HITL-гвард, версионирование шаблонов prompt-ов, готовый к pgvector сервис
  знаний, Evaluation-каркас.
- **Открытая платформа**: REST + OpenAPI 3, Swagger UI, API-ключи с дайджестом SHA-256, фильтр
  аутентификации партнёров.
- **Консоль**: SPA в стиле Material Design 3, обслуживаемая Spring Boot; 12 встроенных языков
  (поддержка RTL для арабского).
- **Эксплуатация**: PostgreSQL + Flyway, многослойный Dockerfile, docker-compose, метрики
  Prometheus, трейсинг OpenTelemetry, CI на GitHub Actions.

## Быстрый старт

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# либо полный стек с PostgreSQL через docker compose
docker compose up --build
```

Откройте <http://localhost:8080/>. Консоль согласует язык через `Accept-Language` и принимает
переопределение `?lang=xx`. Каждая REST-ответка возвращает выбранный язык в `meta.locale`.

## Дополнительное чтение

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)

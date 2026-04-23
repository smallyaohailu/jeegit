# jeegit · Vue d'ensemble (Français)

[English](../en/README.md) · [简体中文](../zh-CN/README.md) · [繁體中文](../zh-TW/README.md) · [日本語](../ja/README.md) · [한국어](../ko/README.md) · [Español](../es/README.md) · [Deutsch](../de/README.md) · [Português (Brasil)](../pt-BR/README.md) · [Русский](../ru/README.md) · [Italiano](../it/README.md) · [العربية](../ar/README.md)

**jeegit** est un **framework d'applications natif IA** basé sur Java 21, Spring Boot 3 et JPA,
distribué sous licence Apache-2.0. Il fournit un socle cohérent pour bâtir des applications
multi-tenant, auditables et dotées de capacités IA — des outils internes jusqu'aux systèmes
d'administration électronique critiques.

## Livré d'emblée

- **Cœur de plateforme** : multi-tenant, arbre d'organisations, dictionnaire, rôles + portée de
  données, audit append-only, limitation de débit.
- **Cœur IA** : Model Gateway (implémentation OpenAI-compatible incluse), Agent Runtime, Tool
  Registry, HITL Guard, versionnage de modèles de prompt, service de connaissance prêt pour
  pgvector, harnais d'évaluation.
- **Plateforme ouverte** : REST + OpenAPI 3, Swagger UI, clés API à empreinte SHA-256, filtre
  d'authentification partenaires.
- **Console** : SPA Material Design 3 servie par Spring Boot, 12 langues incluses (arabe en RTL).
- **Ops** : PostgreSQL + Flyway, Dockerfile multi-étages, docker-compose, métriques Prometheus,
  traçage OpenTelemetry, CI GitHub Actions.

## Démarrage rapide

```bash
mvn -q -DskipTests package
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
# ou lancer la pile complète avec PostgreSQL via docker compose
docker compose up --build
```

Ouvrez <http://localhost:8080/>. La console négocie la langue via `Accept-Language` et accepte
une surcharge `?lang=xx`. Chaque réponse REST indique la langue effective dans `meta.locale`.

## Lectures complémentaires

- [`PRODUCT_CHARTER.md`](../../PRODUCT_CHARTER.md)
- [`ARCHITECTURE_CHARTER.md`](../../ARCHITECTURE_CHARTER.md)
- [`AI_GOVERNANCE.md`](../../AI_GOVERNANCE.md)
- [`LANGUAGES.md`](../LANGUAGES.md)
- [`RELEASE_NOTES.md`](../../../RELEASE_NOTES.md)

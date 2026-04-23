# Architecture overview

This document is the single "where do I look?" answer for jeegit. It binds module → package →
governance artefact so contributors can navigate the tree from any starting point.

## Shape (1 + 4 + 1)

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

## Module → package → doc

| Maven module | Top package | `package-info.java` | Primary governance doc |
| --- | --- | --- | --- |
| `jeegit-common` | `io.jeegit.common` | [link](../jeegit-common/src/main/java/io/jeegit/common/package-info.java) | `docs/ARCHITECTURE_CHARTER.md` |
| `jeegit-tech` | `io.jeegit.tech` | [link](../jeegit-tech/src/main/java/io/jeegit/tech/package-info.java) | `docs/ARCHITECTURE_CHARTER.md` + `docs/AI_GOVERNANCE.md` (audit) |
| `jeegit-ai` | `io.jeegit.ai` | [link](../jeegit-ai/src/main/java/io/jeegit/ai/package-info.java) | `docs/AI_GOVERNANCE.md` + `docs/AI_SAFETY_POLICY.md` |
| `jeegit-business` | `io.jeegit.business` | [link](../jeegit-business/src/main/java/io/jeegit/business/package-info.java) | `docs/PRODUCT_CHARTER.md` |
| `jeegit-agent-intake` | `io.jeegit.agent.intake` | [link](../jeegit-agent-intake/src/main/java/io/jeegit/agent/intake/package-info.java) | `docs/AI_GOVERNANCE.md` §2 (permissions) |
| `jeegit-openapi` | `io.jeegit.openapi` | [link](../jeegit-openapi/src/main/java/io/jeegit/openapi/package-info.java) | `docs/engineering/api-design-style.md` |
| `jeegit-bootstrap` | `io.jeegit.bootstrap` | [link](../jeegit-bootstrap/src/main/java/io/jeegit/bootstrap/package-info.java) | `docs/engineering/release-engineering.md` |

## Cross-cutting concerns

| Concern | Lives in | Reference |
| --- | --- | --- |
| Multi-tenant isolation | `TenantAwareEntity`, `TenantContext`, `TenantResolverFilter` | `ARCHITECTURE_CHARTER.md` #7 |
| Auditing | `AuditLog`, `AuditService`, `AgentRuntime.writeAudit` | `AI_GOVERNANCE.md` §4 |
| Localization | `I18n`, `SupportedLocales`, `messages*.properties`, `static/i18n/*.json` | `docs/i18n/LANGUAGES.md` |
| Rate limiting | `SlidingWindowRateLimiter` + `RateLimitingConfig` | `SECURITY.md` |
| Observability | Actuator + `/actuator/prometheus` + OpenTelemetry bridge | `docs/sre/slo.md` |
| Events | `EventBus`, `DomainEvent`, `jeegit.*.v1` topics | `docs/engineering/api-design-style.md` |
| Security | `SecurityConfig`, `ApiKeyAuthenticationFilter` | `SECURITY.md` |
| Style & formatting | `spotless-maven-plugin` + `google-java-format` | `docs/engineering/testing-strategy.md` |

## Request flow (Matter dispatch)

1. Partner calls `POST /api/v1/matters/{id}/dispatch` with either Basic auth or `X-API-Key`.
2. Spring Security chain authenticates, `ApiKeyAuthenticationFilter` or the tenant filter sets
   `TenantContext`.
3. `MatterController` forwards to `AgentRuntime`.
4. `AgentRuntime` evaluates HITL (`HitlGuard`), writes an audit pre-run.
5. `IntakeDispatchAgent` reads dispatch rules from the dictionary, asks `ModelGateway`, then
   calls the `matter.dispatch` tool.
6. Tool calls `MatterService.assignDepartment`, which fires `jeegit.matter.dispatched.v1` on the
   `EventBus`.
7. `AgentRuntime` writes the post-run audit row and returns a localized reasoning summary.

## Where to put new things

| If you're adding… | Put it in | Also update |
| --- | --- | --- |
| …a new AI tool | `jeegit-ai/...tool/` or a new module | Agent's allow-list + audit coverage |
| …a new agent | new module `jeegit-agent-<name>` | Evaluation suite; README admin page |
| …a new REST endpoint | appropriate controller package | `docs/engineering/api-design-style.md` compliance |
| …a new persistent aggregate | domain module with JPA entity | Flyway migration in `jeegit-bootstrap/src/main/resources/db/migration/` |
| …a new locale | `static/i18n/<tag>.json` + `i18n/messages_<tag>.properties` + `docs/i18n/<tag>/README.md` | `SupportedLocales`, `docs/i18n/LANGUAGES.md` |
| …a new operational knob | `application.properties` | `docs/engineering/release-engineering.md` |

## Non-goals captured here

- Implementing a full BPMN engine — delegated to Flowable / Camunda via `WorkflowEngine`.
- Implementing a full vector database — delegated to pgvector or a dedicated store.
- Training / hosting models — delegated to whatever `ModelGateway` you configure.

This keeps jeegit the *platform*, not every one of its sub-ecosystems.

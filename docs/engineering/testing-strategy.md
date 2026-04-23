# Testing strategy (Google-aligned)

jeegit's testing strategy is patterned on *Software Engineering at Google* chapter 11 and the
*Testing on the Toilet* series. We use the same vocabulary for test sizes so contributors coming
from the Google ecosystem can navigate the suite with zero ramp-up.

## Test sizes

| Size | Scope | Runtime budget | Dependencies allowed | In this repo |
| --- | --- | --- | --- | --- |
| **Small** | single class, no I/O | < 100 ms each | pure in-memory | `io.jeegit.common.**`, `io.jeegit.ai.prompt.PromptTemplateServiceTest`, `io.jeegit.ai.model.OpenAICompatibleModelGatewayTest` (uses an in-process stub server that still counts as small) |
| **Medium** | module, may use in-memory H2 and Spring context | < 1 s each | no network | `@DataJpaTest` slices (`io.jeegit.tech.dict.DictServiceTest`, `io.jeegit.tech.org.OrgServiceTest`) |
| **Large** | full Spring Boot context with in-memory database | ~3-8 s each | no external services | `io.jeegit.bootstrap.JeegitApplicationTest` |

A test that needs the internet, a Docker container, or a clock shift is an **enormous** test —
we do not have any in jeegit's CI path yet. When we do, they will live under `src/test/java/...IT.java`
and be gated by `mvn verify -Dskip.it=false`.

## What every commit brings

- A behaviour-changing commit adds or updates at least one test.
- A pure refactor does not need new tests but MUST keep the existing suite green.
- A new entity adds a JPA slice test asserting the `TenantAwareEntity` / auditing contract.
- A new agent adds an evaluation-harness case against `POST /api/v1/ai/eval/{agentId}/run`.

## Style

Borrowed from the *Test Desiderata* Kent Beck / Kent Dodds list that Google code-review guidance
aligns with:

1. **Isolated.** Tests must pass in any order and in parallel.
2. **Readable.** The test is also the specification — prefer AssertJ fluent matchers; avoid
   `@MockBean` for code we own.
3. **Composable.** Helper factories live next to the test class; no cross-package test kits.
4. **Fast.** The whole suite completes in a developer's ~20 s thought loop. If it doesn't, split
   the slow test into a medium slice.
5. **Deterministic.** Time, random, and UUIDs are injected. Flaky tests are bugs, not noise.

## Forbidden patterns

- Sleeping for real time (use `Awaitility` when polling is unavoidable).
- Disabling or ignoring tests to unblock a build — fix or delete.
- Asserting on log lines (brittle and redundant with audit / metric assertions).

## Coverage

We do not target a numeric coverage percentage. The right question is *"if I broke this line,
would a test catch it?"* — see *Unit Testing Anti-Patterns* (abseil.io/resources/swe-book).
Coverage reports are informational; PRs are judged on the presence of behaviour tests, not on a
number.

## Running

```bash
mvn -q -DskipTests=false verify              # full suite (~30 s)
mvn -q test -pl jeegit-ai -am                # slice
mvn -q test -Dtest=EventBusTest -pl jeegit-common  # single test
```

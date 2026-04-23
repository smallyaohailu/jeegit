# Service Level Objectives

These are the SLOs we commit to operating against. Each SLO is anchored on a Service Level
Indicator (SLI) that can be computed from the Prometheus metrics already exposed at
`/actuator/prometheus`.

> Format follows *Site Reliability Engineering* ch. 4 — Indicator → Objective → Window →
> Measurement → Consequences when missed.

## Tier · Availability

| SLI | Objective | Window | Measurement | Consequence on breach |
| --- | --- | --- | --- | --- |
| `/actuator/health` returns `UP` | **99.9 %** | 30-day rolling | `up{job="jeegit"}` | All non-fix changes paused (see error-budget policy) |
| Successful 2xx ratio on `GET /api/v1/**` | **99.5 %** | 30-day rolling | `rate(http_server_requests_seconds_count{outcome="SUCCESS",method="GET",uri=~"/api/v1/.*"}[30d]) / rate(http_server_requests_seconds_count{method="GET",uri=~"/api/v1/.*"}[30d])` | Same |
| Successful 2xx/4xx ratio on `POST /api/v1/**` (non-5xx writes) | **99.5 %** | 30-day rolling | `rate(http_server_requests_seconds_count{outcome!~"SERVER_ERROR",method="POST",uri=~"/api/v1/.*"}[30d]) / rate(http_server_requests_seconds_count{method="POST",uri=~"/api/v1/.*"}[30d])` | Same |

## Tier · Latency

| SLI | Objective | Window | Measurement |
| --- | --- | --- | --- |
| `GET /api/v1/platform/info` p95 | **< 100 ms** | 7-day rolling | `histogram_quantile(0.95, rate(http_server_requests_seconds_bucket{uri="/api/v1/platform/info"}[7d]))` |
| `POST /api/v1/matters` p95 | **< 250 ms** | 7-day rolling | as above with the matters URI |
| `POST /api/v1/matters/{id}/dispatch` p95 (Echo gateway) | **< 350 ms** | 7-day rolling | as above |

## Tier · AI quality

| SLI | Objective | Window | Measurement |
| --- | --- | --- | --- |
| `agent.intake.dispatch` evaluation pass-rate | **≥ 95 %** | per release | `EvalSummary.passRate` from `POST /api/v1/ai/eval/{agentId}/run` against the canonical case set |
| Audit completeness for AI calls | **100 %** | continuous | every `MODEL_CALL` and `AGENT_RUN` produces an `AuditLog` row (acceptance test in `JeegitApplicationTest`) |

## Tier · Multilingual reach

| SLI | Objective | Window | Measurement |
| --- | --- | --- | --- |
| Locales with a complete bundle | **12 / 12** | continuous | `messages_*.properties` files keep parity with `messages.properties`; CI script `scripts/check-locale-parity.sh` (TBD) checks key counts |

## Choosing the right SLO target

We do not promise "five nines" because we are not a payments rail. The current targets reflect
a preview platform whose users are operators, ISVs, and government back-office staff. As real
deployments come online we will tighten or relax these targets via the error-budget policy
process documented next door.

## Reporting

- Operators run a Prometheus instance scraping `/actuator/prometheus` and back the SLI queries
  above with recording rules.
- A release does not ship if any guarded SLO has been in breach for the trailing 7 days; the
  error-budget policy describes the override path.

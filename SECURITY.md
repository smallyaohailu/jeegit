# Security policy

## Supported versions

| Version | Supported |
| :------ | :-------- |
| 1.1.x   | ✅ (current) |
| 1.0.x   | 🟡 critical fixes only |
| < 1.0   | ❌ |

## Reporting a vulnerability

Please **do not** open public GitHub issues for security problems.

Email **security@jeegit.io** with:

1. A concise description of the issue.
2. Reproduction steps — ideally a minimal JVM test or HTTP trace.
3. Your assessment of impact (confidentiality / integrity / availability).
4. Any proposed mitigation.

We will acknowledge your report within three business days and provide a tentative remediation
timeline within seven. Please allow up to 90 days for a coordinated disclosure.

## Threat model reminders

- Business code must not call vendor SDKs directly; all model calls flow through the
  `ModelGateway`.
- The rate limiter sits on `/api/v1/**`; in production deployments it must be backed by Redis or
  equivalent so the limit is cluster-wide.
- API keys are stored only as SHA-256 digests — never log the plaintext.
- The HITL guard blocks `HIGH`-risk agent actions by default; any configuration that loosens this
  should be reviewed as a security change, not just a product change.
- Audit entries are append-only by contract. Production deployments should revoke `UPDATE` /
  `DELETE` privileges on `jg_audit_log` from the application role.

## Upstream dependencies

jeegit inherits security updates from Spring Boot, Hibernate, Jackson, Flyway, springdoc, Micrometer
and the JDK. CI pins versions; we rebase onto new minor releases within two weeks of a security
advisory from any of those projects.

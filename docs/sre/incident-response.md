# Incident response

Adapted from *SRE* chapters 13 (*Managing Incidents*) and 14 (*Emergency Response*). The
expectation: whoever notices the incident opens it, the on-call drives it, and a single Incident
Commander holds the decision.

## Incident definition

An event that either:

1. **Breaches or will plausibly breach an SLO** within the current window, or
2. **Degrades the user experience at scale** (≥ 1 % of authenticated callers affected), or
3. **Exposes a security posture we committed to** in `SECURITY.md`.

## Severity

| Sev | Symptom | Response time |
| --- | --- | --- |
| **SEV-1** | Platform unreachable, data loss risk, credential leak | 15 min to engage Incident Commander |
| **SEV-2** | Major endpoint family down, > 5 % error rate sustained, audit chain broken | 30 min |
| **SEV-3** | Single endpoint degraded, SLO at risk but not breached | next business day |

## Roles

- **Incident Commander (IC).** Owns the decision. Does not fix bugs during the incident.
- **Ops lead.** Performs mitigations (rollback, flag flip, scale).
- **Comms lead.** Keeps the incident channel and status page updated.
- **Scribe.** Maintains the timeline.

For small teams one person may hold multiple roles, but IC and Ops lead MUST be different
people.

## Flow

```
Detect → Declare → Assess → Mitigate → Stabilise → Close → Postmortem
```

1. **Detect.** Prometheus alert, SLO burn alert, or user report.
2. **Declare.** Open the incident ticket with the `incident` label; post in the incident
   channel; assign IC.
3. **Assess.** Scope the blast radius. Which SLO is burning? Which tenant(s)?
4. **Mitigate.** Rollback first, understand later. The tag of the last-known-good deploy is
   always the first mitigation considered.
5. **Stabilise.** Confirm SLO metrics have recovered for two straight windows. Disable the
   mitigation if it was temporary.
6. **Close.** IC declares resolved, timeline is frozen, postmortem is scheduled.
7. **Postmortem.** Within 5 business days for SEV-1/2, within 10 for SEV-3. Use
   [`postmortem-template.md`](./postmortem-template.md).

## Mitigations we own

- **Rollback** — `docker compose pull && docker compose up -d` with the previous image tag.
- **Rate limit tightening** — `jeegit.rate-limit.permits-per-minute` down to e.g. 60.
- **Disable the model gateway** — set `jeegit.ai.model.provider=` (empty) to fall back to echo.
- **Revoke a misbehaving API key** — `DELETE /api/v1/openapi/keys/{id}` via admin.
- **Quiesce writes** — block POST/PUT/PATCH/DELETE at the reverse proxy.

## Communication template

```
[SEV-X] <one-line summary>
Started:    <timestamp UTC>
IC:         <name>
Impact:     <who / how many / which SLO>
Mitigation: <current action>
Next update: <time>
```

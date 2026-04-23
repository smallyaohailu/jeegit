# On-call rotation

Adapted from *Site Reliability Engineering* ch. 11 — *Being On-Call*.

## Shape

- **Minimum viable rotation:** two people, weekly handover on Mondays 09:00 local to the primary
  oncall's timezone.
- **Follow-the-sun** is preferred once three regions are staffed.
- **Shadow week** for every new joiner before they take primary.

## Responsibilities

The on-call engineer (primary):

1. Watches the incident channel.
2. Owns alert triage within 5 minutes during working hours, 15 minutes outside.
3. Is the default Incident Commander for SEV-1 / SEV-2 until the IC hat is explicitly handed
   off.
4. Keeps the runbook up to date — if they learned something new this week, it is recorded
   before handover.

## What on-call does NOT do

- Feature work. A primary oncall who picks up a non-urgent PR is under-investing in reliability.
- Interviews.
- Unplanned meetings lasting > 30 minutes.

## Handover

At the end of each shift the outgoing primary posts:

```
Handover · <date> · <primary> → <next primary>
Pages this week:        N
New issues filed:       N
Open mitigations:       …
Runbook edits:          …
Known risks for next week: …
```

## Compensation

jeegit is an open-source project today and does not compensate on-call. When downstream
deployments adopt the rotation they SHOULD compensate per *SRE* ch. 11 guidance (time off
in lieu, at minimum).

## Burnout safeguards

- No engineer on primary for two consecutive weeks.
- More than two pages per shift on average for four weeks triggers a reliability-debt review.
- Escalation paths are published and tested monthly.

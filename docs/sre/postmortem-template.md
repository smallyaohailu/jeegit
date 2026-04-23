# Postmortem — <title>

> Template follows Google's public example at <https://sre.google/sre-book/example-postmortem/>.
> Postmortems are **blameless**: they describe what happened, not who to fault. If the document
> below names a person, that person is the author.

## Metadata

- **Incident ID:**
- **Severity:**
- **Date:**
- **Duration:**
- **Author(s):**
- **Status:** DRAFT · IN REVIEW · FINAL

## Summary

One paragraph. What happened, in plain language.

## Impact

- Which users / tenants / regions?
- How many requests / dollars / hours were affected?
- Which SLO was burned? By how much?

## Timeline (UTC)

| Time | Event |
| --- | --- |
| 13:00 | Alert fires on `http_server_requests_seconds_count{outcome="SERVER_ERROR"}` |
| 13:02 | IC acknowledges; ops lead begins rollback |
| … | … |

## Root cause

Technical description. Aim for the *five whys* depth, not a single sentence. Include a short
excerpt of the offending code or config.

## Detection

Did monitoring fire first, or a user? How long did detection take?

## Resolution

Exact steps that restored the SLO. Include commands, PR numbers, configuration changes.

## Did we do well?

- What mitigations landed quickly?
- Which runbooks or dashboards helped?

## Where did we go wrong?

- What slowed us down?
- What was missing in our observability?

## Action items

| # | Action | Owner | Due | Tracking |
| --- | --- | --- | --- | --- |
| AI-1 | Add alert for X | @oncall |  |  |
| AI-2 | Rewrite runbook Y | @author |  |  |

Each action item MUST have an owner, a due date, and a tracking link. Action items without a
tracking link are not action items; they are good intentions.

## Lessons learned

Two or three bullet points that a new contributor will find valuable a year from now.

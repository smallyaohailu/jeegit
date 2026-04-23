# Design doc template

> Adapted from *Design Docs at Google* (Industrial Empathy, 2020) and the internal Google design
> doc norms described in *Software Engineering at Google* ch. 14. A design doc is a lightweight
> decision artefact, not a specification; keep it short and focused on the problem and the
> chosen trade-off.

---

## Title · Author(s) · Status · Last updated

<!-- Status: DRAFT | IN REVIEW | APPROVED | SUPERSEDED -->

## Context

Two or three paragraphs. What is the current state of the system? Why does this change need to be
discussed now? Link to the relevant code paths, the governance charter, and any upstream
discussion.

## Goals

Bulleted list of measurable outcomes.

- …
- …

## Non-goals

Bulleted list. Explicitly call out what this design will not address so reviewers do not waste
time asking about it.

## Overview

One-diagram summary (ASCII, Mermaid, or linked image). Optional but strongly recommended.

## Detailed design

### Data model

Schemas, entities, migrations. Prefer a table with column · type · nullability · description. If
you introduce a new aggregate root, spell out its tenant-isolation story.

### API surface

Request/response shapes, error envelopes, pagination. Anchor naming decisions against
[`docs/engineering/api-design-style.md`](./api-design-style.md).

### Agent / tool / event impact

Is there a new `Tool`, a new `Agent`, or a new topic on the `EventBus`? List them here so the
reviewer can confirm HITL and audit coverage.

### Observability

Metrics added, trace spans renamed, dashboards updated. Default stance: any new code path that
writes to the database or calls a model has a metric and an audit row.

### Security / privacy

Threats from the [`SECURITY.md`](../../SECURITY.md) model that apply to this change, and how they
are mitigated. Explicitly note whether any new data is classified as personal or sensitive.

### Internationalization

New user-facing strings go into `messages.properties` + 11 translated bundles and — if the change
has UI — into `src/main/resources/static/i18n/*.json`. Reviewers reject documents that skip this.

## Alternatives considered

At least two alternatives with the reason they were rejected. A design doc without rejected
alternatives is not yet a design doc.

## Cross-cutting impact

- Performance
- Cost
- Backward compatibility
- Operational runbooks

## Rollout plan

1. Land the change behind a feature flag / config toggle when possible.
2. Internal preview on the `cursor/**` branch.
3. CI green → merge to `master`.
4. Docker image build → docker-compose sanity check.
5. Tag release → bump `PLATFORM_VERSION` → update `RELEASE_NOTES.md`.

## Deprecation / migration

If the change removes or renames anything, describe the deprecation window (minimum one minor
release) and the migration path.

## Open questions

Bulleted. Resolve every open question before status → APPROVED.

## Approvals

| Role | Reviewer | Date |
| --- | --- | --- |
| Engineering |  |  |
| Product |  |  |
| Security |  |  |

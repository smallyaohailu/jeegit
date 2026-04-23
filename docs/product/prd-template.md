# Product Requirements Document

> Template shape follows the PRD pattern ex-Google PMs describe publicly
> ([*How to Write a Great PRD* — Fellow/Atlassian reprints of Google-internal examples](https://rework.withgoogle.com/guides/set-goals-with-okrs/)).
> A PRD is a one-document contract between product and engineering about what we're solving and
> why — not how.

---

## Title · Owner · Reviewers · Status · Last updated

Status: DRAFT · IN REVIEW · APPROVED · DEPRECATED

## TL;DR

Two sentences. If a reader reads nothing else, they read this.

## Problem

- Who has the problem?
- How does it hurt today?
- What is the cost of not solving it?

Cite qualitative evidence (conversations) and quantitative signals (telemetry, surveys).

## Users and use cases

| Persona | Use case | Frequency | Success criterion |
| --- | --- | --- | --- |
| e.g. Government clerk | Route incoming requests to the right department | daily, ≥ 30 / shift | Time to route < 10 s; explanation visible in audit |
| e.g. ISV engineer | Build a tenant-specific workflow on top of jeegit | per launch | MVP ships in < 2 weeks |

## Goals (MoSCoW)

- **Must** — measurable outcomes that define success.
- **Should** — nice-to-have by launch.
- **Could** — explicitly deferred.
- **Won't** — explicitly not in this PRD.

## Non-goals

Bulleted list — helps future readers distinguish scope drift from new work.

## Key metrics

| Metric | Pre-launch baseline | Target | Tracking source |
| --- | --- | --- | --- |

## Competitive / complementary landscape

Honest assessment. jeegit does not pretend to replace every alternative; cite where we compete,
where we integrate, and where we defer.

## Risks and mitigations

| Risk | Likelihood | Impact | Mitigation |
| --- | --- | --- | --- |

## Dependencies

- **Engineering** — modules, storage, AI providers.
- **Product** — prior PRDs, roadmap alignment.
- **Legal / compliance** — data-classification review, OSS licence audit.

## Rollout

1. Design doc (see `docs/engineering/design-doc-template.md`).
2. CI + feature flag.
3. Fishfood on the preview tenant.
4. Dogfood internally for ≥ 1 week.
5. GA: bump `PLATFORM_VERSION`, update `RELEASE_NOTES.md`, publish blog post in 12 locales.

## Appendices

- Linked design docs
- Linked postmortems
- Linked telemetry dashboards

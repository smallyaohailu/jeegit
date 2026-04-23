# OKRs — Objectives and Key Results

We run on quarterly OKRs and an annual "North Star." The cadence mirrors the public
[Google re:Work guide](https://rework.withgoogle.com/guides/set-goals-with-okrs/).

## Principles

- **Objectives are qualitative, aspirational, short.** One sentence that could be printed on a
  T-shirt.
- **Key Results are quantitative, dated, and make the Objective measurable.** 3–5 per Objective.
- **A 70 % hit rate is healthy.** 100 % means the KRs were not ambitious; 0 % means they were not
  realistic.
- **Grading is honest and public.** At quarter end each KR is scored 0.0 – 1.0 with a short note.

## 2026 North Star

> Become the default **AI-Native application framework** for enterprises and public-sector teams
> that need auditable, multi-tenant, multilingual software on a stable JVM stack.

## Q2 2026 OKRs

### O1 · Ship a production-grade 1.1 that operators can run unattended

- **KR1** · `mvn verify` passes on the CI workflow for 100 % of master commits.
- **KR2** · 99.5 % availability on `GET /api/v1/**` across a 30-day rolling window on a hosted
  preview.
- **KR3** · 30 automated tests covering every module (achieved: 43).
- **KR4** · Docker image < 200 MB uncompressed.

### O2 · Earn developer trust in the multilingual story

- **KR1** · First-party translations for 12 locales shipping in the binary (achieved).
- **KR2** · Localized README landing pages for 12 locales (achieved).
- **KR3** · Response `meta.locale` present on 100 % of REST responses (achieved — covered by
  `ApiResponseTest`).
- **KR4** · Zero locale-regression defects in the backlog.

### O3 · Make AI behaviour auditable end-to-end

- **KR1** · Every `MODEL_CALL` / `AGENT_RUN` / `AGENT_GATE` produces a row in `jg_audit_log`.
- **KR2** · Evaluation-harness endpoint in production for two reference agents.
- **KR3** · HITL guard blocks 100 % of HIGH-risk actions unless explicitly approved.
- **KR4** · At least one design doc per quarter that cites the architecture charter by line
  number when arguing for an exception.

## Q3 2026 draft OKRs

### O1 · Expand the ecosystem

- **KR1** · Ship an OpenAI-compatible Model Gateway validated against at least three providers
  in the CI matrix.
- **KR2** · Provide a pgvector-backed RAG implementation with an integration test against a real
  Postgres container.
- **KR3** · Publish a migration guide from one competing low-code platform.

### O2 · Deepen governance

- **KR1** · Move the rate limiter's state to Redis for cluster-wide correctness.
- **KR2** · Add field-level data classification tags on every PII column with CI enforcement.

## Grading ritual

1. Last week of each quarter: each OKR owner posts the scores and commentary.
2. Quarterly retrospective: discuss misses, not wins.
3. Next quarter's OKRs land on trunk as an update to this document; previous ones move to
   `docs/product/okrs-archive/`.

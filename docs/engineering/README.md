# Engineering practice (Google-aligned)

jeegit's engineering practice follows the operating model of Google's software teams as
described in *Software Engineering at Google* (the "O'Reilly Green Book") and reinforced by
Google's public OSS guides. This directory captures the artefacts that model requires so
contributors can work with the same cadence and reviewers can apply the same rubric.

| Artefact | Google reference | Path |
| --- | --- | --- |
| Design doc template | [*Design Docs at Google* — Industrial Empathy](https://www.industrialempathy.com/posts/design-docs-at-google/) | [`design-doc-template.md`](./design-doc-template.md) |
| API design style | [Google Cloud API Design Guide (AIP)](https://google.aip.dev/) | [`api-design-style.md`](./api-design-style.md) |
| Testing strategy | [*Software Engineering at Google* ch. 11 — Testing Overview](https://abseil.io/resources/swe-book/html/ch11.html) · [Testing on the Toilet](https://testing.googleblog.com/) | [`testing-strategy.md`](./testing-strategy.md) |
| Code-review practice | [*Google Code Review Developer Guide*](https://google.github.io/eng-practices/review/) | [`code-review.md`](./code-review.md) |
| Release engineering | [*Site Reliability Engineering* ch. 8](https://sre.google/sre-book/release-engineering/) | [`release-engineering.md`](./release-engineering.md) |

## Core invariants

These invariants come directly from Google's public engineering guidance:

1. **Respect the trunk.** One main branch, small changes, CI on every push. We follow this
   literally: PRs through `cursor/**` branches, squash-merged into `master`, gated by
   `.github/workflows/ci.yml`.
2. **Tests are first-class.** Every behaviour-changing commit either adds or updates tests.
   Coverage is a diagnostic, not a goal — see [`testing-strategy.md`](./testing-strategy.md).
3. **Readability before cleverness.** We adopt the Google Java Style Guide and enforce it via
   Spotless + `google-java-format` so reviewers can focus on intent rather than formatting.
4. **API stability is a contract.** Public REST endpoints and event topics follow the AIP
   conventions summarised in [`api-design-style.md`](./api-design-style.md); breaking a v1
   contract requires an RFC (see `docs/ARCHITECTURE_CHARTER.md` §8).
5. **Design before code, when design is non-trivial.** Use the template to socialise changes
   that alter a module boundary, add a storage format, or introduce an external dependency.

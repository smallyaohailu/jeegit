# ADR 0001 · Adopt Google engineering methodology

- **Status:** Accepted
- **Date:** 2026-04-23
- **Owner:** jeegit maintainers

## Context

The jeegit repository had accumulated high-quality software practices over thirty auto-iteration
rounds (trunk-based development, CI gate, Google Java Style via Spotless, etc.) but the broader
methodology — product, reliability, operations, governance — was carried implicitly. Contributors
needed a single, citable answer to the question *"what rules is this project run by?"* and
downstream adopters needed an explicit frame to map their own processes against.

We evaluated the mainstream frameworks available in 2026:

- **Google's public engineering practice.** Described in *Software Engineering at Google*, the
  *Site Reliability Engineering* trilogy, the *Google Code Review Developer Guide*, the
  [Google AIP](https://google.aip.dev/) API style, and the *re:Work* manager/product guides.
- **Microsoft Engineering Playbook.** Well-documented but heavier on Azure integration than we
  need.
- **Spotify Engineering Culture.** Strong on team topology but light on API design and SRE.
- **ThoughtWorks Tech Radar norms.** Useful for signal on practices but not itself a full
  methodology.

## Decision

Adopt Google's public engineering, SRE, API design, and product methodologies as jeegit's
reference frame. Specifically:

1. **Code + reviews.** Google Java Style Guide (enforced). Google Code Review Developer Guide as
   the reviewer's rubric.
2. **Architecture change.** Lightweight design docs modelled on *Design Docs at Google*, with a
   template in `docs/engineering/design-doc-template.md`.
3. **APIs.** Google AIPs inform versioning, pagination, error model, resource naming — summarised
   in `docs/engineering/api-design-style.md`.
4. **Testing.** *Software Engineering at Google* ch. 11 sizes (small/medium/large/enormous).
5. **Reliability.** SLO · error-budget · blameless postmortem cycle from the *SRE* books.
6. **Product.** PRD + OKR + launch-checklist artefacts patterned on Google re:Work and SRE
   ch. 27.
7. **Repository hygiene.** `.editorconfig`, `CODEOWNERS`, issue forms, PR template, ADR
   directory — mirrors the layout Google's open-source projects use on GitHub.
8. **Business model.** Apache-2.0 code; revenue lives in services, training, and managed
   runtimes — the stance Google Cloud takes for adjacent OSS stacks.

## Consequences

### Positive

- Contributors with Google / ex-Google experience are productive from day one.
- Downstream enterprise adopters can cite a well-known methodology in RFPs.
- Decisions produce durable artefacts (design docs, ADRs, postmortems) rather than Slack
  history.
- Every governance question has exactly one home in the tree.

### Negative

- More doc surface to keep current. Mitigation: the CI is already the gate on style; we add a
  *documentation freshness* review step at release time instead of a per-PR enforcement.
- Some Google practices (e.g. internal tooling like Critique, Blaze, Monarch) do not have a
  1:1 public equivalent; we map them to open substitutes (GitHub PR review, Maven, Prometheus).

### Neutral

- This ADR does not change any user-visible behaviour today. Its value compounds over
  subsequent releases.

## Alternatives considered

1. **Keep methodology implicit.** Rejected: each PR re-litigated shared norms.
2. **Adopt a narrower subset (only SRE, only AIP).** Rejected: reliability without product
   discipline reintroduces the "ship fast, cry later" loop; product without reliability loses
   trust.
3. **Invent a bespoke methodology.** Rejected: not a differentiator. We'd rather stand on the
   shoulders of the best-documented engineering culture in the industry.

## References

- *Software Engineering at Google* — Winters, Manshreck, Wright (O'Reilly 2020)
- *Site Reliability Engineering* — Beyer et al. (O'Reilly 2016)
- *The Site Reliability Workbook* — Beyer et al. (O'Reilly 2018)
- *Design Docs at Google* — Malte Ubl, Industrial Empathy (2020)
- Google AIP — <https://google.aip.dev/>
- Google Code Review Developer Guide — <https://google.github.io/eng-practices/review/>
- Google re:Work, OKR guide — <https://rework.withgoogle.com/guides/set-goals-with-okrs/>
- Google Style Guides — <https://google.github.io/styleguide/>

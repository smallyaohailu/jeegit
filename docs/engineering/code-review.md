# Code review practice

Adapted from the [Google Code Review Developer Guide](https://google.github.io/eng-practices/review/).
Reviewers apply the same rubric as the Google internal review process; authors apply the same
authoring guidance. Both roles are equal in jeegit.

## The reviewer's job

Ask, in order:

1. **Design.** Does this change belong in this codebase, and does it fit the architecture
   (`docs/ARCHITECTURE_CHARTER.md`)?
2. **Functionality.** Does it do what the author says? Are edge cases handled?
3. **Complexity.** Is it simpler than it could be? The "over-engineered" verdict is an acceptable
   block — cite this line when you use it.
4. **Tests.** Is the behaviour covered? See `testing-strategy.md`.
5. **Naming / comments.** Can a new contributor parse this next year?
6. **Style.** Spotless already ran — if it didn't, block.
7. **Documentation.** If the change affects an API, an event, or a runbook, is the doc updated in
   the same PR?

A reviewer who only comments on style is failing the review.

## The author's job

- Write a PR description that explains *why* first, *what* second, *how* last.
- Keep the diff small. If it cannot be small, split it.
- Respond to every comment, even if the response is "good catch, fixed in abc1234".
- Treat review as collaboration; remember the reviewer is investing in the change, not
  gatekeeping.

## "LGTM" and approval

- **LGTM with comments** is acceptable once all blocking concerns are addressed.
- At least one maintainer must approve before merge. For changes that touch the architecture
  charter, product charter, or AI governance document, two approvals are required.
- Spotless / CI must be green.
- The PR must link the design doc when one exists.

## Speed

Target: first review within one business day. If a reviewer is stuck, reassign rather than sit
on the PR — stale reviews erode trust faster than any bug does.

## Tooling

- `mvn spotless:apply` locally before pushing.
- `gh pr checks` to inspect CI from the command line.
- `docs/engineering/design-doc-template.md` for design-sized changes.

# Pull request

<!-- Follow https://google.github.io/eng-practices/review/developer/cl-descriptions.html -->

## Why

Explain the problem this change solves. One or two sentences; link to the design doc, PRD,
or issue.

## What

Bullet points describing the observable behaviour change.

## How

Short paragraph describing the approach. Call out any alternatives you considered and why
this one won.

## Checklist

- [ ] Follows the architecture charter (`docs/ARCHITECTURE_CHARTER.md`).
- [ ] `mvn spotless:check` + `mvn verify` green locally.
- [ ] Unit or integration tests added / updated (see `docs/engineering/testing-strategy.md`).
- [ ] Public API changes respect `docs/engineering/api-design-style.md`.
- [ ] User-facing strings added to `messages.properties` and the eleven translated bundles
      (when the change has UI, also `src/main/resources/static/i18n/*.json`).
- [ ] RELEASE_NOTES.md updated if the change is user-visible.
- [ ] Observability impact considered (metric / trace / audit row).
- [ ] Security impact considered (see `SECURITY.md`).

## Screenshots / artefacts

If this change affects the console, attach a screenshot (Material 3 conventions). If it changes
the CLI or API surface, paste the relevant transcript.

## Rollout

- Rollback plan: …
- Feature flag: …
- Migration required: …

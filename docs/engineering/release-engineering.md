# Release engineering

Patterned on the operating model in *Site Reliability Engineering* chapter 8. jeegit ships
continuously; versioned releases are cut from trunk on demand.

## Cadence

- **Trunk-based development.** `master` is always releasable.
- **Hermetic builds.** `mvn clean verify` must pass on CI against the same `pom.xml` checksums
  that ship to the Docker image build stage.
- **Versioned releases.** We cut a release by bumping `JeegitConstants.PLATFORM_VERSION` and
  appending a section to `RELEASE_NOTES.md`. The tag is `v<semver>`.
- **Emergency fixes.** A security or availability fix may be cherry-picked to a `release/<x.y>`
  branch, version-bumped to `<x.y.z+1>`, and tagged separately.

## Gates

Every commit to `master` has passed:

1. `mvn spotless:check`
2. `mvn clean verify`
3. The multi-stage Docker image build (`.github/workflows/ci.yml`, `docker` job)
4. Human review with at least one maintainer approval

## Artefacts

- **Maven jar.** `jeegit-bootstrap/target/jeegit-bootstrap.jar`
- **Docker image.** Built from the repository root `Dockerfile`; non-root runtime user; JRE-only
  runtime stage.
- **OpenAPI spec.** `/v3/api-docs` at runtime; also checked into the release notes when it
  grows.
- **Release notes.** `RELEASE_NOTES.md`, newest first.

## Post-deploy verification

For each release we run the regression script archived under
`/opt/cursor/artifacts/jeegit_v<version>_regression.log`. It exercises:

- `/actuator/health`
- 12 locales on `/api/v1/platform/info`
- API-key create / revoke
- Matter intake → dispatch → audit cycle
- Evaluation harness on `agent.intake.dispatch`
- Prometheus scrape

A release is complete only when this log is captured and linked from the PR / tag notes.

## SLO alignment

Release engineering feeds the SLO feedback loop in
[`docs/sre/slo.md`](../sre/slo.md). A release that regresses a guarded SLI does not receive the
approval to merge — that is the point of the error budget.

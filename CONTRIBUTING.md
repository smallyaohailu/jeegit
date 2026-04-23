# Contributing to jeegit

Thank you for considering a contribution.

## Code of conduct

Participation is governed by the [Contributor Covenant 2.1](CODE_OF_CONDUCT.md). By participating
you agree to uphold it.

## Reporting issues

Open a GitHub Issue. Please include:

1. A concise, factual title.
2. Reproduction steps or a minimal failing test.
3. Environment: JDK version, OS, database, Spring Boot version if you overrode it.
4. Whether the issue is a security report — if so, please follow the process in
   [`SECURITY.md`](SECURITY.md) instead.

## Development workflow

```bash
# 1. Fork, clone, branch
git checkout -b feature/your-change

# 2. Build + test
mvn -B verify              # runs Spotless + unit/integration tests

# 3. Format (if you skipped the build gate)
mvn spotless:apply

# 4. Run the preview
java -jar jeegit-bootstrap/target/jeegit-bootstrap.jar
```

### Style

- Java code follows the **Google Java Style Guide** (enforced by `com.diffplug.spotless` with
  `google-java-format 1.22.0`). The pre-commit invariant is `mvn spotless:check`; CI fails if
  the tree drifts.
- Comments, Javadoc, commit messages, and code identifiers are written in **English**.
- User-facing strings must live in `jeegit-common/src/main/resources/i18n/messages.properties`
  and the eleven translated bundles next to it.

### Architectural guardrails

The Architecture Charter enumerates non-negotiable principles
([`docs/ARCHITECTURE_CHARTER.md`](docs/ARCHITECTURE_CHARTER.md)). In short:

- Business code never calls vendor SDKs directly — always route through `ModelGateway`.
- Agents only mutate state via tools on their allow-list.
- Every aggregate root carries a `tenant_id`.
- Audit entries are append-only.
- New AI features ship with an evaluation suite (`POST /api/v1/ai/eval/{agentId}/run`).

## Tests

- Unit tests live under each module's `src/test/java/...` following the `*Test` naming convention.
- Integration tests that require a running Spring context extend Spring Boot test slices.
- Prefer fakes/stubs over mocks when verifying domain services.
- CI: `mvn verify` must stay green on every commit. See `.github/workflows/ci.yml`.

## Internationalization

- Add new keys to `messages.properties` first, then mirror them to the remaining 11 locale
  bundles.
- Do not translate keys — only values.
- When adding UI strings, update `src/main/resources/static/i18n/<locale>.json` as well; front-end
  strings flow through the same 12-locale commitment.
- RTL must keep working — don't hard-code left/right paddings without a mirror via
  `dir="rtl"`.

## Commit conventions

- One logical change per commit.
- Subject is imperative and ≤ 72 chars: `feat(tech): add data-scope CUSTOM`.
- Larger changes belong in their own PR; reviewers should not feel obliged to hold context across
  an unrelated diff.

## Security

Never open a public issue for a vulnerability. Follow [`SECURITY.md`](SECURITY.md).

## License

By contributing you agree that your contribution will be licensed under the project's
[Apache 2.0 license](LICENSE).

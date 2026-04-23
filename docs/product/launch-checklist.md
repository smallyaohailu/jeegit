# Launch checklist (LCR-style)

Adapted from the Launch Coordination Review in *Site Reliability Engineering* chapter 27. Any
new module, feature flag, or externally-visible change runs through this checklist before it
can be tagged as GA.

## 1. Product

- [ ] PRD approved (`docs/product/prd-template.md`).
- [ ] User impact mapped to a persona in `PRODUCT_CHARTER.md`.
- [ ] Rollout metrics defined on a dashboard.
- [ ] Non-goals recorded — no scope creep.

## 2. Engineering

- [ ] Design doc approved (`docs/engineering/design-doc-template.md`).
- [ ] Architecture-charter principles respected or an RFC cites the exception.
- [ ] Code review: at least one maintainer; two if this touches the charters.
- [ ] Spotless + `mvn verify` green on CI.
- [ ] Public API follows `docs/engineering/api-design-style.md` (version, naming, errors,
      envelope).

## 3. Reliability

- [ ] SLO updated or reaffirmed in `docs/sre/slo.md`.
- [ ] Runbook section added to `docs/sre/incident-response.md` ("Mitigations we own").
- [ ] Alert defined in Prometheus / Alertmanager (or the appropriate vendor tool).
- [ ] Error-budget impact assessed.

## 4. Security / privacy

- [ ] Threat model reviewed against `SECURITY.md`.
- [ ] New data columns classified (public / internal / confidential / restricted).
- [ ] Logs scrubbed of secrets.
- [ ] HITL policy correct for any new agent / tool.

## 5. Internationalization

- [ ] New user-facing strings in `messages.properties` and the 11 translated bundles.
- [ ] Frontend JSON bundles (`/static/i18n/*.json`) updated for the 12 locales.
- [ ] Manual check in en, zh-CN, ja, ar (RTL).

## 6. Observability

- [ ] Metrics named per Micrometer conventions (`domain_resource_total`).
- [ ] Trace spans cover the critical request path.
- [ ] Dashboards updated.

## 7. Documentation

- [ ] README (top-level + 12 localized) references the new capability.
- [ ] OpenAPI description regenerated (automatic via springdoc) and path count noted in
      `RELEASE_NOTES.md`.
- [ ] CHANGELOG-style entry added to `RELEASE_NOTES.md` before the tag is cut.

## 8. Launch communications

- [ ] Announcement drafted in English.
- [ ] Translation pipeline kicked off for non-English README updates (community PRs welcome).
- [ ] GA email / blog / repo pinned issue published.

## 9. Post-launch

- [ ] Dogfood for one week.
- [ ] Launch postmortem even on success — 30 minutes, 3 wins, 3 improvements.
- [ ] Update OKRs with observed progress on the relevant KRs.

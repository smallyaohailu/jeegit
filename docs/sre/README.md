# Site Reliability Engineering

jeegit adopts the Google SRE operating model: measure service-level **indicators**, commit to
service-level **objectives**, spend the **error budget** on velocity, write **blameless
postmortems** when things break. This folder holds the artefacts that model requires.

| Artefact | Google reference | Path |
| --- | --- | --- |
| Service level objectives | [*Site Reliability Engineering* ch. 4](https://sre.google/sre-book/service-level-objectives/) | [`slo.md`](./slo.md) |
| Error-budget policy | [*The Site Reliability Workbook* ch. 4](https://sre.google/workbook/error-budget-policy/) | [`error-budget-policy.md`](./error-budget-policy.md) |
| Incident response | [*SRE* ch. 13 & 14](https://sre.google/sre-book/managing-incidents/) | [`incident-response.md`](./incident-response.md) |
| Postmortem template | [Google SRE postmortem template](https://sre.google/sre-book/example-postmortem/) | [`postmortem-template.md`](./postmortem-template.md) |
| On-call rotation | [*SRE* ch. 11](https://sre.google/sre-book/being-on-call/) | [`on-call.md`](./on-call.md) |

## Philosophy (short form)

1. **Hope is not a strategy.** If it isn't measured, it isn't reliable.
2. **100% is the wrong SLO.** Aim for the right level, then spend the remaining budget on
   feature velocity.
3. **Write it down.** Runbooks, postmortems, SLO decisions — treat them as production code.
4. **Blameless by design.** Postmortems analyse systems, not people.

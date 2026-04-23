# Error-budget policy

Taken almost verbatim from *The Site Reliability Workbook* ch. 4. The policy turns the SLOs in
[`slo.md`](./slo.md) into concrete rules that govern how we spend release velocity.

## Intent

- The error budget is the difference between **100 %** and the **SLO target**, integrated over
  the SLO window.
- The error budget belongs to the product, not to the engineers. When it is spent, engineering
  velocity pays the price — not users.

## Budget state

| Remaining budget | Posture |
| --- | --- |
| > 50 % of window | **Green.** Normal velocity. Experimental launches are allowed. |
| 25 – 50 % | **Yellow.** Every PR still ships; the oncall scrutinises risky changes more carefully; a change correlated to an incident can be reverted without debate. |
| 0 – 25 % | **Red.** Feature work pauses for engineers oncall. Only reliability, security, and bug fixes land. A short-lived "freeze" is acceptable; anything longer requires a product-owner sign-off. |
| Exceeded | **Breach.** Freeze all non-reliability changes. Begin root-cause / postmortem within 24 h. The freeze lifts only when the budget burn has decayed below 25 % *and* the postmortem has action items in flight. |

## Who invokes it

- Any maintainer may call Yellow or Red by opening an issue with label `reliability-risk`.
- The on-call lifts Red back to Yellow when the burn rate drops below 2x the steady state for
  six continuous hours.
- The product owner may explicitly grant an exemption for a single PR in Red — logged in the PR
  description.

## How we burn budget

- Every 5xx response on an SLO-guarded endpoint.
- Every request that exceeds its p95 latency target by 2x or more.
- Every AI call that audits as `DENIED` because of a runtime error (not because of a legitimate
  policy / HITL block).
- Every incident that lasts more than 5 minutes counts as a separate event on the availability
  SLO.

## What we DO NOT do

- We do not extend the SLO window to hide a breach.
- We do not change the SLO target during the breach — we change the target on the next
  retrospective, and we document why.

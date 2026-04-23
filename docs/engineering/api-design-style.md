# API design style (Google-aligned)

jeegit's public HTTP surface follows the Google Cloud *API Improvement Proposals* (AIP). AIPs are
the canonical expression of the API design rules Google uses internally; adopting them gives our
partners the same ergonomics they experience with Google APIs and keeps the surface consistent
across modules. This page summarises the subset we commit to; the authoritative AIP text lives at
<https://google.aip.dev/>.

## Baseline

| Area | Rule | AIP |
| --- | --- | --- |
| Version prefix | `/api/v{major}` only — a major bump never breaks the previous version | [AIP-122](https://google.aip.dev/122) |
| Resource naming | Plural nouns in kebab-case collections (`/api/v1/matters`) | [AIP-121](https://google.aip.dev/121) · [AIP-135](https://google.aip.dev/135) |
| HTTP methods | `GET` = read, `POST` = create or action, `PATCH` = partial update, `DELETE` = soft-delete | [AIP-131](https://google.aip.dev/131) |
| Pagination | `page`, `size` query params, capped server-side; response returns `totalElements` and `totalPages` | [AIP-158](https://google.aip.dev/158) |
| Error model | Unified `ApiResponse` envelope with `code`, `message`, `meta.locale`; HTTP status mirrors the code family | [AIP-193](https://google.aip.dev/193) |
| Field naming | camelCase in JSON bodies; snake_case SQL column names map via JPA | [AIP-140](https://google.aip.dev/140) |
| Long-running | Reserved for future use — when we need it, we'll model it on `Operation` | [AIP-151](https://google.aip.dev/151) |

## Response envelope

Every successful response:

```json
{
  "success": true,
  "code": "OK",
  "message": "success",
  "data": { "…": "…" },
  "timestamp": "2026-04-23T13:14:15Z",
  "meta": { "locale": "en" }
}
```

Every error response:

```json
{
  "success": false,
  "code": "RATE_LIMITED",
  "message": "Rate limit exceeded for tenant 'acme' (300 requests / 60 s)",
  "data": null,
  "timestamp": "2026-04-23T13:14:15Z",
  "meta": { "locale": "zh-CN" }
}
```

Stable error codes currently used:

| Code | HTTP | Meaning |
| --- | --- | --- |
| `OK` | 200 | success |
| `BAD_REQUEST` | 400 | client-side validation failure |
| `VALIDATION_FAILED` | 400 | @Valid failure (per-field reasons in `message`) |
| `CONFLICT` | 409 | state-machine violation |
| `RATE_LIMITED` | 429 | per-tenant rate limit exceeded |
| `INTERNAL_ERROR` | 500 | unhandled exception — logged with a trace id |

## Authentication

| Mechanism | Use | Header |
| --- | --- | --- |
| HTTP Basic | human operators, CI scripts | `Authorization: Basic …` |
| API Key | partner services | `X-API-Key: jeegit_…` |
| Reads | always public in the preview — gated by your reverse proxy in production |   |

## Localization

Clients SHOULD send `Accept-Language`. Servers MUST report the resolved locale back in
`meta.locale` (BCP-47 tag). A `?lang=xx` query parameter is honoured for debugging.

## Events

Domain events on the in-process `EventBus` use the AsyncAPI-style topic name
`jeegit.{domain}.{event}.v1`. Examples:

- `jeegit.matter.submitted.v1`
- `jeegit.matter.dispatched.v1`

Breaking an event payload is the same kind of change as breaking an HTTP contract — bump the
version suffix.

## Versioning and deprecation

- `v1` is the current major version.
- Fields may be **added** in a backwards-compatible way at any time.
- Fields may be **deprecated** with a minimum one-minor-release overlap; deprecation is annotated
  in the OpenAPI description (`deprecated: true`) and in `RELEASE_NOTES.md`.
- Breaking changes bump the major version and live alongside the previous version until the
  support window closes (see `SECURITY.md`).

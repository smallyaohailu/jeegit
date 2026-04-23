# Architecture Decision Records

This directory follows Michael Nygard's ADR convention, which Google's OSS projects (e.g.
[`go/abseil-cpp`](https://github.com/abseil/abseil-cpp)) and the wider industry have converged on.

## Naming

`NNNN-short-slug.md` where NNNN is a zero-padded sequence number. Numbers are immutable once
merged; a superseded ADR is not renumbered.

## Lifecycle

- **Proposed** — draft on a branch.
- **Accepted** — merged to trunk.
- **Deprecated** — still applies but discouraged; a replacement ADR exists.
- **Superseded** — no longer applies; header names the replacement.

## Template

Use [`adr-template.md`](./adr-template.md) when you add one.

## Index

| # | Title | Status |
| --- | --- | --- |
| [0001](./0001-adopt-google-engineering-methodology.md) | Adopt Google engineering methodology | Accepted |

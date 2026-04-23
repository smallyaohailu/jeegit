# Supported Languages

jeegit uses **English as the primary development language** (code, comments, identifiers, docs) and
ships first-party localization for the following **12 locales**, matching the set most commonly
demanded by global enterprise and government customers and aligned with the language coverage
of the LangChain multilingual ecosystem.

| #  | Locale    | BCP‑47   | Display name            | Script     | Direction |
| -- | --------- | -------- | ----------------------- | ---------- | --------- |
| 1  | English   | `en`     | English                 | Latin      | LTR       |
| 2  | Chinese (Simplified)  | `zh-CN`  | 简体中文          | Han (Simp.) | LTR      |
| 3  | Chinese (Traditional) | `zh-TW`  | 繁體中文          | Han (Trad.) | LTR      |
| 4  | Japanese  | `ja`     | 日本語                  | Han + Kana | LTR       |
| 5  | Korean    | `ko`     | 한국어                  | Hangul     | LTR       |
| 6  | Spanish   | `es`     | Español                 | Latin      | LTR       |
| 7  | French    | `fr`     | Français                | Latin      | LTR       |
| 8  | German    | `de`     | Deutsch                 | Latin      | LTR       |
| 9  | Portuguese (Brazil)   | `pt-BR`  | Português (Brasil) | Latin   | LTR       |
| 10 | Russian   | `ru`     | Русский                 | Cyrillic   | LTR       |
| 11 | Italian   | `it`     | Italiano                | Latin      | LTR       |
| 12 | Arabic    | `ar`     | العربية                  | Arabic     | **RTL**   |

## Why these twelve

1. **Global coverage**: the top 12 locales cover > 60 % of global enterprise-IT spend and the
   largest e-government user populations.
2. **CJK parity**: first-class support for Simplified & Traditional Chinese, Japanese, and Korean —
   the majority of the Asian enterprise installed base.
3. **EU & Americas**: Spanish, French, German, Portuguese (Brazil), Italian for EU and LATAM.
4. **Cyrillic & RTL**: Russian and Arabic ensure the platform is stress-tested against both
   non-Latin scripts and right-to-left layouts.
5. **LangChain alignment**: the LangChain community tutorials and multilingual agents commonly
   target this same language band, so prompts, evaluation suites, and knowledge-base content
   authored for jeegit remain directly interoperable with LangChain-style workflows.

## Language selection & resolution

- The active locale is determined, in order of precedence:
  1. Request cookie `jeegit_locale` (if present).
  2. `Accept-Language` HTTP header (negotiated against the supported list).
  3. JVM default locale.
  4. Fallback: `en`.
- Every response body carries a `meta.locale` field so clients can observe which locale was used
  to render user-facing strings.
- Locales outside the supported list fall back to the closest supported language family
  (e.g. `zh-HK` → `zh-TW`, `pt-PT` → `pt-BR`, `es-MX` → `es`).

## Where translations live

- Bundled resources: `jeegit-common/src/main/resources/i18n/messages[_xx].properties`.
- Base bundle (`messages.properties`) is English and is always the source of truth; other
  bundles may lag behind and will fall back to English key-by-key.
- Documentation translations live under `docs/i18n/<locale>/` when contributed; the English
  documents under `docs/` remain authoritative.

## Adding a new translation

1. Copy `messages.properties` to `messages_<locale>.properties`.
2. Translate values only; never translate keys.
3. Run the application with `Accept-Language: <locale>` and verify the `api.success`,
   `platform.info.description`, and `agent.intake.reasoning.*` keys render correctly.
4. Open a PR — the default review criteria are **accuracy first, concision second, brand
   consistency third**.

## Non-goals

- Full machine translation of rule-table `itemValue` (keyword) data — those are configured per
  tenant by operators, not by the platform.
- Runtime re-translation of free-form user-entered content (titles, descriptions). These are
  stored and returned verbatim.

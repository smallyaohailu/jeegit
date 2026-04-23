package io.jeegit.common.i18n;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * The canonical list of locales jeegit ships first-party translations for.
 * See {@code docs/i18n/LANGUAGES.md} for the rationale behind the choice.
 */
public final class SupportedLocales {

    public static final Locale EN = Locale.ENGLISH;
    public static final Locale ZH_CN = Locale.SIMPLIFIED_CHINESE;
    public static final Locale ZH_TW = Locale.TRADITIONAL_CHINESE;
    public static final Locale JA = Locale.JAPANESE;
    public static final Locale KO = Locale.KOREAN;
    public static final Locale ES = Locale.of("es");
    public static final Locale FR = Locale.FRENCH;
    public static final Locale DE = Locale.GERMAN;
    public static final Locale PT_BR = Locale.of("pt", "BR");
    public static final Locale RU = Locale.of("ru");
    public static final Locale IT = Locale.ITALIAN;
    public static final Locale AR = Locale.of("ar");

    /** Ordered list of supported locales. */
    public static final List<Locale> ALL = List.of(
            EN, ZH_CN, ZH_TW, JA, KO, ES, FR, DE, PT_BR, RU, IT, AR);

    /** Default locale when no acceptable language can be negotiated. */
    public static final Locale DEFAULT = EN;

    /** Cookie name used by the UI to pin a user-selected locale. */
    public static final String LOCALE_COOKIE = "jeegit_locale";

    private SupportedLocales() {
    }

    /** Check whether the given locale (language+country) is supported as-is. */
    public static boolean isSupported(Locale locale) {
        if (locale == null) return false;
        String tag = locale.toLanguageTag();
        for (Locale supported : ALL) {
            if (supported.toLanguageTag().equalsIgnoreCase(tag)) return true;
        }
        return false;
    }

    /**
     * Best-effort reduction of an arbitrary client locale to one of the supported locales.
     * Rules:
     *   1. Exact language-tag match wins.
     *   2. Same language + country synonyms:  zh-HK/zh-MO -> zh-TW, zh-SG -> zh-CN,
     *      pt-PT -> pt-BR.
     *   3. Language-only match (ignoring country/variant) — picks the first supported entry
     *      with the same ISO-639 language code.
     *   4. Otherwise {@link #DEFAULT}.
     */
    public static Locale negotiate(Locale requested) {
        if (requested == null) return DEFAULT;
        if (isSupported(requested)) return requested;

        String lang = requested.getLanguage();
        String country = requested.getCountry();

        if ("zh".equalsIgnoreCase(lang)) {
            if ("HK".equalsIgnoreCase(country) || "MO".equalsIgnoreCase(country)
                    || Arrays.asList("Hant", "HANT").contains(requested.getScript())) {
                return ZH_TW;
            }
            return ZH_CN;
        }
        if ("pt".equalsIgnoreCase(lang)) return PT_BR;

        return ALL.stream()
                .filter(l -> l.getLanguage().equalsIgnoreCase(lang))
                .findFirst()
                .orElse(DEFAULT);
    }

    /** Parse a BCP-47 tag safely and return the supported equivalent. */
    public static Optional<Locale> parse(String tag) {
        if (tag == null || tag.isBlank()) return Optional.empty();
        try {
            Locale parsed = Locale.forLanguageTag(tag);
            if (parsed.getLanguage().isEmpty()) return Optional.empty();
            return Optional.of(negotiate(parsed));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }
}

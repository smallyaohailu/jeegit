package io.jeegit.common.i18n;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import org.springframework.web.servlet.LocaleResolver;

/**
 * Locale resolver that combines, in order of precedence: 1. A {@code jeegit_locale} cookie written
 * by the UI (or by {@link org.springframework.web.servlet.i18n.LocaleChangeInterceptor}). 2. The
 * {@code Accept-Language} request header. 3. {@link SupportedLocales#DEFAULT}.
 *
 * <p>Every resolved locale is folded back onto the supported list by {@link
 * SupportedLocales#negotiate(Locale)} so callers always get one of the twelve supported locales.
 */
public class JeegitLocaleResolver implements LocaleResolver {

  private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 365;
  private static final String REQUEST_ATTR = JeegitLocaleResolver.class.getName() + ".LOCALE";

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    Locale override = (Locale) request.getAttribute(REQUEST_ATTR);
    if (override != null) {
      return override;
    }
    Locale fromCookie = readCookie(request);
    if (fromCookie != null) {
      return SupportedLocales.negotiate(fromCookie);
    }
    Locale fromHeader = readAcceptLanguage(request);
    if (fromHeader != null) {
      return SupportedLocales.negotiate(fromHeader);
    }
    return SupportedLocales.DEFAULT;
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    Locale effective =
        locale == null ? SupportedLocales.DEFAULT : SupportedLocales.negotiate(locale);
    // Make the new choice visible to the current request immediately.
    if (request != null) {
      request.setAttribute(REQUEST_ATTR, effective);
    }
    if (response == null) {
      return;
    }
    Cookie cookie = new Cookie(SupportedLocales.LOCALE_COOKIE, effective.toLanguageTag());
    cookie.setPath("/");
    cookie.setHttpOnly(false);
    cookie.setMaxAge(locale == null ? 0 : COOKIE_MAX_AGE);
    response.addCookie(cookie);
  }

  private Locale readCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return null;
    for (Cookie c : cookies) {
      if (SupportedLocales.LOCALE_COOKIE.equals(c.getName())) {
        String value = c.getValue();
        if (value == null || value.isBlank()) continue;
        try {
          return Locale.forLanguageTag(value);
        } catch (Exception ignored) {
          return null;
        }
      }
    }
    return null;
  }

  private Locale readAcceptLanguage(HttpServletRequest request) {
    Enumeration<Locale> locales;
    try {
      locales = request.getLocales();
    } catch (Exception ex) {
      return null;
    }
    if (locales == null) return null;
    List<Locale> list = Collections.list(locales);
    for (Locale candidate : list) {
      Locale negotiated = SupportedLocales.negotiate(candidate);
      if (SupportedLocales.isSupported(negotiated) && negotiated != SupportedLocales.DEFAULT) {
        return negotiated;
      }
    }
    return list.isEmpty() ? null : list.get(0);
  }
}

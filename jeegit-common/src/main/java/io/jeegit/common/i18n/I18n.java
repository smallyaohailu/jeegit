package io.jeegit.common.i18n;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Thin wrapper around {@link MessageSource} so application code does not have to carry the locale
 * parameter everywhere. Resolves the current locale from {@link LocaleContextHolder}, which is
 * populated by Spring's locale resolver for each incoming request.
 */
@Component
public class I18n {

  private final MessageSource messageSource;

  public I18n(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /** Resolve {@code code} using the current request locale, falling back to the code itself. */
  public String t(String code, Object... args) {
    return t(LocaleContextHolder.getLocale(), code, args);
  }

  /** Resolve {@code code} for an explicit locale. */
  public String t(Locale locale, String code, Object... args) {
    Locale effective =
        locale == null ? SupportedLocales.DEFAULT : SupportedLocales.negotiate(locale);
    try {
      return messageSource.getMessage(code, args, effective);
    } catch (NoSuchMessageException ex) {
      return code;
    }
  }

  /** The locale Spring associated with the current request (already negotiated). */
  public Locale currentLocale() {
    return LocaleContextHolder.getLocale();
  }
}

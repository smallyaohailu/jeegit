package io.jeegit.common.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.Test;

class SupportedLocalesTest {

  @Test
  void twelveLocalesAreShipped() {
    assertThat(SupportedLocales.ALL).hasSize(12);
    assertThat(SupportedLocales.ALL).contains(SupportedLocales.AR, SupportedLocales.ZH_CN);
  }

  @Test
  void negotiatesExactMatch() {
    assertThat(SupportedLocales.negotiate(Locale.JAPANESE)).isEqualTo(SupportedLocales.JA);
  }

  @Test
  void zhHkFallsBackToZhTw() {
    assertThat(SupportedLocales.negotiate(Locale.forLanguageTag("zh-HK")))
        .isEqualTo(SupportedLocales.ZH_TW);
  }

  @Test
  void zhSgFallsBackToZhCn() {
    assertThat(SupportedLocales.negotiate(Locale.forLanguageTag("zh-SG")))
        .isEqualTo(SupportedLocales.ZH_CN);
  }

  @Test
  void ptPtFallsBackToPtBr() {
    assertThat(SupportedLocales.negotiate(Locale.forLanguageTag("pt-PT")))
        .isEqualTo(SupportedLocales.PT_BR);
  }

  @Test
  void unknownLanguageFallsBackToDefault() {
    assertThat(SupportedLocales.negotiate(Locale.forLanguageTag("xx-ZZ")))
        .isEqualTo(SupportedLocales.DEFAULT);
  }

  @Test
  void parseAcceptsValidTag() {
    assertThat(SupportedLocales.parse("fr-CA")).isPresent().contains(SupportedLocales.FR);
  }

  @Test
  void parseRejectsBlankInput() {
    assertThat(SupportedLocales.parse("")).isEmpty();
    assertThat(SupportedLocales.parse(null)).isEmpty();
  }
}

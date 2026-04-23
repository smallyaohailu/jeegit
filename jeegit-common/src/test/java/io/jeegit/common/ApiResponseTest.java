package io.jeegit.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

class ApiResponseTest {

  @AfterEach
  void resetLocale() {
    LocaleContextHolder.resetLocaleContext();
  }

  @Test
  void metaCarriesCurrentLocaleTag() {
    LocaleContextHolder.setLocale(Locale.JAPANESE);
    ApiResponse<String> response = ApiResponse.ok("hello");
    assertThat(response.meta()).containsEntry("locale", "ja");
    assertThat(response.success()).isTrue();
    assertThat(response.code()).isEqualTo("OK");
  }

  @Test
  void failEnvelopeHasCodeAndMessage() {
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    ApiResponse<Object> response = ApiResponse.fail("CONFLICT", "duplicate");
    assertThat(response.success()).isFalse();
    assertThat(response.code()).isEqualTo("CONFLICT");
    assertThat(response.message()).isEqualTo("duplicate");
    assertThat(response.meta()).containsEntry("locale", "en");
  }
}

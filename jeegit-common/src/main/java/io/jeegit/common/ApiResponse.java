package io.jeegit.common;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Unified response envelope used by every public REST endpoint.
 *
 * <p>The {@code meta} map always contains the negotiated {@code locale} tag so clients can observe
 * which language the server rendered user-facing strings in. Additional meta values can be provided
 * by callers via {@link #ok(Object, Map)}.
 */
public record ApiResponse<T>(
    boolean success,
    String code,
    String message,
    T data,
    Instant timestamp,
    Map<String, Object> meta) {

  public static <T> ApiResponse<T> ok(T data) {
    return ok(data, Map.of());
  }

  public static <T> ApiResponse<T> ok(T data, Map<String, Object> additionalMeta) {
    Map<String, Object> meta = withLocale(additionalMeta);
    return new ApiResponse<>(true, "OK", "success", data, Instant.now(), meta);
  }

  public static <T> ApiResponse<T> fail(String code, String message) {
    return new ApiResponse<>(false, code, message, null, Instant.now(), withLocale(Map.of()));
  }

  private static Map<String, Object> withLocale(Map<String, Object> additionalMeta) {
    Map<String, Object> meta = new LinkedHashMap<>();
    meta.put("locale", LocaleContextHolder.getLocale().toLanguageTag());
    if (additionalMeta != null) {
      meta.putAll(additionalMeta);
    }
    return Map.copyOf(meta);
  }
}

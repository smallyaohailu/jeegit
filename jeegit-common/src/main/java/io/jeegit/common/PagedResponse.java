package io.jeegit.common;

import java.util.List;
import java.util.Map;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;

/**
 * Standard paged response envelope. Always carries the negotiated locale in {@code meta}, just like
 * {@link ApiResponse}.
 */
public record PagedResponse<T>(
    boolean success,
    String code,
    List<T> data,
    long totalElements,
    int totalPages,
    int page,
    int size,
    Map<String, Object> meta) {

  public static <T> PagedResponse<T> of(Page<T> springPage) {
    Map<String, Object> meta = Map.of("locale", LocaleContextHolder.getLocale().toLanguageTag());
    return new PagedResponse<>(
        true,
        "OK",
        springPage.getContent(),
        springPage.getTotalElements(),
        springPage.getTotalPages(),
        springPage.getNumber(),
        springPage.getSize(),
        meta);
  }
}

package io.jeegit.common.web;

import io.jeegit.common.ApiResponse;
import io.jeegit.common.i18n.I18n;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global error-handling contract.
 *
 * <p>Every unhandled exception leaves the process through this class, which returns a unified
 * {@link ApiResponse} whose {@code code} is a stable, machine-readable error identifier and whose
 * {@code message} is localized through {@link I18n}.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  private final I18n i18n;

  public GlobalExceptionHandler(I18n i18n) {
    this.i18n = i18n;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(
      IllegalArgumentException ex, HttpServletRequest request) {
    log.debug("bad request at {}: {}", request.getRequestURI(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.fail("BAD_REQUEST", ex.getMessage()));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiResponse<Object>> handleIllegalState(
      IllegalStateException ex, HttpServletRequest request) {
    log.warn("conflict at {}: {}", request.getRequestURI(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ApiResponse.fail("CONFLICT", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleValidation(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> errors = new ArrayList<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(e -> errors.add(e.getField() + ": " + e.getDefaultMessage()));
    String summary = errors.stream().collect(Collectors.joining("; "));
    log.debug("validation failed at {}: {}", request.getRequestURI(), summary);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.fail("VALIDATION_FAILED", summary));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    String message =
        String.format(
            "parameter '%s' could not be converted to %s",
            ex.getName(),
            ex.getRequiredType() == null ? "required type" : ex.getRequiredType().getSimpleName());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.fail("BAD_REQUEST", message));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleUnknown(
      Exception ex, HttpServletRequest request) {
    log.error("unhandled exception at {}", request.getRequestURI(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.fail("INTERNAL_ERROR", i18n.t("api.fail.generic")));
  }
}

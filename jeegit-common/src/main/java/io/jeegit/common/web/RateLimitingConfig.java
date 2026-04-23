package io.jeegit.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jeegit.common.ApiResponse;
import io.jeegit.common.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Per-tenant rate limiting. Only {@code /api/v1/**} is limited; Swagger UI, actuator and static
 * assets are not. The feature is on by default; disable with {@code
 * jeegit.rate-limit.enabled=false}.
 */
@Configuration
@ConditionalOnProperty(
    name = "jeegit.rate-limit.enabled",
    havingValue = "true",
    matchIfMissing = true)
public class RateLimitingConfig {

  @Value("${jeegit.rate-limit.permits-per-minute:300}")
  private int permitsPerMinute;

  @Bean
  public SlidingWindowRateLimiter rateLimiter() {
    return new SlidingWindowRateLimiter(permitsPerMinute, 60_000L);
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE + 10)
  public OncePerRequestFilter rateLimitFilter(SlidingWindowRateLimiter limiter) {
    ObjectMapper mapper = new ObjectMapper();
    return new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/v1/")) {
          chain.doFilter(request, response);
          return;
        }
        String key = TenantContext.tenant();
        if (!limiter.tryAcquire(key)) {
          response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          response.setHeader("X-RateLimit-Limit", String.valueOf(limiter.permitsPerWindow()));
          response.setHeader("Retry-After", String.valueOf(limiter.windowMillis() / 1000));
          ApiResponse<Object> body =
              ApiResponse.fail(
                  "RATE_LIMITED",
                  "Rate limit exceeded for tenant '"
                      + key
                      + "' ("
                      + limiter.permitsPerWindow()
                      + " requests / "
                      + (limiter.windowMillis() / 1000)
                      + " s)");
          response.getWriter().write(mapper.writeValueAsString(body));
          return;
        }
        chain.doFilter(request, response);
      }
    };
  }
}

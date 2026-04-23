package io.jeegit.bootstrap;

import io.jeegit.common.TenantContext;
import io.jeegit.openapi.apikey.ApiKey;
import io.jeegit.openapi.apikey.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Turns a valid {@code X-API-Key} header into a Spring Security authentication so partner
 * applications using an issued API key can write to {@code /api/v1/**} without Basic credentials.
 *
 * <p>Also adopts the key's tenant into {@link TenantContext} for the remainder of the request. Bad
 * keys are silently ignored — Spring Security will fall through to Basic auth or reject the request
 * if the route requires authentication.
 */
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

  private final ApiKeyService apiKeyService;

  public ApiKeyAuthenticationFilter(ApiKeyService apiKeyService) {
    this.apiKeyService = apiKeyService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String header = request.getHeader("X-API-Key");
    if (header != null && !header.isBlank()) {
      Optional<ApiKey> resolved = apiKeyService.resolve(header);
      if (resolved.isPresent()) {
        ApiKey key = resolved.get();
        var auth =
            new UsernamePasswordAuthenticationToken(
                "api-key:" + key.getId(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_PARTNER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        TenantContext.setTenant(key.getTenantId());
        TenantContext.setActor(auth.getName());
      }
    }
    chain.doFilter(request, response);
  }
}

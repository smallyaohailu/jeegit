package io.jeegit.openapi.apikey;

import io.jeegit.common.JeegitConstants;
import io.jeegit.common.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Resolves the active tenant (and, if an API key was supplied, the partner's actor id).
 *
 * <p>Order of preference:
 *
 * <ol>
 *   <li>{@code X-API-Key} — looked up against {@link ApiKeyService#resolve(String)}; on hit the
 *       tenant id from the key is adopted and the actor becomes {@code api-key:&lt;id&gt;}.
 *   <li>{@code X-Tenant-Id} — explicit tenant override.
 *   <li>Otherwise defaults stay in place ({@code default} / {@code system}).
 * </ol>
 *
 * Runs after Spring Security's filter so Basic-authenticated callers (which populate
 * TenantContext.actor in {@code SecurityConfig}) still win if no header was supplied.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class TenantResolverFilter extends OncePerRequestFilter {

  private final ApiKeyService apiKeyService;

  public TenantResolverFilter(ApiKeyService apiKeyService) {
    this.apiKeyService = apiKeyService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String apiKey = request.getHeader("X-API-Key");
    if (apiKey != null && !apiKey.isBlank()) {
      Optional<ApiKey> resolved = apiKeyService.resolve(apiKey);
      if (resolved.isPresent()) {
        ApiKey key = resolved.get();
        TenantContext.setTenant(key.getTenantId());
        TenantContext.setActor("api-key:" + key.getId());
        chain.doFilter(request, response);
        return;
      }
    }

    String explicitTenant = request.getHeader("X-Tenant-Id");
    if (explicitTenant != null && !explicitTenant.isBlank()) {
      TenantContext.setTenant(explicitTenant);
    } else {
      TenantContext.setTenant(JeegitConstants.DEFAULT_TENANT);
    }
    chain.doFilter(request, response);
  }
}

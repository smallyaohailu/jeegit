package io.jeegit.bootstrap;

import io.jeegit.common.JeegitConstants;
import io.jeegit.common.TenantContext;
import io.jeegit.openapi.apikey.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Security baseline.
 *
 * <ul>
 *   <li>Health, platform metadata, Swagger UI, OpenAPI spec, and every GET under {@code /api/v1}
 *       are publicly readable so partners can discover the API.
 *   <li>Every write (POST/PUT/PATCH/DELETE) requires HTTP Basic authentication.
 *   <li>The built-in {@code admin} user is configured through {@code jeegit.security.admin-*}
 *       properties; a random password is generated in tests when not configured.
 * </ul>
 */
@Configuration
public class SecurityConfig {

  @Value("${jeegit.security.admin-username:admin}")
  private String adminUsername;

  @Value("${jeegit.security.admin-password:admin}")
  private String adminPassword;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, ApiKeyService apiKeyService)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/",
                        "/index.html",
                        "/css/**",
                        "/js/**",
                        "/i18n/**",
                        "/assets/**",
                        "/favicon.ico",
                        "/actuator/health",
                        "/actuator/info",
                        "/actuator/metrics",
                        "/actuator/prometheus",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/webjars/**")
                    .permitAll()
                    .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/**")
                    .permitAll()
                    .requestMatchers("/api/v1/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll())
        .httpBasic(org.springframework.security.config.Customizer.withDefaults())
        .addFilterBefore(
            new ApiKeyAuthenticationFilter(apiKeyService),
            org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
                .class)
        .addFilterBefore(
            tenantContextFilter(),
            org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
                .class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder encoder) {
    return new InMemoryUserDetailsManager(
        User.withUsername(adminUsername)
            .password(encoder.encode(adminPassword))
            .roles("ADMIN")
            .build());
  }

  /**
   * Propagates the authenticated username into {@link TenantContext} so audit fields reflect it.
   */
  @Bean
  public OncePerRequestFilter tenantContextFilter() {
    return new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws ServletException, IOException {
        try {
          String actor = JeegitConstants.SYSTEM_ACTOR;
          var auth =
              org.springframework.security.core.context.SecurityContextHolder.getContext()
                  .getAuthentication();
          if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            String name = auth.getName();
            if (!"anonymousUser".equals(name)) {
              actor = name;
            }
          }
          TenantContext.setActor(actor);
          chain.doFilter(request, response);
        } finally {
          TenantContext.clear();
        }
      }
    };
  }
}

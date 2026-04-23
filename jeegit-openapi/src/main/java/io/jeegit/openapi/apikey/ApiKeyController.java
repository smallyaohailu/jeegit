package io.jeegit.openapi.apikey;

import io.jeegit.common.ApiResponse;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

/**
 * Management endpoints for API keys.
 *
 * <p>Requires the {@code ADMIN} role — partner-facing applications authenticate themselves via the
 * {@code X-API-Key} header instead of calling these endpoints.
 */
@RestController
@RequestMapping("/api/v1/openapi/keys")
public class ApiKeyController {

  private final ApiKeyService service;
  private final ApiKeyRepository repository;

  public ApiKeyController(ApiKeyService service, ApiKeyRepository repository) {
    this.service = service;
    this.repository = repository;
  }

  @PostMapping
  public ApiResponse<Map<String, Object>> issue(@RequestBody Map<String, Object> body) {
    String name = (String) body.getOrDefault("name", "unnamed");
    String owner = (String) body.getOrDefault("owner", "");
    return ApiResponse.ok(service.issue(name, owner));
  }

  @GetMapping
  public ApiResponse<List<ApiKey>> list() {
    return ApiResponse.ok(repository.findAll());
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Map<String, Object>> revoke(@PathVariable String id) {
    service.revoke(id);
    return ApiResponse.ok(Map.of("id", id, "status", "revoked"));
  }
}

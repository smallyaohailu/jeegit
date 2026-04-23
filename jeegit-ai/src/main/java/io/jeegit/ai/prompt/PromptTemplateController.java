package io.jeegit.ai.prompt;

import io.jeegit.common.ApiResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

/** Administrative REST surface for the prompt-template catalogue. */
@RestController
@RequestMapping("/api/v1/ai/prompts")
public class PromptTemplateController {

  private final PromptTemplateService service;

  public PromptTemplateController(PromptTemplateService service) {
    this.service = service;
  }

  @GetMapping
  public ApiResponse<List<PromptTemplate>> listAll() {
    return ApiResponse.ok(service.listAll());
  }

  @GetMapping("/{code}")
  public ApiResponse<List<PromptTemplate>> history(@PathVariable String code) {
    return ApiResponse.ok(service.history(code));
  }

  @GetMapping("/{code}/latest")
  public ApiResponse<Optional<PromptTemplate>> latest(@PathVariable String code) {
    return ApiResponse.ok(service.latestPublished(code));
  }

  @PostMapping("/{code}")
  public ApiResponse<PromptTemplate> create(
      @PathVariable String code, @RequestBody Map<String, Object> body) {
    String template = (String) body.get("template");
    String description = (String) body.getOrDefault("description", "");
    if (template == null || template.isBlank()) {
      throw new IllegalArgumentException("'template' is required");
    }
    return ApiResponse.ok(service.createNextVersion(code, template, description));
  }

  @PostMapping("/{id}/publish")
  public ApiResponse<PromptTemplate> publish(@PathVariable String id) {
    return ApiResponse.ok(service.publish(id));
  }
}

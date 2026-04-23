package io.jeegit.tech.api;

import io.jeegit.common.ApiResponse;
import io.jeegit.tech.org.Org;
import io.jeegit.tech.org.OrgService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orgs")
public class OrgController {

  private final OrgService orgService;

  public OrgController(OrgService orgService) {
    this.orgService = orgService;
  }

  @GetMapping
  public ApiResponse<List<Org>> list() {
    return ApiResponse.ok(orgService.listForCurrentTenant());
  }

  @GetMapping("/{id}")
  public ApiResponse<Org> get(@PathVariable String id) {
    return ApiResponse.ok(orgService.get(id));
  }

  @GetMapping("/{id}/children")
  public ApiResponse<List<Org>> children(@PathVariable String id) {
    return ApiResponse.ok(orgService.listChildren(id));
  }

  @GetMapping("/{id}/descendants")
  public ApiResponse<List<Org>> descendants(@PathVariable String id) {
    return ApiResponse.ok(orgService.listDescendants(id));
  }

  @PostMapping
  public ApiResponse<Org> create(@RequestBody Map<String, Object> body) {
    Org o = new Org();
    o.setCode((String) body.get("code"));
    o.setName((String) body.get("name"));
    String typeRaw = (String) body.getOrDefault("type", Org.Type.DEPARTMENT.name());
    o.setType(Org.Type.valueOf(typeRaw));
    return ApiResponse.ok(orgService.create(o, (String) body.get("parentId")));
  }
}

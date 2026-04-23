package io.jeegit.tech.api;

import io.jeegit.common.ApiResponse;
import io.jeegit.tech.dict.DictItem;
import io.jeegit.tech.dict.DictService;
import io.jeegit.tech.dict.DictType;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dicts")
public class DictController {

  private final DictService dictService;

  public DictController(DictService dictService) {
    this.dictService = dictService;
  }

  @GetMapping("/types")
  public ApiResponse<List<DictType>> listTypes() {
    return ApiResponse.ok(dictService.listTypes());
  }

  @GetMapping("/types/{code}/items")
  public ApiResponse<List<DictItem>> listItems(@PathVariable String code) {
    return ApiResponse.ok(dictService.listItems(code));
  }

  @PostMapping("/types/{code}/items")
  public ApiResponse<DictItem> upsertItem(@PathVariable String code, @RequestBody DictItem item) {
    item.setTypeCode(code);
    return ApiResponse.ok(dictService.upsertItem(item));
  }
}

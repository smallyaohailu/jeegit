package com.jeegit.common.core.base;

import com.jeegit.common.core.constant.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "分页查询参数")
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "页码", defaultValue = "1", example = "1")
    private Integer pageNum = CommonConstants.DEFAULT_PAGE_NUM;

    @Schema(description = "每页条数", defaultValue = "10", example = "10")
    private Integer pageSize = CommonConstants.DEFAULT_PAGE_SIZE;

    @Schema(description = "排序字段")
    private String orderByColumn;

    @Schema(description = "排序方向（asc/desc）", example = "asc")
    private String isAsc;
}

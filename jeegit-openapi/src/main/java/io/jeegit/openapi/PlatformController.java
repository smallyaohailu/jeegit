package io.jeegit.openapi;

import io.jeegit.common.ApiResponse;
import io.jeegit.common.JeegitConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 开放平台入口控制器。MVP 阶段仅暴露平台元信息 + 模块清单；
 * 后续演进为完整的 API Gateway / 开发者门户后端。
 */
@RestController
@RequestMapping("/api/v1/platform")
public class PlatformController {

    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> info() {
        Map<String, Object> info = Map.of(
                "name", JeegitConstants.PLATFORM_NAME,
                "version", JeegitConstants.PLATFORM_VERSION,
                "license", "Apache-2.0",
                "architecture", "1 AI 底座 + 4 中台 + 1 开放平台",
                "modules", List.of(
                        "jeegit-common",
                        "jeegit-tech",
                        "jeegit-data",
                        "jeegit-ai",
                        "jeegit-business",
                        "jeegit-app",
                        "jeegit-openapi",
                        "jeegit-agent-intake",
                        "jeegit-bootstrap"
                ),
                "charters", List.of(
                        "docs/PRODUCT_CHARTER.md",
                        "docs/ARCHITECTURE_CHARTER.md",
                        "docs/AI_GOVERNANCE.md",
                        "docs/MVP_SCOPE.md"
                )
        );
        return ApiResponse.ok(info);
    }
}

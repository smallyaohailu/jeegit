package io.jeegit.openapi;

import io.jeegit.common.ApiResponse;
import io.jeegit.common.JeegitConstants;
import io.jeegit.common.i18n.I18n;
import io.jeegit.common.i18n.SupportedLocales;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Open Platform entry controller. In the preview release it exposes platform metadata and the
 * supported-locale catalogue; it will grow into the developer portal backend (API key management,
 * resource catalogue, usage metering) in subsequent releases.
 */
@RestController
@RequestMapping("/api/v1/platform")
public class PlatformController {

  private final I18n i18n;

  public PlatformController(I18n i18n) {
    this.i18n = i18n;
  }

  @GetMapping("/info")
  public ApiResponse<Map<String, Object>> info() {
    Map<String, Object> info =
        Map.of(
            "name", JeegitConstants.PLATFORM_NAME,
            "version", JeegitConstants.PLATFORM_VERSION,
            "license", "Apache-2.0",
            "tagline", i18n.t("platform.tagline"),
            "architecture", i18n.t("platform.architecture"),
            "modules",
                List.of(
                    "jeegit-common",
                    "jeegit-tech",
                    "jeegit-data",
                    "jeegit-ai",
                    "jeegit-business",
                    "jeegit-app",
                    "jeegit-openapi",
                    "jeegit-agent-intake",
                    "jeegit-bootstrap"),
            "charters",
                List.of(
                    "docs/PRODUCT_CHARTER.md",
                    "docs/ARCHITECTURE_CHARTER.md",
                    "docs/AI_GOVERNANCE.md",
                    "docs/MVP_SCOPE.md",
                    "docs/i18n/LANGUAGES.md"));
    return ApiResponse.ok(info);
  }

  @GetMapping("/locales")
  public ApiResponse<List<Map<String, Object>>> locales() {
    List<Map<String, Object>> data =
        SupportedLocales.ALL.stream()
            .map(
                l ->
                    Map.<String, Object>of(
                        "tag", l.toLanguageTag(),
                        "displayNameNative", l.getDisplayName(l),
                        "displayNameEnglish", l.getDisplayName(Locale.ENGLISH),
                        "rtl", isRightToLeft(l),
                        "default", l.equals(SupportedLocales.DEFAULT)))
            .toList();
    return ApiResponse.ok(data);
  }

  private boolean isRightToLeft(Locale locale) {
    String lang = locale.getLanguage();
    return "ar".equalsIgnoreCase(lang)
        || "he".equalsIgnoreCase(lang)
        || "fa".equalsIgnoreCase(lang)
        || "ur".equalsIgnoreCase(lang);
  }
}

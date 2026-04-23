package io.jeegit.ai.prompt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PromptTemplateServiceTest {

  @Test
  void renderSubstitutesPlaceholders() {
    String rendered =
        PromptTemplateService.render(
            "Hello {{name}}, welcome to {{product}}.", Map.of("name", "Ada", "product", "jeegit"));
    assertThat(rendered).isEqualTo("Hello Ada, welcome to jeegit.");
  }

  @Test
  void renderIsNullSafe() {
    assertThat(PromptTemplateService.render(null, Map.of("a", "b"))).isNull();
    assertThat(PromptTemplateService.render("unchanged", Map.of())).isEqualTo("unchanged");
    Map<String, Object> withNull = new HashMap<>();
    withNull.put("x", "X");
    withNull.put("y", null);
    assertThat(PromptTemplateService.render("{{x}} and {{y}}", withNull)).isEqualTo("X and ");
  }
}

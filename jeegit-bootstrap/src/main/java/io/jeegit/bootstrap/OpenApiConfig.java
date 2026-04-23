package io.jeegit.bootstrap;

import io.jeegit.common.JeegitConstants;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** OpenAPI metadata used by Swagger UI at {@code /swagger-ui.html}. */
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI jeegitOpenApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("jeegit API")
                .version(JeegitConstants.PLATFORM_VERSION)
                .description(
                    "REST surface of the jeegit AI-Native application framework. "
                        + "All endpoints return a unified ApiResponse envelope; the "
                        + "meta.locale field always reports the negotiated locale.")
                .license(
                    new License()
                        .name("Apache-2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")))
        .externalDocs(
            new ExternalDocumentation()
                .description("Project documentation")
                .url("https://github.com/smallyaohailu/jeegit"));
  }
}

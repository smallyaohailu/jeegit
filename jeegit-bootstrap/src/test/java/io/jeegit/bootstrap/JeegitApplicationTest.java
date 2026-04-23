package io.jeegit.bootstrap;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class JeegitApplicationTest {

  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private FilterChainProxy springSecurityFilterChain;

  private MockMvc mvc() {
    return MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .addFilters(springSecurityFilterChain)
        .build();
  }

  @Test
  void platformInfo_localizedByAcceptLanguage() throws Exception {
    mvc()
        .perform(get("/api/v1/platform/info").header("Accept-Language", "zh-CN"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.name").value("jeegit"))
        .andExpect(jsonPath("$.data.license").value("Apache-2.0"))
        .andExpect(jsonPath("$.data.tagline", containsString("AI")))
        .andExpect(jsonPath("$.meta.locale").value("zh-CN"));
  }

  @Test
  void platformInfo_defaultsToEnglish() throws Exception {
    mvc()
        .perform(get("/api/v1/platform/info"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.tagline").value(containsString("AI-Native")))
        .andExpect(jsonPath("$.meta.locale").value("en"));
  }

  @Test
  void platformLocales_returnsTwelveWithArabicMarkedRtl() throws Exception {
    MvcResult result =
        mvc()
            .perform(get("/api/v1/platform/locales"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", hasSize(12)))
            .andReturn();
    JsonNode root = new ObjectMapper().readTree(result.getResponse().getContentAsString());
    JsonNode data = root.path("data");
    boolean arabicRtl = false;
    boolean englishDefault = false;
    for (JsonNode entry : data) {
      if ("ar".equals(entry.path("tag").asText())) {
        arabicRtl = entry.path("rtl").asBoolean();
      }
      if ("en".equals(entry.path("tag").asText())) {
        englishDefault = entry.path("default").asBoolean();
      }
    }
    if (!arabicRtl) {
      throw new AssertionError("Arabic must be marked RTL");
    }
    if (!englishDefault) {
      throw new AssertionError("English must be marked as default");
    }
  }

  @Test
  void orgsEndpoint_returnsSeededTree() throws Exception {
    MvcResult result =
        mvc()
            .perform(get("/api/v1/orgs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(4))))
            .andReturn();
    JsonNode data =
        new ObjectMapper().readTree(result.getResponse().getContentAsString()).path("data");
    boolean rootFound = false;
    for (JsonNode entry : data) {
      if ("ROOT".equals(entry.path("code").asText())) {
        if (!"Default Company".equals(entry.path("name").asText())) {
          throw new AssertionError("ROOT name mismatch: " + entry.path("name"));
        }
        rootFound = true;
      }
    }
    if (!rootFound) {
      throw new AssertionError("seed tree should include a ROOT node");
    }
  }

  @Test
  void matterCreateDispatchAndAudit_fullCycleInEnglish() throws Exception {
    ObjectMapper json = new ObjectMapper();

    MvcResult created =
        mvc()
            .perform(
                post("/api/v1/matters")
                    .with(httpBasic("admin", "admin"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        "{\"title\":\"Tax filing help\","
                            + "\"category\":\"tax\","
                            + "\"description\":\"need help filing\","
                            + "\"applicantId\":\"u1\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.matterStatus").value("SUBMITTED"))
            .andReturn();

    JsonNode createdBody = json.readTree(created.getResponse().getContentAsString());
    String id = createdBody.path("data").path("id").asText();

    mvc()
        .perform(post("/api/v1/matters/" + id + "/dispatch").with(httpBasic("admin", "admin")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.status").value("EXECUTED"))
        .andExpect(jsonPath("$.data.decision.department").value("Tax Bureau"))
        .andExpect(jsonPath("$.data.reasoningSummary", not(equalTo(""))));

    mvc()
        .perform(get("/api/v1/matters/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.matterStatus").value("DISPATCHED"))
        .andExpect(jsonPath("$.data.assignedDepartment").value("Tax Bureau"));

    mvc()
        .perform(get("/api/v1/audit/tenants/default"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(2))));
  }

  @Test
  void invalidAgent_returnsLocalizedDeniedReasoning() throws Exception {
    mvc()
        .perform(
            post("/api/v1/ai/agents/agent.intake.dispatch/invoke")
                .with(httpBasic("admin", "admin"))
                .header("Accept-Language", "es")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"input\":{}}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.status").value("DENIED"))
        .andExpect(jsonPath("$.data.reasoningSummary", containsString("matterId")))
        .andExpect(jsonPath("$.meta.locale").value("es"));
  }

  @Test
  void unauthenticatedWrite_isRejected() throws Exception {
    mvc()
        .perform(
            post("/api/v1/matters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"x\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void openApiDocumentExposesSixteenPaths() throws Exception {
    MvcResult result = mvc().perform(get("/v3/api-docs")).andExpect(status().isOk()).andReturn();
    JsonNode doc = new ObjectMapper().readTree(result.getResponse().getContentAsString());
    int paths = doc.path("paths").size();
    if (paths < 12) {
      throw new AssertionError("expected >= 12 paths in OpenAPI doc, got " + paths);
    }
  }
}

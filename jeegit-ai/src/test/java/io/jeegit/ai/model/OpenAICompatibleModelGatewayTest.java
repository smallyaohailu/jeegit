package io.jeegit.ai.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.sun.net.httpserver.HttpServer;
import io.jeegit.tech.audit.AuditLog;
import io.jeegit.tech.audit.AuditService;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class OpenAICompatibleModelGatewayTest {

  private HttpServer server;
  private int port;
  private AtomicReference<String> capturedBody = new AtomicReference<>();
  private AtomicReference<String> capturedAuth = new AtomicReference<>();
  private AtomicReference<String> capturedTenantHeader = new AtomicReference<>();

  @BeforeEach
  void startStubServer() throws Exception {
    server = HttpServer.create(new InetSocketAddress(0), 0);
    server.createContext(
        "/v1/chat/completions",
        exchange -> {
          byte[] body = exchange.getRequestBody().readAllBytes();
          capturedBody.set(new String(body, StandardCharsets.UTF_8));
          capturedAuth.set(exchange.getRequestHeaders().getFirst("Authorization"));
          capturedTenantHeader.set(exchange.getRequestHeaders().getFirst("X-Jeegit-Tenant"));
          String response =
              "{\"id\":\"chatcmpl-test\",\"choices\":[{\"message\":{\"role\":\"assistant\","
                  + "\"content\":\"hello from stub\"}}],"
                  + "\"usage\":{\"prompt_tokens\":12,\"completion_tokens\":5,\"total_tokens\":17}}";
          byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
          exchange.getResponseHeaders().add("Content-Type", "application/json");
          exchange.sendResponseHeaders(200, bytes.length);
          exchange.getResponseBody().write(bytes);
          exchange.close();
        });
    server.start();
    port = server.getAddress().getPort();
  }

  @AfterEach
  void stopStubServer() {
    if (server != null) server.stop(0);
  }

  @Test
  void sendsChatCompletionRequestAndReturnsContent() {
    AuditService audit = Mockito.mock(AuditService.class);
    OpenAICompatibleModelGateway gateway =
        new OpenAICompatibleModelGateway(
            audit, "http://127.0.0.1:" + port, "sk-test", "gpt-4o-mini", Duration.ofSeconds(10));

    ModelResponse response =
        gateway.invoke(ModelRequest.of("tenant-xyz", "gpt-4o-mini", "why is the sky blue?"));

    assertThat(response.content()).isEqualTo("hello from stub");
    assertThat(response.tokenIn()).isEqualTo(12);
    assertThat(response.tokenOut()).isEqualTo(5);
    assertThat(capturedAuth.get()).isEqualTo("Bearer sk-test");
    assertThat(capturedTenantHeader.get()).isEqualTo("tenant-xyz");
    assertThat(capturedBody.get()).contains("\"model\":\"gpt-4o-mini\"");
    assertThat(capturedBody.get()).contains("why is the sky blue?");

    ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
    Mockito.verify(audit).record(captor.capture());
    AuditLog written = captor.getValue();
    assertThat(written.getAction()).isEqualTo("MODEL_CALL");
    assertThat(written.getDecision()).isEqualTo("ALLOW");
    assertThat(written.getTokenIn()).isEqualTo(12);
    assertThat(written.getTokenOut()).isEqualTo(5);
  }
}

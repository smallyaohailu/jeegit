package io.jeegit.ai.tool.notify;

import io.jeegit.ai.tool.Tool;
import io.jeegit.common.TenantContext;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@code notify.send} tool — the preview implementation logs the notification. Production
 * deployments swap this with an email / SMS / webhook sender by registering a {@link Tool} bean
 * with the same name that overrides this one.
 */
@Component
public class LoggingNotifyTool implements Tool {

  private static final Logger log = LoggerFactory.getLogger(LoggingNotifyTool.class);

  @Override
  public String name() {
    return "notify.send";
  }

  @Override
  public String description() {
    return "Send a notification to a user (preview implementation just logs the payload).";
  }

  @Override
  public String riskLevel() {
    return "LOW";
  }

  @Override
  public Map<String, Object> execute(Map<String, Object> params) {
    String recipient = String.valueOf(params.getOrDefault("recipient", ""));
    String subject = String.valueOf(params.getOrDefault("subject", ""));
    String body = String.valueOf(params.getOrDefault("body", ""));
    String tenantId = String.valueOf(params.getOrDefault("tenantId", TenantContext.tenant()));
    log.info(
        "notify.send tenant={} recipient={} subject={} bodyLength={}",
        tenantId,
        recipient,
        subject,
        body.length());

    Map<String, Object> out = new HashMap<>();
    out.put("delivered", true);
    out.put("channel", "log");
    out.put("recipient", recipient);
    out.put("timestamp", Instant.now().toString());
    return out;
  }
}

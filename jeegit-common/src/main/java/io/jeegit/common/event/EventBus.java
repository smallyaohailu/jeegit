package io.jeegit.common.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Lightweight in-process event bus. Not a replacement for a real broker — it exists so modules can
 * publish {@link DomainEvent}s through a stable API that we can later back with Kafka / RocketMQ /
 * Solace / whatever the target deployment prefers.
 *
 * <p>Subscribers register with {@link #subscribe(String, Consumer)} (topic supports a trailing
 * {@code .*} wildcard) and are invoked synchronously after {@link #publish(DomainEvent)}.
 */
@Component
public class EventBus {

  private static final Logger log = LoggerFactory.getLogger(EventBus.class);

  private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();

  public void publish(DomainEvent event) {
    log.debug("publish {} tenant={} id={}", event.topic(), event.tenantId(), event.id());
    for (Subscription s : subscriptions) {
      if (s.matches(event.topic())) {
        try {
          s.handler().accept(event);
        } catch (RuntimeException ex) {
          log.warn("subscriber {} failed on {}", s.pattern(), event.topic(), ex);
        }
      }
    }
  }

  public void subscribe(String pattern, Consumer<DomainEvent> handler) {
    subscriptions.add(new Subscription(pattern, handler));
  }

  private record Subscription(String pattern, Consumer<DomainEvent> handler) {
    boolean matches(String topic) {
      if (pattern.equals(topic)) return true;
      if (pattern.endsWith(".*")) {
        String prefix = pattern.substring(0, pattern.length() - 1);
        return topic.startsWith(prefix);
      }
      return false;
    }
  }
}

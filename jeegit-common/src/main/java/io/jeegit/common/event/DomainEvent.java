package io.jeegit.common.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Base class for domain events.
 *
 * <p>Convention: the {@code topic} follows {@code jeegit.&lt;domain&gt;.&lt;event&gt;.v1} to mirror
 * the AsyncAPI naming described in the Architecture Charter.
 */
public record DomainEvent(
    String id, String topic, String tenantId, Instant occurredAt, Map<String, Object> payload) {

  public static DomainEvent of(String topic, String tenantId, Map<String, Object> payload) {
    return new DomainEvent(
        UUID.randomUUID().toString(), topic, tenantId, Instant.now(), Map.copyOf(payload));
  }
}

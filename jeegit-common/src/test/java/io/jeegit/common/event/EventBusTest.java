package io.jeegit.common.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EventBusTest {

  @Test
  void subscriberReceivesExactTopicOnly() {
    EventBus bus = new EventBus();
    List<DomainEvent> received = new ArrayList<>();
    bus.subscribe("jeegit.matter.dispatched.v1", received::add);
    bus.publish(DomainEvent.of("jeegit.matter.submitted.v1", "t", Map.of("a", 1)));
    bus.publish(DomainEvent.of("jeegit.matter.dispatched.v1", "t", Map.of("a", 2)));
    assertThat(received).hasSize(1);
    assertThat(received.get(0).topic()).isEqualTo("jeegit.matter.dispatched.v1");
    assertThat(received.get(0).payload()).containsEntry("a", 2);
  }

  @Test
  void wildcardMatchesSiblingTopics() {
    EventBus bus = new EventBus();
    List<String> topics = new ArrayList<>();
    bus.subscribe("jeegit.matter.*", ev -> topics.add(ev.topic()));
    bus.publish(DomainEvent.of("jeegit.matter.submitted.v1", "t", Map.of()));
    bus.publish(DomainEvent.of("jeegit.matter.dispatched.v1", "t", Map.of()));
    bus.publish(DomainEvent.of("jeegit.audit.recorded.v1", "t", Map.of()));
    assertThat(topics).containsExactly("jeegit.matter.submitted.v1", "jeegit.matter.dispatched.v1");
  }

  @Test
  void subscriberFailureDoesNotStopOthers() {
    EventBus bus = new EventBus();
    List<String> received = new ArrayList<>();
    bus.subscribe(
        "jeegit.matter.dispatched.v1",
        ev -> {
          throw new RuntimeException("boom");
        });
    bus.subscribe("jeegit.matter.dispatched.v1", ev -> received.add(ev.id()));
    bus.publish(DomainEvent.of("jeegit.matter.dispatched.v1", "t", Map.of()));
    assertThat(received).hasSize(1);
  }
}

package io.jeegit.ai.tool;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Registry of every {@link Tool} Spring discovers on the classpath. Agents must look up their tools
 * here so invocations consistently go through the single choke point where we can validate
 * parameters and write audit entries.
 */
@Component
public class ToolRegistry {

  private final Map<String, Tool> tools = new ConcurrentHashMap<>();

  public ToolRegistry(List<Tool> discovered) {
    for (Tool t : discovered) {
      tools.put(t.name(), t);
    }
  }

  public Optional<Tool> find(String name) {
    return Optional.ofNullable(tools.get(name));
  }

  public List<Tool> list() {
    return List.copyOf(tools.values());
  }

  public void register(Tool tool) {
    tools.put(tool.name(), tool);
  }
}

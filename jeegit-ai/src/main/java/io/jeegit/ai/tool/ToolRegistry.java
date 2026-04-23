package io.jeegit.ai.tool;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工具注册中心。Spring 扫描到的所有 Tool 自动注册。
 * Agent 调用工具必须经过此处，以便统一校验与审计。
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

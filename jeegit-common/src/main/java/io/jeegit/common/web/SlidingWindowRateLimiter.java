package io.jeegit.common.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Token-bucket-style rate limiter backed by monotonic wall-clock windows.
 *
 * <p>The implementation is intentionally simple: each {@code key} owns a sliding window of {@link
 * #windowMillis} milliseconds; counters are reset the first time a request comes in after the
 * window rolls over. This is suitable for per-tenant / per-API-key fairness; cluster-wide limits
 * should move to Redis.
 */
public class SlidingWindowRateLimiter {

  private final int permitsPerWindow;
  private final long windowMillis;
  private final Map<String, Window> windows = new ConcurrentHashMap<>();

  public SlidingWindowRateLimiter(int permitsPerWindow, long windowMillis) {
    this.permitsPerWindow = permitsPerWindow;
    this.windowMillis = windowMillis;
  }

  public boolean tryAcquire(String key) {
    long now = System.currentTimeMillis();
    Window window = windows.computeIfAbsent(key, k -> new Window(now));
    synchronized (window) {
      if (now - window.startedAt >= windowMillis) {
        window.startedAt = now;
        window.counter.reset();
      }
      if (window.counter.sum() >= permitsPerWindow) {
        return false;
      }
      window.counter.increment();
      return true;
    }
  }

  public int permitsPerWindow() {
    return permitsPerWindow;
  }

  public long windowMillis() {
    return windowMillis;
  }

  private static final class Window {
    long startedAt;
    final LongAdder counter = new LongAdder();

    Window(long startedAt) {
      this.startedAt = startedAt;
    }
  }
}

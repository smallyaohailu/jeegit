package io.jeegit.common.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SlidingWindowRateLimiterTest {

  @Test
  void allowsUpToPermitsPerWindow() {
    SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(3, 60_000);
    assertThat(limiter.tryAcquire("default")).isTrue();
    assertThat(limiter.tryAcquire("default")).isTrue();
    assertThat(limiter.tryAcquire("default")).isTrue();
    assertThat(limiter.tryAcquire("default")).isFalse();
  }

  @Test
  void otherKeysAreIndependent() {
    SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(1, 60_000);
    assertThat(limiter.tryAcquire("tenant-a")).isTrue();
    assertThat(limiter.tryAcquire("tenant-a")).isFalse();
    assertThat(limiter.tryAcquire("tenant-b")).isTrue();
  }

  @Test
  void windowResetsAfterExpiry() throws InterruptedException {
    SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(1, 50);
    assertThat(limiter.tryAcquire("x")).isTrue();
    assertThat(limiter.tryAcquire("x")).isFalse();
    Thread.sleep(60);
    assertThat(limiter.tryAcquire("x")).isTrue();
  }
}

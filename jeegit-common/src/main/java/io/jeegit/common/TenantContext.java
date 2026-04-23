package io.jeegit.common;

/**
 * Per-request tenant and actor context.
 *
 * <p>Every aggregate root in the platform is tenant-aware (see {@code TenantAwareEntity}); service
 * layers are expected to read the active tenant from this holder and populate new records
 * accordingly. The {@code actor} value is fed into Spring Data JPA auditing to fill
 * {@code @CreatedBy} / {@code @LastModifiedBy}.
 */
public final class TenantContext {

  private static final ThreadLocal<String> TENANT =
      ThreadLocal.withInitial(() -> JeegitConstants.DEFAULT_TENANT);
  private static final ThreadLocal<String> ACTOR =
      ThreadLocal.withInitial(() -> JeegitConstants.SYSTEM_ACTOR);

  public static String tenant() {
    return TENANT.get();
  }

  public static void setTenant(String tenant) {
    TENANT.set(tenant == null ? JeegitConstants.DEFAULT_TENANT : tenant);
  }

  public static String actor() {
    return ACTOR.get();
  }

  public static void setActor(String actor) {
    ACTOR.set(actor == null ? JeegitConstants.SYSTEM_ACTOR : actor);
  }

  public static void clear() {
    TENANT.remove();
    ACTOR.remove();
  }

  private TenantContext() {}
}

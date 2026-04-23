package io.jeegit.common;

/**
 * 请求级租户/主体上下文。
 * 遵循架构宪章：所有聚合根必须携带租户 ID，由服务层从本上下文透传。
 */
public final class TenantContext {

    private static final ThreadLocal<String> TENANT = ThreadLocal.withInitial(() -> JeegitConstants.DEFAULT_TENANT);
    private static final ThreadLocal<String> ACTOR = ThreadLocal.withInitial(() -> JeegitConstants.SYSTEM_ACTOR);

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

    private TenantContext() {
    }
}

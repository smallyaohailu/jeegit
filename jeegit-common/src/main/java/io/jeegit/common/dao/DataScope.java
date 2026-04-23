package io.jeegit.common.dao;

/**
 * Declarative data-access scope attached to a {@code Role}.
 *
 * <p>The scope is turned into a JPA {@code Specification} by a
 * framework-provided factory so every business query composes the same
 * portable Criteria — no hand-written SQL fragments.</p>
 */
public enum DataScope {
    /** All data, no filtering. */
    ALL,
    /** Data belonging to the user's owning company (top-level organization). */
    COMPANY,
    /** The user's company and every descendant organization. */
    COMPANY_AND_CHILD,
    /** Data belonging to the user's immediate department. */
    DEPARTMENT,
    /** The user's department and every descendant department. */
    DEPARTMENT_AND_CHILD,
    /** Data created by the user. */
    SELF,
    /** Custom — governed by an explicit list of organization IDs on the role. */
    CUSTOM
}

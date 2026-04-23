package io.jeegit.common.dao;

/**
 * Helpers for the materialized-path convention used by {@link TreeEntity}.
 * Descendant lookup query: {@code WHERE parent_ids LIKE '%,{nodeId},%'}.
 */
public final class TreePaths {

    private static final String SEP = ",";

    private TreePaths() {
    }

    /** {@code parentIds} value for a root node: a single separator. */
    public static String rootPath() {
        return SEP;
    }

    /** Build the {@code parentIds} value for a child given the parent's path and id. */
    public static String childPath(String parentParentIds, String parentId) {
        if (parentId == null || parentId.isBlank()) {
            return rootPath();
        }
        String prefix = (parentParentIds == null || parentParentIds.isBlank()) ? SEP : parentParentIds;
        if (!prefix.endsWith(SEP)) {
            prefix = prefix + SEP;
        }
        return prefix + parentId + SEP;
    }

    /** {@code LIKE} pattern that matches every descendant of {@code nodeId}. */
    public static String descendantLikePattern(String nodeId) {
        return "%" + SEP + nodeId + SEP + "%";
    }
}

package io.jeegit.common.dao;

/**
 * 树结构路径工具。维护 {@link TreeEntity#getParentIds()} 所需的 "逗号包裹" 路径。
 * 查询所有后代： {@code WHERE parent_ids LIKE '%,{nodeId},%'}。
 */
public final class TreePaths {

    private static final String SEP = ",";

    private TreePaths() {
    }

    /** 根节点的 parentIds 值：仅一个前置分隔符。 */
    public static String rootPath() {
        return SEP;
    }

    /** 基于父节点的 parentIds 与父节点自身 ID，构造子节点的 parentIds。 */
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

    /** 用于 JPQL/SQL LIKE 的 descendant 过滤模式，例如 "%,abc,%"。 */
    public static String descendantLikePattern(String nodeId) {
        return "%" + SEP + nodeId + SEP + "%";
    }
}

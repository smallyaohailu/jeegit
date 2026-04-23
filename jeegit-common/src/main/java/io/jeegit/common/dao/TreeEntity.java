package io.jeegit.common.dao;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * 树形实体基类。借鉴 jeesite 的"物化路径"思路：
 * <ul>
 *   <li>{@code parentId} 直接上级 ID（根节点为空）</li>
 *   <li>{@code parentIds} 从根到父的完整 ID 路径，形如 {@code ",root,a,b,"}，
 *       两端及分隔处均加逗号，便于 {@code LIKE} 查询所有后代</li>
 *   <li>{@code treeLevel} 节点深度（根为 0）</li>
 *   <li>{@code treeSort} 同层显示排序</li>
 *   <li>{@code treeLeaf} 是否叶子（便于 UI 折叠/展开判断）</li>
 * </ul>
 * 子类只需新增 {@code code/name} 等业务字段；路径维护逻辑统一在服务层 util 中提供。
 */
@MappedSuperclass
public abstract class TreeEntity extends TenantAwareEntity {

    @Column(name = "parent_id", length = 64)
    protected String parentId;

    @Column(name = "parent_ids", length = 2000)
    protected String parentIds;

    @Column(name = "tree_level", nullable = false)
    protected int treeLevel;

    @Column(name = "tree_sort", nullable = false)
    protected int treeSort = 30;

    @Column(name = "tree_leaf", nullable = false)
    protected boolean treeLeaf = true;

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public String getParentIds() { return parentIds; }
    public void setParentIds(String parentIds) { this.parentIds = parentIds; }

    public int getTreeLevel() { return treeLevel; }
    public void setTreeLevel(int treeLevel) { this.treeLevel = treeLevel; }

    public int getTreeSort() { return treeSort; }
    public void setTreeSort(int treeSort) { this.treeSort = treeSort; }

    public boolean isTreeLeaf() { return treeLeaf; }
    public void setTreeLeaf(boolean treeLeaf) { this.treeLeaf = treeLeaf; }
}

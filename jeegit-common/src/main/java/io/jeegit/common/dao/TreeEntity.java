package io.jeegit.common.dao;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * Base class for tree-shaped entities, using a <em>materialized path</em>:
 * <ul>
 *   <li>{@code parentId} — immediate parent (null for the root)</li>
 *   <li>{@code parentIds} — comma-surrounded path from root to parent,
 *       e.g. {@code ",root,a,b,"} — enables descendant queries via
 *       {@code LIKE '%,x,%'}</li>
 *   <li>{@code treeLevel} — depth, 0 for the root</li>
 *   <li>{@code treeSort} — display sort within the same parent</li>
 *   <li>{@code treeLeaf} — convenience flag for UI fold/unfold</li>
 * </ul>
 * Subclasses only need to add their own {@code code}/{@code name} columns.
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

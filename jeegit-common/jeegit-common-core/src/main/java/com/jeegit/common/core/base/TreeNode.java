package com.jeegit.common.core.base;

import java.util.List;

public interface TreeNode {

    Long getId();

    Long getParentId();

    List<? extends TreeNode> getChildren();

    @SuppressWarnings("rawtypes")
    void setChildren(List children);
}

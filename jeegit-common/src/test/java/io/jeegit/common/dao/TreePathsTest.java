package io.jeegit.common.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TreePathsTest {

  @Test
  void rootPath_isSingleSeparator() {
    assertThat(TreePaths.rootPath()).isEqualTo(",");
  }

  @Test
  void childPath_rootParent() {
    String path = TreePaths.childPath(TreePaths.rootPath(), "root-id");
    assertThat(path).isEqualTo(",root-id,");
  }

  @Test
  void childPath_nestedParent() {
    String path = TreePaths.childPath(",root,", "tax");
    assertThat(path).isEqualTo(",root,tax,");
  }

  @Test
  void childPath_handlesMissingTrailingSeparator() {
    String path = TreePaths.childPath(",root", "tax");
    assertThat(path).isEqualTo(",root,tax,");
  }

  @Test
  void childPath_nullParentId_returnsRoot() {
    assertThat(TreePaths.childPath(",root,", null)).isEqualTo(",");
  }

  @Test
  void descendantLikePattern_wraps() {
    assertThat(TreePaths.descendantLikePattern("node-1")).isEqualTo("%,node-1,%");
  }
}

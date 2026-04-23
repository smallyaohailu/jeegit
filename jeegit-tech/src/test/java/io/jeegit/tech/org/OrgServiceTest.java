package io.jeegit.tech.org;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jeegit.common.TenantContext;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@DataJpaTest
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "io.jeegit.tech.org")
@EntityScan(basePackages = "io.jeegit")
@Import({OrgService.class})
class OrgServiceTest {

  @Autowired private OrgService service;
  @Autowired private OrgRepository repository;

  @BeforeEach
  void setup() {
    TenantContext.setTenant("t-org-test");
    TenantContext.setActor("tester");
  }

  @AfterEach
  void cleanup() {
    TenantContext.clear();
  }

  @Test
  void buildsTreeWithMaterializedPath() {
    Org root = service.create(new Org("ROOT", "Root Co", Org.Type.COMPANY), null);
    Org dept = service.create(new Org("TAX", "Tax Dept", Org.Type.DEPARTMENT), root.getId());
    Org team = service.create(new Org("COLL", "Collections", Org.Type.TEAM), dept.getId());

    assertThat(root.getParentIds()).isEqualTo(",");
    assertThat(root.getTreeLevel()).isEqualTo(0);
    assertThat(dept.getTreeLevel()).isEqualTo(1);
    assertThat(dept.getParentIds()).isEqualTo("," + root.getId() + ",");
    assertThat(team.getTreeLevel()).isEqualTo(2);
    assertThat(team.getParentIds()).isEqualTo("," + root.getId() + "," + dept.getId() + ",");

    // parent becomes non-leaf after a child is added
    Org parentReloaded = repository.findById(root.getId()).orElseThrow();
    assertThat(parentReloaded.isTreeLeaf()).isFalse();
  }

  @Test
  void descendantsUseLikeQuery() {
    Org root = service.create(new Org("ROOT", "Root Co", Org.Type.COMPANY), null);
    Org t = service.create(new Org("TAX", "Tax", Org.Type.DEPARTMENT), root.getId());
    service.create(new Org("COLL", "Collections", Org.Type.TEAM), t.getId());
    service.create(new Org("AUDIT", "Audit", Org.Type.TEAM), t.getId());

    List<Org> desc = service.listDescendants(root.getId());
    assertThat(desc).extracting(Org::getCode).containsExactlyInAnyOrder("TAX", "COLL", "AUDIT");
  }

  @Test
  void rejectsMissingParent() {
    assertThatThrownBy(
            () -> service.create(new Org("X", "x", Org.Type.DEPARTMENT), "missing-parent"))
        .isInstanceOf(IllegalArgumentException.class);
  }
}

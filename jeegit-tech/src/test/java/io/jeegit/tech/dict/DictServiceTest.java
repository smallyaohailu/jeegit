package io.jeegit.tech.dict;

import static org.assertj.core.api.Assertions.assertThat;

import io.jeegit.common.TenantContext;
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
@EnableJpaRepositories(basePackages = "io.jeegit.tech.dict")
@EntityScan(basePackages = "io.jeegit")
@Import({DictService.class})
class DictServiceTest {

  @Autowired private DictService service;

  @BeforeEach
  void setup() {
    TenantContext.setTenant("t-dict");
    TenantContext.setActor("tester");
  }

  @AfterEach
  void cleanup() {
    TenantContext.clear();
  }

  @Test
  void upsertIsIdempotentByTenantAndCode() {
    DictType v1 = service.upsertType(new DictType("ROUTE", "Routes", "first", true));
    DictType v2 = service.upsertType(new DictType("ROUTE", "Routes v2", "second", true));
    assertThat(v1.getId()).isEqualTo(v2.getId());
    assertThat(v2.getName()).isEqualTo("Routes v2");
    assertThat(service.listTypes()).hasSize(1);
  }

  @Test
  void itemsAreReturnedSortedBySortOrder() {
    service.upsertType(new DictType("ROUTE", "Routes", null, true));
    service.upsertItem(new DictItem("ROUTE", "B", "B", "b,b", 20, null));
    service.upsertItem(new DictItem("ROUTE", "A", "A", "a,a", 10, null));
    service.upsertItem(new DictItem("ROUTE", "C", "C", "c,c", 30, null));
    assertThat(service.listItems("ROUTE"))
        .extracting(DictItem::getItemKey)
        .containsExactly("A", "B", "C");
  }
}

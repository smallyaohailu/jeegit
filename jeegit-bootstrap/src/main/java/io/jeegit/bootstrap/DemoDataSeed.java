package io.jeegit.bootstrap;

import io.jeegit.common.JeegitConstants;
import io.jeegit.common.TenantContext;
import io.jeegit.common.dao.DataScope;
import io.jeegit.tech.dict.DictItem;
import io.jeegit.tech.dict.DictService;
import io.jeegit.tech.dict.DictType;
import io.jeegit.tech.iam.Role;
import io.jeegit.tech.iam.RoleRepository;
import io.jeegit.tech.iam.User;
import io.jeegit.tech.iam.UserRepository;
import io.jeegit.tech.org.Org;
import io.jeegit.tech.org.OrgService;
import io.jeegit.tech.tenant.Tenant;
import io.jeegit.tech.tenant.TenantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Demo seed data — makes sure the reference flows in the README work on a
 * fresh install. Production deployments should use externalized migrations
 * (Flyway / Liquibase) rather than this runner.
 */
@Component
public class DemoDataSeed implements CommandLineRunner {

    private final TenantRepository tenantRepository;
    private final OrgService orgService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DictService dictService;

    public DemoDataSeed(TenantRepository tenantRepository,
                        OrgService orgService,
                        UserRepository userRepository,
                        RoleRepository roleRepository,
                        DictService dictService) {
        this.tenantRepository = tenantRepository;
        this.orgService = orgService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.dictService = dictService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        String tenantCode = JeegitConstants.DEFAULT_TENANT;
        TenantContext.setActor("system");
        TenantContext.setTenant(tenantCode);
        try {
            ensureTenant(tenantCode);
            Org root = ensureOrgTree();
            ensureAdmin(root.getId());
            ensureDefaultRoles();
            ensureDispatchRuleDict();
        } finally {
            TenantContext.clear();
        }
    }

    private void ensureTenant(String tenantCode) {
        if (tenantRepository.findByCode(tenantCode).isEmpty()) {
            Tenant tenant = new Tenant();
            tenant.setCode(tenantCode);
            tenant.setName("Default tenant");
            tenant.setDescription("Built-in default tenant used for demos and initial acceptance.");
            tenantRepository.save(tenant);
        }
    }

    private Org ensureOrgTree() {
        return orgService.listForCurrentTenant().stream()
                .filter(o -> o.getTreeLevel() == 0)
                .findFirst()
                .orElseGet(() -> {
                    Org root = new Org("ROOT", "Default Company", Org.Type.COMPANY);
                    root = orgService.create(root, null);
                    orgService.create(new Org("GEN", "General Intake Window", Org.Type.DEPARTMENT), root.getId());
                    orgService.create(new Org("TAX", "Tax Bureau", Org.Type.DEPARTMENT), root.getId());
                    orgService.create(new Org("SOCIAL", "Social Security Bureau", Org.Type.DEPARTMENT), root.getId());
                    return root;
                });
    }

    private void ensureAdmin(String rootOrgId) {
        String tenantId = TenantContext.tenant();
        if (userRepository.findByTenantIdAndUsername(tenantId, "admin").isEmpty()) {
            User admin = new User("admin", "Platform Administrator", rootOrgId);
            admin.setTenantId(tenantId);
            admin.setEmail("admin@jeegit.io");
            userRepository.save(admin);
        }
    }

    private void ensureDefaultRoles() {
        String tenantId = TenantContext.tenant();
        if (roleRepository.findByTenantIdAndCode(tenantId, "ROLE_ADMIN").isEmpty()) {
            Role admin = new Role("ROLE_ADMIN", "Administrator", DataScope.ALL);
            admin.setTenantId(tenantId);
            roleRepository.save(admin);
        }
        if (roleRepository.findByTenantIdAndCode(tenantId, "ROLE_DISPATCHER").isEmpty()) {
            Role dispatcher = new Role("ROLE_DISPATCHER", "Intake Dispatcher", DataScope.COMPANY_AND_CHILD);
            dispatcher.setTenantId(tenantId);
            roleRepository.save(dispatcher);
        }
    }

    private void ensureDispatchRuleDict() {
        ensureType("MATTER_DISPATCH_RULE", "Matter Dispatch Rules",
                "key = target department; value = comma-separated keywords; sort = match priority",
                true);
        upsertItem("MATTER_DISPATCH_RULE", "Tax Bureau",
                "Tax Bureau — tax / invoice / filing matters",
                "tax,invoice,filing,税,发票,纳税", 10);
        upsertItem("MATTER_DISPATCH_RULE", "Social Security Bureau",
                "Social Security Bureau — social / medical / pension matters",
                "social,medical,pension,社保,医保,养老", 20);
        upsertItem("MATTER_DISPATCH_RULE", "Market Regulation Bureau",
                "Market Regulation Bureau — business licensing matters",
                "market,license,business,工商,营业执照", 30);
        upsertItem("MATTER_DISPATCH_RULE", "Civil Affairs Bureau",
                "Civil Affairs Bureau — household registration / ID matters",
                "civil,household,id card,户籍,身份证", 40);
        upsertItem("MATTER_DISPATCH_RULE", "Complaints Office",
                "Complaints Office — complaint / petition matters",
                "complaint,petition,投诉,信访", 50);
    }

    private void ensureType(String code, String name, String description, boolean system) {
        DictType type = new DictType(code, name, description, system);
        dictService.upsertType(type);
    }

    private void upsertItem(String typeCode, String key, String label, String value, int sort) {
        DictItem item = new DictItem(typeCode, key, label, value, sort, null);
        dictService.upsertItem(item);
    }
}

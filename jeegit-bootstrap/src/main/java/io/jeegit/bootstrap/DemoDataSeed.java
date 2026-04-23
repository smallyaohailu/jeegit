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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 启动时的演示数据种子。保证第一次启动即可跑通 README 中所有 demo。
 * 在生产环境请通过外部迁移（Flyway）而不是本类装载数据。
 */
@Component
public class DemoDataSeed implements CommandLineRunner {

    private final TenantRepository tenantRepository;
    private final OrgService orgService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DictService dictService;

    @Autowired
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
            tenant.setName("默认租户");
            tenant.setDescription("平台内置默认租户，用于演示与首次运行验证。");
            tenantRepository.save(tenant);
        }
    }

    private Org ensureOrgTree() {
        return orgService.listForCurrentTenant().stream()
                .filter(o -> o.getTreeLevel() == 0)
                .findFirst()
                .orElseGet(() -> {
                    Org root = new Org("ROOT", "默认公司", Org.Type.COMPANY);
                    root = orgService.create(root, null);
                    Org dept = new Org("GEN", "综合受理窗口", Org.Type.DEPARTMENT);
                    orgService.create(dept, root.getId());
                    Org tax = new Org("TAX", "税务局", Org.Type.DEPARTMENT);
                    orgService.create(tax, root.getId());
                    Org social = new Org("SOCIAL", "社保局", Org.Type.DEPARTMENT);
                    orgService.create(social, root.getId());
                    return root;
                });
    }

    private void ensureAdmin(String rootOrgId) {
        String tenantId = TenantContext.tenant();
        if (userRepository.findByTenantIdAndUsername(tenantId, "admin").isEmpty()) {
            User admin = new User("admin", "平台管理员", rootOrgId);
            admin.setTenantId(tenantId);
            admin.setEmail("admin@jeegit.io");
            userRepository.save(admin);
        }
    }

    private void ensureDefaultRoles() {
        String tenantId = TenantContext.tenant();
        if (roleRepository.findByTenantIdAndCode(tenantId, "ROLE_ADMIN").isEmpty()) {
            Role admin = new Role("ROLE_ADMIN", "管理员", DataScope.ALL);
            admin.setTenantId(tenantId);
            roleRepository.save(admin);
        }
        if (roleRepository.findByTenantIdAndCode(tenantId, "ROLE_DISPATCHER").isEmpty()) {
            Role dispatcher = new Role("ROLE_DISPATCHER", "受理分派员", DataScope.COMPANY_AND_CHILD);
            dispatcher.setTenantId(tenantId);
            roleRepository.save(dispatcher);
        }
    }

    private void ensureDispatchRuleDict() {
        ensureType("MATTER_DISPATCH_RULE", "事项分派规则",
                "key=目标部门，value=关键词列表（逗号分隔），sort=匹配优先级", true);
        upsertItem("MATTER_DISPATCH_RULE", "税务局",
                "税务局 —— 税费/税务/发票/纳税相关", "税,tax,发票,纳税", 10);
        upsertItem("MATTER_DISPATCH_RULE", "社保局",
                "社保局 —— 社保/医保/养老相关", "社保,医保,养老,social", 20);
        upsertItem("MATTER_DISPATCH_RULE", "市场监督管理局",
                "市场监督管理局 —— 工商/营业执照相关", "工商,营业执照,market,license", 30);
        upsertItem("MATTER_DISPATCH_RULE", "公安局户政科",
                "公安局户政科 —— 户籍/身份证相关", "户籍,身份证,civil", 40);
        upsertItem("MATTER_DISPATCH_RULE", "信访办",
                "信访办 —— 投诉/信访相关", "投诉,信访,complaint", 50);
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

package io.jeegit.bootstrap;

import io.jeegit.tech.tenant.Tenant;
import io.jeegit.tech.tenant.TenantRepository;
import io.jeegit.tech.iam.User;
import io.jeegit.tech.iam.UserRepository;
import io.jeegit.common.JeegitConstants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@SpringBootApplication(scanBasePackages = "io.jeegit")
@EntityScan(basePackages = "io.jeegit")
@EnableJpaRepositories(basePackages = "io.jeegit")
public class JeegitApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeegitApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(TenantRepository tenantRepository, UserRepository userRepository) {
        return args -> {
            if (!tenantRepository.existsById(JeegitConstants.DEFAULT_TENANT)) {
                tenantRepository.save(new Tenant(
                        JeegitConstants.DEFAULT_TENANT,
                        "默认租户",
                        "平台内置默认租户（用于首次启动演示）"));
            }
            if (userRepository.findByTenantId(JeegitConstants.DEFAULT_TENANT).isEmpty()) {
                userRepository.save(new User(
                        UUID.randomUUID().toString(),
                        JeegitConstants.DEFAULT_TENANT,
                        "admin",
                        "平台管理员",
                        "综合受理窗口"));
            }
        };
    }
}

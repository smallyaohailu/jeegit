package io.jeegit.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.jeegit")
@EntityScan(basePackages = "io.jeegit")
@EnableJpaRepositories(basePackages = "io.jeegit")
@EnableJpaAuditing(auditorAwareRef = "tenantAwareAuditorAware")
public class JeegitApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeegitApplication.class, args);
    }
}

package com.example.taskmanager.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
    /**
     * getCurrentAuditor 상속받아 custom 진행 기존 로직 제거
     * @author douglas
     * @since 2024-05-07
     */
    @Bean
    public UserAuditorAware auditorProvider() {
        return new UserAuditorAware();
    }

}

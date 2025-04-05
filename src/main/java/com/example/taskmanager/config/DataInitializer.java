package com.example.taskmanager.config;

import com.example.taskmanager.domain.User;
import com.example.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 초기 사용자 생성
            if (userRepository.count() == 0) {
                // 관리자 사용자 생성
                Set<String> adminRoles = new HashSet<>();
                adminRoles.add("ROLE_ADMIN");
                adminRoles.add("ROLE_USER");
                
                User admin = new User(
                    null, 
                    "admin", 
                    passwordEncoder.encode("admin123"), 
                    "admin@taskmanager.com", 
                    "Admin User", 
                    adminRoles
                );
                
                userRepository.save(admin);
                
                // 일반 사용자 생성
                Set<String> userRoles = new HashSet<>();
                userRoles.add("ROLE_USER");
                
                User user = new User(
                    null, 
                    "user", 
                    passwordEncoder.encode("user123"), 
                    "user@taskmanager.com", 
                    "Normal User", 
                    userRoles
                );
                
                userRepository.save(user);
                
                System.out.println("Initial users have been created.");
            }
        };
    }
}

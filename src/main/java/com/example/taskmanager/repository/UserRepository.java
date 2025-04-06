package com.example.taskmanager.repository;

import com.example.taskmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    
    Optional<User> findByUsername(String username);
    Optional<User> findByRefreshToken(String refreshToken);
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}

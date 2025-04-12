package com.example.taskmanager.repository;

import com.example.taskmanager.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {
            "roles"
    })
    @Query("SELECT u FROM User u " +
            " JOIN u.roles " +
            " WHERE u.id = :userId")
    Optional<User> findById(Long userId);


    @EntityGraph(attributePaths = {
            "roles"
    })
    @Query("SELECT u FROM User u " +
            " JOIN u.roles " +
            " WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

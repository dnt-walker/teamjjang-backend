package com.example.taskmanager.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    
    @Column(name = "username", unique = true, nullable = false, length = 128)
    private String username;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Column(name = "email", length = 128)
    private String email;
    
    @Column(name = "full_name", length = 128)
    private String fullName;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", length = 128)
    private Set<String> roles = new HashSet<>();

    @Setter
    @Column(name = "refresh_token", length = 512)
    private String refreshToken;

    @Setter
    @Column(name = "refresh_token_expiry")
    private LocalDateTime refreshTokenExpiry;

    @Builder
    public User(Long id, String username, String password, String email, String fullName, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        
        if (roles != null) {
            this.roles = new HashSet<>(roles);
        }
    }
    
    // 역할 추가 메서드
    public void addRole(String role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }
    
    // 역할 제거 메서드
    public void removeRole(String role) {
        if (this.roles != null) {
            this.roles.remove(role);
        }
    }
    
    // 패스워드 변경 메서드
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
    
    // 사용자 정보 업데이트 메서드
    public void update(String email, String fullName) {
        if (email != null) this.email = email;
        if (fullName != null) this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
    
    // 추가 필드의 getter/setter 호환성을 위한 메서드
//    public String getName() {
//        return this.fullName;
//    }
    
    public boolean isActive() {
        return true; // 기본적으로 모든 사용자는 활성 상태
    }
    
    public String getDepartment() {
        return null; // 기존 엔티티에 없는 필드
    }
    
    public String getPosition() {
        return null; // 기존 엔티티에 없는 필드
    }
}

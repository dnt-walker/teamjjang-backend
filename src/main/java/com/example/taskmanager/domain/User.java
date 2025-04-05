package com.example.taskmanager.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    private String email;
    private String fullName;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();
    
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
}

package com.example.taskmanager.dto;

import com.example.taskmanager.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    private String fullName;
    private String email;
    private String department;
    private String position;
    private boolean active;
    private Set<String> roles;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .position(user.getPosition())
                .active(user.isActive())
                .roles(user.getRoles())
                .build();
    }

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .fullName(this.fullName)
                .roles(this.roles)
                .build();
    }
}

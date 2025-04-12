package com.example.taskmanager.dto;

import com.example.taskmanager.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자(User)에 대한 데이터 전송 객체")
public class UserDto {
    @Schema(description = "사용자 ID", example = "1")
    private Long id;
    
    @Schema(description = "문자 사용자 ID", example = "john.doe")
    private String username;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "비밀번호 (쓰기 전용)", example = "password123")
    private String password;
    
    @Schema(description = "이름", example = "John Doe")
    private String fullName;
    
    @Schema(description = "이메일", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "역할 목록", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private Set<String> roles;

    // 요약 정보만 포함하는 내부 클래스 (assignee 정보 표시용)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSummaryDto {
        private Long id;
        private String username;
        private String fullName;
        
        public static UserSummaryDto from(User user) {
            if (user == null) return null;
            
            return UserSummaryDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .build();
        }
    }

    public static UserDto from(User user) {
        if (user == null) return null;
        
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
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

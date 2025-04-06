package com.example.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    
    @Email(message = "유효한 이메일 주소를 입력해주세요")
    private String email;
    
    @Size(max = 128, message = "이름은 128자를 초과할 수 없습니다")
    private String fullName;
    
    // 필요한 경우 비밀번호 변경 필드 추가
    private String currentPassword;
    private String newPassword;
}

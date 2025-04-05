package com.example.taskmanager.controller;

import com.example.taskmanager.dto.JwtResponseDto;
import com.example.taskmanager.dto.LoginRequestDto;
import com.example.taskmanager.dto.SignupRequestDto;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "인증 관련 API")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "새로운 사용자 계정을 생성합니다")
    @ApiResponse(responseCode = "200", description = "회원 가입 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody SignupRequestDto signupRequest) {
        UserDto userDto = authService.registerUser(signupRequest);
        return ResponseEntity.ok(userDto);
    }
    
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 인증 후 JWT 토큰을 발급합니다")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class)))
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        JwtResponseDto jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
    
    @GetMapping("/me")
    @Operation(summary = "현재 사용자 정보", description = "현재 인증된 사용자의 정보를 조회합니다")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDto userDto = authService.getUserInfo(username);
        return ResponseEntity.ok(userDto);
    }
}

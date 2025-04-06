package com.example.taskmanager.controller;

import com.example.taskmanager.dto.*;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "회원 가입 신청 정보") SignupRequestDto signupRequest, @AuthenticationPrincipal UserDetails userDetails) {
        UserDto userDto = authService.registerUser(signupRequest);
        return ResponseEntity.ok(userDto);
    }
    
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 인증 후 JWT 토큰을 발급합니다")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class)))
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody
                                                           @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "로그인 요청 정보") LoginRequestDto loginRequest,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        JwtResponseDto jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
    
    @GetMapping("/me")
    @Operation(summary = "현재 사용자 정보", description = "현재 인증된 사용자의 정보를 조회합니다")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDto userDto = authService.getUserInfo(username);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/auth/refresh")
    @Operation(summary = "토큰 새로 발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다")
    public ResponseEntity<JwtResponseDto> refreshToken(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "리프래시 토큰 정보") RefreshTokenRequestDto request,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        JwtResponseDto jwtResponse = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/auth/logout")
    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리를 합니다")
    public ResponseEntity<?> logout(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "리프래시 토큰 정보") RefreshTokenRequestDto request,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        authService.invalidateRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }
}

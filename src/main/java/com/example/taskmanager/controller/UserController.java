package com.example.taskmanager.controller;

import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "사용자 관리 API")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    @Operation(summary = "모든 사용자 목록 조회", description = "시스템의 모든 사용자 목록을 조회합니다.")
    public ResponseEntity<List<UserDto>> getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{username}")
    @Operation(summary = "사용자 상세 정보 조회", description = "특정 사용자명의 상세 정보를 조회합니다.")
    public ResponseEntity<UserDto> getUserByUsername(
            @PathVariable @Parameter(description = "조회할 사용자명") String username,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }


}

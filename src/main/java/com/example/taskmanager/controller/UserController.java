package com.example.taskmanager.controller;

import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.dto.UserUpdateDto;
import com.example.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "사용자 관리", description = "사용자 관리 API")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/current")
    @Operation(summary = "현재 사용자 정보", description = "현재 로그인한 사용자의 정보를 조회합니다")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "모든 사용자 목록 조회", description = "관리자 권한으로 모든 사용자의 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<List<UserDto>> getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "사용자 정보 조회", description = "특정 ID의 사용자 정보를 조회합니다")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<UserDto> getUserById(
            @PathVariable @Parameter(description = "사용자 ID") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        // 일반 사용자는 자신의 정보만 조회 가능, 관리자는 모든 사용자 조회 가능
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        if (!isAdmin && !userDetails.getUsername().equals(userService.getUserById(id).getUsername())) {
            return ResponseEntity.status(403).build();
        }
        
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "사용자명으로 사용자 조회", description = "특정 사용자명을 가진 사용자 정보를 조회합니다")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<UserDto> getUserByUsername(
            @PathVariable @Parameter(description = "사용자명") String username,
            @AuthenticationPrincipal UserDetails userDetails) {
        // 일반 사용자는 자신의 정보만 조회 가능, 관리자는 모든 사용자 조회 가능
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        if (!isAdmin && !userDetails.getUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }
        
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "사용자 정보 수정", description = "특정 ID의 사용자 정보를 수정합니다")
    @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<UserDto> updateUser(
            @PathVariable @Parameter(description = "사용자 ID") Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 사용자 정보") UserUpdateDto userUpdateDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        // 일반 사용자는 자신의 정보만 수정 가능, 관리자는 모든 사용자 수정 가능
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        if (!isAdmin && !userDetails.getUsername().equals(userService.getUserById(id).getUsername())) {
            return ResponseEntity.status(403).build();
        }
        
        UserDto updatedUser = userService.updateUser(id, userUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "사용자 삭제", description = "특정 ID의 사용자를 삭제합니다 (관리자 전용)")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Parameter(description = "사용자 ID") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "사용자 검색", description = "사용자명, 이메일 또는 전체 이름으로 사용자를 검색합니다")
    @ApiResponse(responseCode = "200", description = "사용자 검색 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<List<UserDto>> searchUsers(
            @RequestParam(required = false) @Parameter(description = "사용자명") String username,
            @RequestParam(required = false) @Parameter(description = "이메일") String email,
            @RequestParam(required = false) @Parameter(description = "전체 이름") String fullName,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<UserDto> users = userService.searchUsers(username, email, fullName);
        return ResponseEntity.ok(users);
    }
}

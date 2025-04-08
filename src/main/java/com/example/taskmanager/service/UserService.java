package com.example.taskmanager.service;

import com.example.taskmanager.domain.User;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        return UserDto.from(user);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }

        // 실제 환경에서는 여기서 비밀번호 암호화 처리를 해야 합니다
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userDto.toEntity();

        // 역할이 설정되지 않은 경우 기본 역할 설정
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Set<String> roles = new HashSet<>();
            roles.add("ROLE_USER");
            // User 생성자를 통해 역할 지정 - 기존 User 클래스에는 setter가 없음
            user = User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .build();
        }
        
        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        
        // ID가 다르면 다른 사용자의 username인지 확인
        if (!existingUser.getUsername().equals(userDto.getUsername()) && 
            userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }
        
        // 기존 User 엔티티는 update 메서드를 통해 수정
        existingUser.update(userDto.getEmail(), userDto.getFullName());
        
        // 비밀번호가 제공된 경우에만 수정
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            // 실제 환경에서는 여기서 비밀번호 암호화 처리를 해야 합니다
            // existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            existingUser.changePassword(userDto.getPassword());
        }
        
        // 역할 정보 업데이트 - 기존 엔티티의 역할 관리 방식 사용
        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            // 기존 역할 모두 제거
            for (String role : new HashSet<>(existingUser.getRoles())) {
                existingUser.removeRole(role);
            }
            // 새 역할 추가
            for (String role : userDto.getRoles()) {
                existingUser.addRole(role);
            }
        }
        
        User updatedUser = userRepository.save(existingUser);
        return UserDto.from(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}

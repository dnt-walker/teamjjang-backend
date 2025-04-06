package com.example.taskmanager.service;

import com.example.taskmanager.domain.User;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.dto.UserUpdateDto;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.example.taskmanager.domain.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 모든 사용자 목록을 조회합니다.
     * 
     * @return 사용자 DTO 목록
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * ID로 사용자를 조회합니다.
     * 
     * @param id 사용자 ID
     * @return 사용자 DTO
     */
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return convertToDto(user);
    }
    
    /**
     * 사용자명으로 사용자를 조회합니다.
     * 
     * @param username 사용자명
     * @return 사용자 DTO
     */
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return convertToDto(user);
    }
    
    /**
     * 사용자 정보를 수정합니다.
     * 
     * @param id 사용자 ID
     * @param userUpdateDto 업데이트할 사용자 정보
     * @return 업데이트된 사용자 DTO
     */
    @Transactional
    public UserDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // 이메일 및 이름 업데이트
        user.update(userUpdateDto.getEmail(), userUpdateDto.getFullName());
        
        // 비밀번호 변경이 요청된 경우 처리
        if (StringUtils.hasText(userUpdateDto.getCurrentPassword()) 
                && StringUtils.hasText(userUpdateDto.getNewPassword())) {
            // 현재 비밀번호 검증
            if (!passwordEncoder.matches(userUpdateDto.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
            }
            
            // 새 비밀번호로 업데이트
            user.changePassword(passwordEncoder.encode(userUpdateDto.getNewPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }
    
    /**
     * 사용자를 삭제합니다.
     * 
     * @param id 사용자 ID
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        
        userRepository.deleteById(id);
    }
    
    /**
     * 사용자를 검색합니다.
     * 
     * @param username 사용자명 (선택)
     * @param email 이메일 (선택)
     * @param fullName 전체 이름 (선택)
     * @return 검색 결과 사용자 DTO 목록
     */
    @Transactional(readOnly = true)
    public List<UserDto> searchUsers(String username, String email, String fullName) {
        QUser qUser = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();
        
        if (StringUtils.hasText(username)) {
            builder.and(qUser.username.containsIgnoreCase(username));
        }
        
        if (StringUtils.hasText(email)) {
            builder.and(qUser.email.containsIgnoreCase(email));
        }
        
        if (StringUtils.hasText(fullName)) {
            builder.and(qUser.fullName.containsIgnoreCase(fullName));
        }
        
        Iterable<User> users = userRepository.findAll(builder);
        
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * User 엔티티를 UserDto로 변환합니다.
     * 
     * @param user User 엔티티
     * @return UserDto
     */
    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }
}

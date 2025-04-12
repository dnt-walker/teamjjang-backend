package com.example.taskmanager.service;

import com.example.taskmanager.domain.SubTaskAssignedUser;
import com.example.taskmanager.domain.TaskAssignedUser;
import com.example.taskmanager.domain.User;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.repository.SubTaskAssignedUserRepository;
import com.example.taskmanager.repository.TaskAssignedUserRepository;
import com.example.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    /**
     * 모든 사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자명으로 사용자 조회
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with userId: " + id));
    }

    /**
     * 사용자명으로 사용자 조회
     */
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found with username: " + username));
        return UserDto.from(user);
    }
    

}

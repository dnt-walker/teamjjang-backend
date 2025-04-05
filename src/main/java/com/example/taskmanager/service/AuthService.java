package com.example.taskmanager.service;

import com.example.taskmanager.domain.User;
import com.example.taskmanager.dto.JwtResponseDto;
import com.example.taskmanager.dto.LoginRequestDto;
import com.example.taskmanager.dto.SignupRequestDto;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    
    @Transactional
    public UserDto registerUser(SignupRequestDto signupRequest) {
        // Check username
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        
        // Check email
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        
        // Create new user's account
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        
        User user = new User(
            null,
            signupRequest.getUsername(),
            passwordEncoder.encode(signupRequest.getPassword()),
            signupRequest.getEmail(),
            signupRequest.getFullName(),
            roles
        );
        
        User savedUser = userRepository.save(user);
        
        return convertToDto(savedUser);
    }
    
    public JwtResponseDto authenticateUser(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", loginRequest.getUsername()));
        
        return new JwtResponseDto(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
    
    public UserDto getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        return convertToDto(user);
    }
    
    private UserDto convertToDto(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName()
        );
    }
}

package com.example.taskmanager.security;

import com.example.taskmanager.domain.User;
import com.example.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        log.info("## User found with user: {}", user.toString());
        log.info("매칭 여부: {}", passwordEncoder.encode("123456"));
        log.info("매칭 여부: {}", passwordEncoder.matches("123456", user.getPassword()));
        TeamUserDetails userDetails = new TeamUserDetails(user);
        user.getRoles().stream().forEach(role ->userDetails.setUserRole(role));

        return userDetails;
//        new TeamUserDetails()
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .authorities(authorities)
//                .build();
    }
}

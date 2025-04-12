package com.example.taskmanager.config;


import com.example.taskmanager.domain.User;
import com.example.taskmanager.security.TeamUserDetails;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

@NoArgsConstructor
public class UserAuditorAware implements AuditorAware<User> {
    /**
     * getCurrentAuditor 조회 시 반복적인 유저 정보 조회 제거
     * @author douglas
     * @since 2024-05-08
     */
//    @Autowired
//    UserDetailsService userDetailService;

    @Override
    public Optional<User> getCurrentAuditor() {
        // 현재 사용자를 가져오는 로직 구현
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null
                    || authentication.getPrincipal() == null
                    || authentication.getPrincipal().equals("anonymousUser"))
                return Optional.empty();

            TeamUserDetails userDetails = (TeamUserDetails) authentication.getPrincipal();
            return Optional.ofNullable(userDetails.getUser());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
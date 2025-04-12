package com.example.taskmanager.security;


import com.example.taskmanager.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * CustomUserDetails로 이름 변경
 * @author douglas
 * @since 2024-04-25
 */
@Data
@NoArgsConstructor
//@Slf4j
@ToString
public class TeamUserDetails implements UserDetails, Serializable {

    private User user;
    public TeamUserDetails(User user) {
        this.user = user;
    }

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public User getUser() {
        return this.user;
    }

    private Set<GrantedAuthority> getUserRole(String roleName) {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(roleName));
        return roles;
    }

    public void setUserRole(String roleName) {
        authorities.add(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public String getFullName() {
        return user.getFullName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

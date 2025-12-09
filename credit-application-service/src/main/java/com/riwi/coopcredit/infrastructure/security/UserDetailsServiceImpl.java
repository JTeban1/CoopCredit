package com.riwi.coopcredit.infrastructure.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository.UserJpaRepository;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userRepository;

    public UserDetailsServiceImpl(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found with username: " + username));

        return new User(
            userEntity.getUsername(),
            userEntity.getPassword(),
            userEntity.isEnabled(),
            true,
            true,
            true,
            mapRolesToAuthorities(userEntity.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(
            Collection<RoleEntity> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }
}

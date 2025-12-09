package com.riwi.coopcredit.infrastructure.adapter.out.persistence.adapter;

import com.riwi.coopcredit.domain.model.User;
import com.riwi.coopcredit.domain.port.out.UserRepositoryPort;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository.RoleJpaRepository;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository,
                                 RoleJpaRepository roleJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    private UserEntity toEntity(User user) {
        Set<RoleEntity> roleEntities = new HashSet<>();
        if (user.getRoles() != null) {
            for (User.Role role : user.getRoles()) {
                roleJpaRepository.findByName(role.name())
                    .ifPresent(roleEntities::add);
            }
        }

        return UserEntity.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .email(user.getEmail())
            .enabled(user.isEnabled())
            .affiliateId(user.getAffiliateId())
            .roles(roleEntities)
            .build();
    }

    private User toDomain(UserEntity entity) {
        Set<User.Role> roles = entity.getRoles().stream()
            .map(roleEntity -> User.Role.valueOf(roleEntity.getName()))
            .collect(Collectors.toSet());

        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setEmail(entity.getEmail());
        user.setEnabled(entity.isEnabled());
        user.setAffiliateId(entity.getAffiliateId());
        user.setRoles(roles);
        return user;
    }
}

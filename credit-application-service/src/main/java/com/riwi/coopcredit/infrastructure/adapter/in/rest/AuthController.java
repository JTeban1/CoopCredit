package com.riwi.coopcredit.infrastructure.adapter.in.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riwi.coopcredit.application.dto.request.LoginRequest;
import com.riwi.coopcredit.application.dto.request.RegisterRequest;
import com.riwi.coopcredit.application.dto.response.AuthResponse;
import com.riwi.coopcredit.application.mapper.AffiliateMapper;
import com.riwi.coopcredit.domain.model.Affiliate;
import com.riwi.coopcredit.domain.model.User;
import com.riwi.coopcredit.domain.port.in.RegisterAffiliateUseCase;
import com.riwi.coopcredit.domain.port.out.UserRepositoryPort;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;
import com.riwi.coopcredit.infrastructure.exception.ErrorCode;
import com.riwi.coopcredit.infrastructure.security.JwtTokenProvider;

import io.micrometer.core.instrument.Counter;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterAffiliateUseCase registerAffiliateUseCase;
    private final AffiliateMapper affiliateMapper;
    private final Counter authenticationFailureCounter;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepositoryPort userRepository,
                          PasswordEncoder passwordEncoder,
                          RegisterAffiliateUseCase registerAffiliateUseCase,
                          AffiliateMapper affiliateMapper,
                          @Qualifier("authenticationFailureCounter") Counter authenticationFailureCounter) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerAffiliateUseCase = registerAffiliateUseCase;
        this.affiliateMapper = affiliateMapper;
        this.authenticationFailureCounter = authenticationFailureCounter;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);

            User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS,
                    "User not found"));

            Set<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

            AuthResponse response = new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                roles,
                user.getAffiliateId()
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            authenticationFailureCounter.increment();
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException(ErrorCode.AFFILIATE_ALREADY_EXISTS,
                "Username is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.AFFILIATE_ALREADY_EXISTS,
                "Email is already registered");
        }

        Long affiliateId = null;
        if (request.affiliate() != null) {
            Affiliate affiliate = affiliateMapper.toDomain(request.affiliate());
            Affiliate savedAffiliate = registerAffiliateUseCase.register(affiliate);
            affiliateId = savedAffiliate.getId();
        }

        Set<User.Role> roles = new HashSet<>();
        roles.add(User.Role.ROLE_AFILIADO);

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setEnabled(true);
        user.setRoles(roles);
        user.setAffiliateId(affiliateId);

        User savedUser = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        Set<String> roleNames = savedUser.getRoles().stream()
            .map(Enum::name)
            .collect(Collectors.toSet());

        AuthResponse response = new AuthResponse(
            token,
            savedUser.getUsername(),
            savedUser.getEmail(),
            roleNames,
            savedUser.getAffiliateId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

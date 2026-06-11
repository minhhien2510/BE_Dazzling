package io.dazzling.dazzling_backend.service;

import io.dazzling.dazzling_backend.dto.AuthResponse;
import io.dazzling.dazzling_backend.dto.LoginRequest;
import io.dazzling.dazzling_backend.dto.LogoutRequest;
import io.dazzling.dazzling_backend.dto.RefreshTokenRequest;
import io.dazzling.dazzling_backend.dto.RegisterRequest;
import io.dazzling.dazzling_backend.entity.RefreshToken;
import io.dazzling.dazzling_backend.entity.User;
import io.dazzling.dazzling_backend.repository.RefreshTokenRepository;
import io.dazzling.dazzling_backend.repository.UserRepository;
import io.dazzling.dazzling_backend.security.JwtTokenService;
import io.dazzling.dazzling_backend.dto.LogoutResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public AuthResponse register(@Valid RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role("USER")
                .build();

        userRepository.save(user);

        String accessToken = jwtTokenService.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtTokenService.generateRefreshToken(user.getEmail(), user.getRole());
        saveRefreshToken(user.getEmail(), user.getRole(), refreshToken);
        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    @Transactional
    public AuthResponse login(@Valid LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String accessToken = jwtTokenService.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtTokenService.generateRefreshToken(user.getEmail(), user.getRole());
        saveRefreshToken(user.getEmail(), user.getRole(), refreshToken);
        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtTokenService.validateRefreshToken(request.refreshToken())) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        RefreshToken existing = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (existing.isRevoked() || existing.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token is expired or revoked");
        }

        existing.setRevoked(true);
        refreshTokenRepository.save(existing);

        String email = jwtTokenService.getEmailFromToken(request.refreshToken());
        String role = jwtTokenService.getRoleFromToken(request.refreshToken());
        String accessToken = jwtTokenService.generateAccessToken(email, role);
        String refreshToken = jwtTokenService.generateRefreshToken(email, role);
        saveRefreshToken(email, role, refreshToken);
        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    @Transactional
    public LogoutResponse logout(LogoutRequest request) {
        return refreshTokenRepository.findByToken(request.refreshToken())
                .map(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                    return new LogoutResponse(true, "Refresh token revoked");
                })
                .orElseGet(() -> new LogoutResponse(false, "Refresh token not found"));
    }

    private void saveRefreshToken(String email, String role, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .email(email)
                .role(role)
                .revoked(false)
                .expiresAt(Instant.now().plusMillis(jwtTokenService.getRefreshExpirationMs()))
                .build();

        refreshTokenRepository.save(token);
    }
}

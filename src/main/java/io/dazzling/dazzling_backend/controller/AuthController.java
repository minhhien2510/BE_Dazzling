package io.dazzling.dazzling_backend.controller;

import io.dazzling.dazzling_backend.dto.AuthResponse;
import io.dazzling.dazzling_backend.dto.LoginRequest;
import io.dazzling.dazzling_backend.dto.RegisterRequest;
import io.dazzling.dazzling_backend.dto.LogoutRequest;
import io.dazzling.dazzling_backend.dto.TokenInfoResponse;
import io.dazzling.dazzling_backend.dto.RefreshTokenRequest;
import io.dazzling.dazzling_backend.dto.UserResponse;
import io.dazzling.dazzling_backend.dto.LogoutResponse;
import io.dazzling.dazzling_backend.security.JwtTokenService;
import io.dazzling.dazzling_backend.service.AuthService;
import io.dazzling.dazzling_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/auth/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/auth/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/auth/refresh")
    public AuthResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @GetMapping("/auth/token-info")
    public TokenInfoResponse tokenInfo(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is required");
        }
        String token = header.substring(7);
        return jwtTokenService.parseTokenInfo(token);
    }

    @PostMapping("/auth/logout")
    public LogoutResponse logout(@Valid @RequestBody LogoutRequest request) {
        return authService.logout(request);
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        String email = authentication.getName();
        return userService.findByEmail(email)
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getAvatarUrl(),
                        user.getRole()
                ))
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
    }
}

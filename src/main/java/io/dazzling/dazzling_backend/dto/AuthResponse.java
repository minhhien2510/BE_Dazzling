package io.dazzling.dazzling_backend.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}

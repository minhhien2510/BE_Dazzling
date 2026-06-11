package io.dazzling.dazzling_backend.dto;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        String avatarUrl,
        String role
) {
}

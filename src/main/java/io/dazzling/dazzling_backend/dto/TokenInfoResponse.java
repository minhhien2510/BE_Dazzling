package io.dazzling.dazzling_backend.dto;

public record TokenInfoResponse(
        String subject,
        String email,
        String role,
        String tokenType,
        long issuedAt,
        long expiresAt
) {
}

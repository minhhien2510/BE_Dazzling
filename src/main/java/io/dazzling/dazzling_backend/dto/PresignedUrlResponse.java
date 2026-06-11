package io.dazzling.dazzling_backend.dto;

public record PresignedUrlResponse(
        String url,
        int expiresIn
) {
}

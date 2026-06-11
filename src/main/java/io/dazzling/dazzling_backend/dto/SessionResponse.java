package io.dazzling.dazzling_backend.dto;

public record SessionResponse(
        Long id,
        Long userId,
        String sessionName,
        String layoutType
) {
}

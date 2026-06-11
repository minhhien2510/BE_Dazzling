package io.dazzling.dazzling_backend.dto;

import java.time.Instant;

public record GalleryResponse(
        Long id,
        Long userId,
        Long sessionId,
        String objectName,
        String fileName,
        Long fileSize,
        Integer width,
        Integer height,
        Instant createdAt
) {
}

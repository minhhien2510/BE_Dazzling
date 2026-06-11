package io.dazzling.dazzling_backend.dto;

public record GalleryUploadResponse(
        Long id,
        String objectName,
        String fileName,
        Long fileSize
) {
}

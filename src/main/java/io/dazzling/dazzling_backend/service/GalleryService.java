package io.dazzling.dazzling_backend.service;

import io.dazzling.dazzling_backend.dto.GalleryResponse;
import io.dazzling.dazzling_backend.dto.GalleryUploadResponse;
import io.dazzling.dazzling_backend.dto.PresignedUrlResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface GalleryService {
    GalleryUploadResponse upload(Long userId, Long sessionId, MultipartFile file) throws IOException;
    Page<GalleryResponse> list(Long userId, Pageable pageable);
    GalleryResponse get(Long userId, Long id);
    PresignedUrlResponse getPresignedUrl(Long userId, Long id);
    void delete(Long userId, Long id);
}

package io.dazzling.dazzling_backend.service.impl;

import io.dazzling.dazzling_backend.dto.GalleryResponse;
import io.dazzling.dazzling_backend.dto.GalleryUploadResponse;
import io.dazzling.dazzling_backend.dto.PresignedUrlResponse;
import io.dazzling.dazzling_backend.entity.GalleryItem;
import io.dazzling.dazzling_backend.exception.StorageException;
import io.dazzling.dazzling_backend.repository.GalleryItemRepository;
import io.dazzling.dazzling_backend.service.GalleryService;
import io.dazzling.dazzling_backend.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final GalleryItemRepository repository;
    private final StorageService storageService;

    @Value("${minio.presigned.expiry-seconds:600}")
    private int presignedUrlExpirySeconds;

    @Override
    public GalleryUploadResponse upload(Long userId, Long sessionId, MultipartFile file) throws IOException {
        String path = "gallery/" + userId;
        String objectName = storageService.uploadFile(path, file.getOriginalFilename(), file.getInputStream(), file.getContentType(), file.getSize());

        GalleryItem item = GalleryItem.builder()
                .userId(userId)
                .sessionId(sessionId)
                .objectName(objectName)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .createdAt(Instant.now())
                .build();

        item = repository.save(item);

            return new GalleryUploadResponse(item.getId(), item.getObjectName(), item.getFileName(), item.getFileSize());
    }

    @Override
    public Page<GalleryResponse> list(Long userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable).map(item -> new GalleryResponse(
                item.getId(), item.getUserId(), item.getSessionId(), item.getObjectName(), item.getFileName(), item.getFileSize(), item.getWidth(), item.getHeight(), item.getCreatedAt()
        ));
    }

    @Override
    public GalleryResponse get(Long userId, Long id) {
        GalleryItem item = repository.findById(id).orElseThrow(() -> new StorageException("Gallery item not found"));
        if (!item.getUserId().equals(userId)) throw new StorageException("Forbidden");
        return new GalleryResponse(item.getId(), item.getUserId(), item.getSessionId(), item.getObjectName(), item.getFileName(), item.getFileSize(), item.getWidth(), item.getHeight(), item.getCreatedAt());
    }

    @Override
    public PresignedUrlResponse getPresignedUrl(Long userId, Long id) {
        GalleryItem item = repository.findById(id).orElseThrow(() -> new StorageException("Gallery item not found"));
        if (!item.getUserId().equals(userId)) throw new StorageException("Forbidden");
        String url = storageService.getPresignedUrl(item.getObjectName(), presignedUrlExpirySeconds);
        return new PresignedUrlResponse(url, presignedUrlExpirySeconds);
    }

    @Override
    public void delete(Long userId, Long id) {
        GalleryItem item = repository.findById(id).orElseThrow(() -> new StorageException("Gallery item not found"));
        if (!item.getUserId().equals(userId)) throw new StorageException("Forbidden");
        storageService.deleteFile(item.getObjectName());
        repository.delete(item);
    }
}

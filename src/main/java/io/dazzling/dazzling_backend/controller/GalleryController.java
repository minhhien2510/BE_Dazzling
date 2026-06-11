package io.dazzling.dazzling_backend.controller;

import io.dazzling.dazzling_backend.dto.GalleryResponse;
import io.dazzling.dazzling_backend.dto.GalleryUploadResponse;
import io.dazzling.dazzling_backend.dto.PresignedUrlResponse;
import io.dazzling.dazzling_backend.service.GalleryService;
import io.dazzling.dazzling_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;

import java.io.IOException;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;
    private final UserService userService;

    @PostMapping("/upload")
    public GalleryUploadResponse upload(@RequestParam(required = false) Long sessionId, @RequestParam("file") MultipartFile file, Authentication auth) throws IOException {
        String email = auth.getName();
        Long userId = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
        return galleryService.upload(userId, sessionId, file);
    }

    @GetMapping
    public Page<GalleryResponse> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size, Authentication auth) {
        String email = auth.getName();
        Long userId = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
        return galleryService.list(userId, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public GalleryResponse get(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        Long userId = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
        return galleryService.get(userId, id);
    }

    @GetMapping("/{id}/presigned-url")
    public PresignedUrlResponse getPresignedUrl(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        Long userId = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
        return galleryService.getPresignedUrl(userId, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        Long userId = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
        galleryService.delete(userId, id);
    }
}

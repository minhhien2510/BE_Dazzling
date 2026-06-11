package io.dazzling.dazzling_backend.service;

import java.io.InputStream;

public interface StorageService {
    String uploadFile(String path, String fileName, InputStream stream, String contentType, long size);
    void deleteFile(String objectName);
    String getPresignedUrl(String objectName, int expirySeconds);
}

package io.dazzling.dazzling_backend.service.impl;

import io.dazzling.dazzling_backend.exception.StorageException;
import io.dazzling.dazzling_backend.service.StorageService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public String uploadFile(String path, String fileName, InputStream stream, String contentType, long size) {
        try {
            String objectName = path + "/" + UUID.randomUUID() + "-" + fileName;
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(stream, size, -1)
                            .contentType(contentType)
                            .build()
            );
            return objectName;
        } catch (Exception ex) {
            throw new StorageException("Failed to upload file", ex);
        }
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
        } catch (Exception ex) {
            throw new StorageException("Failed to delete file", ex);
        }
    }

    @Override
    public String getPresignedUrl(String objectName, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectName)
                            .expiry(expirySeconds)
                            .build()
            );
        } catch (Exception ex) {
            throw new StorageException("Failed to generate presigned URL", ex);
        }
    }
}

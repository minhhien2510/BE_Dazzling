package io.dazzling.dazzling_backend.repository;

import io.dazzling.dazzling_backend.entity.GalleryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long> {
    Page<GalleryItem> findByUserId(Long userId, Pageable pageable);
    List<GalleryItem> findBySessionId(Long sessionId);
}

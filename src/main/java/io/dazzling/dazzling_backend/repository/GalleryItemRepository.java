package io.dazzling.dazzling_backend.repository;

import io.dazzling.dazzling_backend.entity.GalleryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long> {
    Page<GalleryItem> findByUserId(Long userId, Pageable pageable);
    List<GalleryItem> findBySessionId(Long sessionId);

    long countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(Instant start, Instant end);

    @Query("SELECT COALESCE(SUM(g.fileSize), 0) FROM GalleryItem g")
    long sumFileSize();

    @Query(value = """
            SELECT DATE(created_at) AS date, COUNT(*) AS count
            FROM gallery_items
            WHERE created_at >= :start AND created_at < :end
            GROUP BY DATE(created_at)
            ORDER BY date
            """, nativeQuery = true)
    List<Object[]> countByDateRange(@Param("start") Instant start, @Param("end") Instant end);
}
